/*
 *  Reversi.java
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

// Notes:
// - Which practice is best when it comes to write unit test for UI?
//   After a brief search on google the best so far tool to investigate
//   on is UISpec4J (http://www.uispec4j.org).

// To do:
// move away the WINNING and LOSING values.
// javadocs
// junit tests
// run the two strategy with an object having a flexible collection of settings
//   this is close to rewrite the Strategy-Player association ....
//   The Strategy as designed so far is not able to persist between moves.

package rcrr.reversi;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.io.PrintStream;

public class Reversi {

    public static final int WINNING_VALUE = Integer.MAX_VALUE;
    public static final int LOSING_VALUE = - Integer.MAX_VALUE;

    private Reversi() {}

    public static Integer reversi(Strategy blStrategy, Strategy whStrategy, PrintStream ps, Long minutes) {
	GameState gs = GameState.valueOf(Board.initialBoard(), Player.BLACK, Clock.initialClock(minutes));
	Game game = Game.valueOf(Arrays.asList(gs));
	try {
	    for (Player player = gs.getPlayer();
		 player != null;
		 player = gs.getBoard().nextToPlay(player)) {
		gs = GameState.getMoveX(gs.getBoard(), ((player == Player.BLACK) ? blStrategy : whStrategy), player, ps, gs.getClock());
		game.add(gs);
		if (ps != null) {
		    if (gs.getBoard().nextToPlay(player) == player) ps.print("\n" + player.opponent() + " has no moves and must pass.\n");
		}
	    }
	} catch (GameOverException goe) {
	    // to be completed .....
	}
	if (ps != null) {
	    ps.print("\nThe Game is over. Final result:\n\n");
	    // gs.getBoard().print(ps, gs.getClock());
	    ps.print(gs.printGameState());
	}
	return gs.getBoard().countDifference(Player.BLACK);
    }


    private static void usage() {
	System.out.println("usage: java rcrr.reversi.Reversi blackStrategy whiteStrategy");
	System.out.println("\t Where blackStrategy and whiteStrategy are two classes");
	System.out.println("\t that implements the rcrr.reversi.Strategy interface.");
    }

	
    public static void main(String[] args) {
	if (args == null || args.length != 2) {
	    System.out.println("Argument list error: blackStrategy and whiteStrategy must be provided.");
	    usage();
	    System.exit(1);
	}
	Strategy s[] = new Strategy[]{null, null};
	for (int i=0; i<2; i++) {
	    Object o = null;
	    try {
		Class<?> c = Class.forName(args[i]);
		o = c.newInstance();
	    } catch (ClassNotFoundException e) {
		System.out.println("Exception e: " + e);
		usage();
		System.exit(2);
	    } catch (InstantiationException e) {
		System.out.println("Exception e: " + e);
		usage();
		System.exit(3);
	    } catch (IllegalAccessException e) {
		System.out.println("Exception e: " + e);
		usage();
		System.exit(4);
	    }
	    try {
		s[i] = (Strategy) o;
	    } catch (ClassCastException e) {
		System.out.println("Exception e: " + e);
		usage();
		System.exit(5);
	    }
	}
	reversi(s[0], s[1], System.out, null);
    }


}