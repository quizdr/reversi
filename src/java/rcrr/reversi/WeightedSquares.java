/*
 *  WeightedSquares.java
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

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class WeightedSquares implements EvalFunction, Strategy {

    public final static List<Integer> WEIGHTS = Arrays.asList(0,   0,   0,   0,  0,  0,   0,   0,   0, 0,
							      0, 120, -20,  20,  5,  5,  20, -20, 120, 0,
							      0, -20, -40,  -5, -5, -5,  -5, -40, -20, 0,
							      0,  20,  -5,  15,  3,  3,  15,  -5,  20, 0,
							      0,   5,  -5,   3,  3,  3,   3,  -5,   5, 0,
							      0,   5,  -5,   3,  3,  3,   3,  -5,   5, 0,
							      0,  20,  -5,  15,  3,  3,  15,  -5,  20, 0,
							      0, -20, -40,  -5, -5, -5,  -5, -40, -20, 0,
							      0, 120, -20,  20,  5,  5,  20, -20, 120, 0,
							      0,   0,   0,   0,  0,  0,   0,   0,   0, 0);

    Strategy maximizeWeightedCount;

    public WeightedSquares() {
	maximizeWeightedCount = BoardState.maximizer(this);
    }

    public Integer eval(SquareState player, BoardState board) {
	SquareState opp = SquareState.opponent(player);
	Integer value = 0;
	for (int i=0; i<100; i++) {
	    int p;
	    SquareState color = board.get(i);
	    if (color == player) p = 1;
	    else if (color == opp) p = -1;
	    else p = 0;
	    value += p * WEIGHTS.get(i);
	}
	return value;
    }

    public Integer move(SquareState player, BoardState board) {
	return maximizeWeightedCount.move(player, board);
    }

}