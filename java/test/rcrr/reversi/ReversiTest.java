/*
 *  ReversiTest.java
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

// To do:
// write all tests

package rcrr.reversi;

import org.junit.*;
import static org.junit.Assert.*;

public class ReversiTest {

    @Test
    public void testReversi() {

	assertEquals(-16, Reversi.reversi(new AlphabetaSearcherCountDifference(),
					  new AlphabetaSearcherWeightedSquares(),
					  null,
					  30));

	assertEquals(-16, Reversi.reversi(new MinimaxSearcherCountDifference(),
					  new AlphabetaSearcherWeightedSquares(),
					  null,
					  30));
    }
    
}