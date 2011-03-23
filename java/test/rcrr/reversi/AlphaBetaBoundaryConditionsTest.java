/*
 *  AlphaBetaBoundaryConditionsTest.java
 *
 *  Copyright (c) 2010, 2011 Roberto Corradini. All rights reserved.
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

import org.junit.Test;

/**
 * Test suite dedicated to the boundary conditions experienced by the {@code AlphaBeta} class.
 */
public class AlphaBetaBoundaryConditionsTest {

    /** The alphabeta decion rule to test. */
    private final DecisionRule alphabeta = AlphaBeta.getInstance();

    /** Class constructor. */
    public AlphaBetaBoundaryConditionsTest() { }

    /**
     * Tests the alphabetaSearcher {@code ply} parameter range.
     *
     * @see AlphaBeta#searcher(int, EvalFunction)
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testAlphabetaSearcherPlyRange() {
        Strategy s = alphabeta.searcher(0, new CountDifference());
    }

    /**
     * Tests the alphabetaSearcher {@code ef} parameter when it is {@code null}.
     *
     * @see AlphaBeta#searcher(int, EvalFunction)
     */
    @Test(expected = NullPointerException.class)
    public final void testAlphabetaSearcherEfNotNull() {
        Strategy s = alphabeta.searcher(1, null);
    }

}
