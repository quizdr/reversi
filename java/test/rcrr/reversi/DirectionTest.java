/*
 *  DirectionTest.java
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

import org.junit.*;
import static org.junit.Assert.*;

public class DirectionTest {

    @Test
    public void testDelta() {
	assertEquals(Integer.valueOf(-8), Direction.N.delta());
	assertEquals(Integer.valueOf(-7), Direction.NE.delta());
	assertEquals(Integer.valueOf(+1), Direction.E.delta());
	assertEquals(Integer.valueOf(+9), Direction.SE.delta());
	assertEquals(Integer.valueOf(+8), Direction.S.delta());
	assertEquals(Integer.valueOf(+7), Direction.SW.delta());
	assertEquals(Integer.valueOf(-1), Direction.W.delta());
	assertEquals(Integer.valueOf(-9), Direction.NW.delta());
    }

    @Test
    public void testGetDescription() {
	assertEquals("North", Direction.N.description());
	assertEquals("South", Direction.S.description());
	assertEquals("East", Direction.E.description());
	assertEquals("West", Direction.W.description());
	assertEquals("North-West", Direction.NW.description());
	assertEquals("South-West", Direction.SW.description());
	assertEquals("North-East", Direction.NE.description());
	assertEquals("South-East", Direction.SE.description());
    }
   
}