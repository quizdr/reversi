/*
 *  FileStateTest.java
 *
 *  Copyright (c) 2010, 2011, 2012 Roberto Corradini. All rights reserved.
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

package rcrr.reversi.board;

import java.util.Map;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * Test Suite for {@code FileState} enum.
 */
public class FileStateTest {

    /** Class constructor. */
    public FileStateTest() { }

    @Test
    public final void testFileIndexClass() {

        final FileState.FileIndex fi = FileState.FileIndex.valueOf(Column.A, 0);

        assertTrue(true);

    }

    @Test
    public final void testFileIndexMoveClass() {

        final FileState.FileIndexMove fim = FileState.FileIndexMove.valueOf(FileState.FileIndex.valueOf(Column.A, 0), 0);

        assertTrue(true);

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testValueOf_when_parameter_index_isInvalid_caseA() {
        FileState.valueOf(8, 6561);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testValueOf_when_parameter_index_isInvalid_caseB() {
        FileState.valueOf(7, 6560);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testValueOf_when_parameter_index_isInvalid_caseC() {
        FileState.valueOf(8, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testValueOf_when_parameter_order_isInvalid_caseA() {
        FileState.valueOf(-1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testValueOf_when_parameter_order_isInvalid_caseB() {
        FileState.valueOf(9, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testValueOf_when_parameter_order_isInvalid_caseC() {
        FileState.valueOf(2, 0);
    }

    /**
     * Test the {@code valueOf(int, int} factory.
     *
     * @see FileState#valueOf(int, int)
     */
    @Test
    public final void testValueOf() {
        assertThat("FileState.valueOf(3, 10) has to be an instance of FileState class.",
                   FileState.valueOf(3, 10),
                   instanceOf(FileState.class));
        assertThat("FileState.valueOf(8, 6560) has to be an instance of FileState class.",
                   FileState.valueOf(8, 6560),
                   instanceOf(FileState.class));
        assertThat("FileState.valueOf(5, 0) has to be an instance of FileState class.",
                   FileState.valueOf(5, 0),
                   instanceOf(FileState.class));
    }

    @Test
    public final void testFlip() {
        assertThat("FileState.valueOf(3, 11).flip() must be FileState.valueOf(3, 19).",
                   FileState.valueOf(3, 11).flip(),
                   is(FileState.valueOf(3, 19)));
        assertThat("Flipping twice has to return the file state itself.",
                   FileState.valueOf(3, 11).flip().flip(),
                   is(FileState.valueOf(3, 11)));
    }

    @Test
    public final void testIndex() {
        assertThat("FileState.valueOf(3, 11).index() must be 11.",
                   FileState.valueOf(3, 11).index(),
                   is(11));
    }

    @Test
    public final void testLegalMoves() {
        final Map<Integer, Integer> result = FileState.valueOf(3, 15).legalMoves();
        assertThat("FileState.valueOf(3, 15).legalMoves() must have ONE entry <0, 13>.",
                   result.size(),
                   is(1));
        assertThat("FileState.valueOf(3, 15).legalMoves() must have one entry <0, 13>, having a 0 key.",
                   result.containsKey(0),
                   is(true));
        assertThat("FileState.valueOf(3, 15).legalMoves() must have one entry <0, 13>, having a 13 value.",
                   result.get(0),
                   is(13));
    }

    @Test
    public final void testOrder() {
        assertThat("FileState.valueOf(3, 11).order() must be 3.",
                   FileState.valueOf(3, 11).order(),
                   is(3));
    }

    @Test
    public final void testToString() {
        assertThat("FileState.valueOf(3, 10).toString() has to return the appropriate string.",
                   FileState.valueOf(3, 10).toString(),
                   is("[(order=3, index=10) [ @ . @ ]]"));
    }

    @Test
    public final void testPrintLegalMoves() {
        /**
         * Turn the statistic into tests ... 
         */
        /*
        for (int order = 3; order <= 8; order ++) {
            int moves = 0;
            int[] moveDistribution = new int[5];
            final int boundary = FileState.indexBoundary(order);
            for (int index = 0; index <= boundary; index++) {
                final FileState fs = FileState.valueOf(order, index);
                final int moveCount = fs.legalMoves().size();
                moves += moveCount;
                switch (moveCount) {
                case 0: moveDistribution[0] += 1; break;
                case 1: moveDistribution[1] += 1; break;
                case 2: moveDistribution[2] += 1; break;
                case 3: moveDistribution[3] += 1; break;
                case 4: moveDistribution[4] += 1; break;
                default: throw new RuntimeException("Too many moves ....");
                }
            }
            System.out.println("order=" + order + ", boundary=" + boundary + ", moves=" + moves
                               + ", D:["
                               + moveDistribution[0] + ", "
                               + moveDistribution[1] + ", "
                               + moveDistribution[2] + ", "
                               + moveDistribution[3] + ", "
                               + moveDistribution[4] + "]");
        }

        System.out.println();

        for (final File file : FileUtils.files()) {
            final List<Square> squares = file.squares();
            final int order = squares.size();
            System.out.println("file=" + file + ", order=" + order + ", squares=" + file.squares());
        }
        */

        assertTrue(true);
    }


}