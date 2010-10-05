/*
 *  Minimax.java
 *
 *  Copyright (c) 2010 Roberto Corradini. All rights reserved.
 *
 *  This file is part of the reversi program
 *  http://github.com/rcrr/reversi
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the
 *  Free Software Foundation; either version 3, or (at your option) any
 *  later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
 *  or visit the site <http://www.gnu.org/licenses/>.
 */

package rcrr.reversi;

public abstract class DecisionRuleAbstract implements DecisionRule {

    /** 
     * The winning value.
     * Integer.MAX_VALUE = 2^31-1 = 2,147,483,647
     * Leaving enough space and having an easy to recognize 
     * number leads to a value of 2000000032.
     */
    protected static final int WINNING_VALUE = + 2000000032;

    /** The losing value. */
    protected static final int LOSING_VALUE = - 2000000032;

    /**
     * Returns the board final value.
     * <p>
     * Should be part of any eval function (or a service).
     *
     * @param board  the final board
     * @param player the player for wich the value is calulated
     * @return       the game final value
     */
    protected static int finalValue(final Board board, final Player player) {
	assert (board != null) : "Parameter board must be not null";
	assert (player != null) : "Parameter player must be not null";
	switch (Integer.signum(board.countDifference(player))) {
	case -1: return LOSING_VALUE;
	case  0: return 0;
	case +1: return WINNING_VALUE;
	default: throw new RuntimeException("Unreachable condition found. player=" + player + ", board=" + board);
	}
    }


    public static Strategy searcher(final int ply, final EvalFunction ef, final DecisionRule dr) {
	if (ply <= 0) throw new IllegalArgumentException("Parameter ply must be greather than zero. ply=" + ply);
	if (ef == null) throw new NullPointerException("Parameter ef must not null. ef=" + ef);
	return new Strategy() {
	    public Square move(GameState gameState) {
		if (!gameState.hasAnyLegalMove()) return null;
		SearchNode node = dr.search(gameState.player(), gameState.board(), ply, ef);
		return node.move();		
	    }
	};
    }


    /**
     * Returns a {@code Strategy} that maximixes the value obtained
     * applying the evaluation function to the avalilable legal moves.
     * <p>
     * The method is equivalent to call a searcher giving a one ply depth
     * search.
     * 
     * @param ef evaluation function
     * @return   a strategy maximizing the value of the legal moves
     */
    public static Strategy maximizer(final EvalFunction ef) {
	return new Strategy() {
	    public Square move(GameState gameState) {
		int value = LOSING_VALUE;
		Square move = null;
		Player player = gameState.player();
		Board board = gameState.board();
		for (Square tentativeMove : board.legalMoves(player)) {
		    int moveValue = ef.eval(player, board.makeMove(tentativeMove, player));
		    if (moveValue > value) {
			value = moveValue;
			move = tentativeMove;
		    }
		}
		return move;
	    }
	};
    }


}