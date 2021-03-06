/**
 * @file
 *
 * @brief Random game sampler module implementation.
 * @details Used to generate sample game tree.
 *
 * @par random_game_sampler.c
 * <tt>
 * This file is part of the reversi program
 * http://github.com/rcrr/reversi
 * </tt>
 * @author Roberto Corradini mailto:rob_corradini@yahoo.it
 * @copyright 2013, 2014 Roberto Corradini. All rights reserved.
 *
 * @par License
 * <tt>
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 3, or (at your option) any
 * later version.
 * \n
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * \n
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
 * or visit the site <http://www.gnu.org/licenses/>.
 * </tt>
 */

#include <stdio.h>
#include <stdlib.h>

#include <glib.h>

#include "board.h"
#include "exact_solver.h"
#include "random_game_sampler.h"

#define GAME_TREE_DEBUG



/*
 * Prototypes for internal functions.
 */

static SearchNode *
game_position_random_sampler_impl (      ExactSolution * const result,
                                   const GamePosition  * const gp);

static int
random_number (int low, int high);

static Square
square_set_random_selection (SquareSet squares);



/*
 * Internal variables and constants.
 */

#ifdef GAME_TREE_DEBUG

/* The total number of call to the recursive function that traverse the game DAG. */
static uint64 call_count = 0;

/* The log file used to record the game DAG traversing. */
static FILE *game_tree_debug_file = NULL;

/* The predecessor-successor array of game position hash values. */
static uint64 gp_hash_stack[128];

/* The index of the last entry into gp_hash_stack. */
static int gp_hash_stack_fill_point = 0;

#endif



/*********************************************************/
/* Function implementations for the GamePosition entity. */ 
/*********************************************************/

/**
 * @brief Runs a sequence of random games for number of times equal
 * to the `repeats` parameter, starting from the `root` game position.
 *
 * @param [in] root    the starting game position to be solved
 * @param [in] repeats number of random game to play
 * @return             a pointer to a new exact solution structure
 */
ExactSolution *
game_position_random_sampler (const GamePosition * const root,
                              const int                  repeats)
{
  ExactSolution *result; 
  SearchNode    *sn;

#ifdef GAME_TREE_DEBUG
  gp_hash_stack[0] = 0;
  game_tree_debug_file = fopen("rand_log.csv", "w");
  fprintf(game_tree_debug_file, "%s;%s;%s;%s;%s;%s;%s;%s;%s;%s\n",
          "CALL_ID",
          "HASH",
          "PARENT_HASH",
          "GAME_POSITION",
          "EMPTY_COUNT",
          "CALL_LEVEL",
          "IS_LEF",
          "LEGAL_MOVE_COUNT",
          "LEGAL_MOVE_COUNT_ADJUSTED",
          "MOVE_LIST");
#endif

  srand(time(NULL));
  
  result = exact_solution_new();
  result->solved_game_position = game_position_clone(root);

  for (int repetition = 0; repetition < repeats; repetition++) {
    sn = game_position_random_sampler_impl(result, result->solved_game_position);  
    if (sn) {
      result->principal_variation[0] = sn->move;
      result->outcome = sn->value;
    }
    sn = search_node_free(sn);
  }

#ifdef GAME_TREE_DEBUG
  fclose(game_tree_debug_file);
#endif

  return result;
}



/*
 * Internal functions.
 */

static SearchNode *
game_position_random_sampler_impl (      ExactSolution * const result,
                                   const GamePosition  * const gp)
{
  SearchNode *node;

  node  = NULL;
  result->node_count++;

#ifdef GAME_TREE_DEBUG
  call_count++;
  gp_hash_stack_fill_point++;
  const SquareSet empties = board_empties(gp->board);
  const int empty_count = bit_works_popcount(empties);
  const uint64 hash = game_position_hash(gp);
  gp_hash_stack[gp_hash_stack_fill_point] = hash;
  gchar *gp_to_s = game_position_to_string(gp);
  const gboolean is_leaf = !game_position_has_any_player_any_legal_move(gp);
  const SquareSet lm = game_position_legal_moves(gp);
  const int lm_count = bit_works_popcount(lm);
  const int lm_count_adj = lm_count + ((lm == 0 && !is_leaf) ? 1 : 0);
  gchar *lm_to_s = square_set_print_as_moves(lm);
  fprintf(game_tree_debug_file, "%8lld;%016llx;%016llx;%s;%2d;%2d;%s;%2d;%2d;%78s\n",
          call_count,
          hash,
          gp_hash_stack[gp_hash_stack_fill_point - 1],
          gp_to_s,
          empty_count,
          gp_hash_stack_fill_point,
          is_leaf ? "t" : "f",
          lm_count,
          lm_count_adj,
          lm_to_s);
  g_free(gp_to_s);
  g_free(lm_to_s);
#endif

  if (game_position_has_any_player_any_legal_move(gp)) { // the game must go on
    const SquareSet moves = game_position_legal_moves(gp);
    const int move_count = bit_works_popcount(moves);
    if (move_count == 0) { // player has to pass
      GamePosition *flipped_players = game_position_pass(gp);
      node = search_node_negated(game_position_random_sampler_impl(result, flipped_players));
      flipped_players = game_position_free(flipped_players);
    } else { // regular move
      const Square random_move = square_set_random_selection(moves);
      GamePosition *next_gp = game_position_make_move(gp, random_move);
      node = search_node_negated(game_position_random_sampler_impl(result, next_gp));
      next_gp = game_position_free(next_gp);
    }
  } else { // game-over
    result->leaf_count++;
    node = search_node_new((Square) -1, game_position_final_value(gp));
  }

#ifdef GAME_TREE_DEBUG
  gp_hash_stack_fill_point--;
#endif

  return node;
}


static Square
square_set_random_selection (SquareSet squares)
{
  g_assert(squares != 0ULL);

  const int square_count = bit_works_popcount(squares);
  const int square_index = random_number(0, square_count - 1);
  //printf("square_set_random_selection: square_count=%2d, square_index=%2d\n", square_count, square_index);
  for (int i = 0; i < square_count; i++) {
    if (i == square_index) break;
    squares ^= bit_works_lowest_bit_set_64(squares);
  }
  return (Square) bit_works_bitscanLS1B_64(squares);
}


static int
random_number (int low, int high)
{
  return low + (double) rand() * (high - low + 1) / RAND_MAX;
}
