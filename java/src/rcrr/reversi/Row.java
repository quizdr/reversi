/*
 *  Row.java
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

import java.util.Map;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;

/**
 * The board row.
 */
public enum Row {
    /**
     * First row.
     */
    R1("1"),
    /**
     * Second row.
     */
    R2("2"),
    /**
     * Third row.
     */
    R3("3"),
    /**
     * Fourth row.
     */
    R4("4"),
    /**
     * Fifth row.
     */
    R5("5"),
    /**
     * Sixth row.
     */
    R6("6"),
    /**
     * Seventh row.
     */
    R7("7"),
    /**
     * Eighth row.
     */
    R8("8");
    
    private final String label;
    
    Row(String label) {
	this.label = label;
    }

    /**
     * Returns a {@code String} that represents the row's label.
     *
     * @return the row's label
     */
    public String label() { return label; }

    private static List<Row> rows() {
	List<Row> rows = new ArrayList<Row>();
	for (Row r : Row.values()) {
	    rows.add(r);
	}
	return rows;
    }

    private static List<Row> ROWS = rows();

    private static int SIZE = ROWS.size();

    /**
     * Returns the number of rows.
     *
     * @return the number of rows
     */
    public static int size() { return SIZE; }

    private static Map<Row, List<Square>> SQUARES_BY_ROW = squaresByRow();

    private static Map<Row, List<Square>> squaresByRow() {
	Map<Row, List<Square>> map = new Hashtable<Row, List<Square>>();
	for (Row r : values()) {
	    List<Square> lst = new ArrayList<Square>();
	    map.put(r, lst);
	}
	return map;
    }

    /**
     * Returns the list of squares belonging to the row.
     *
     * @return the row's squares
     */
    public List<Square> getSquares() {
	return SQUARES_BY_ROW.get(this);
    }

    /**
     * Returns the row at the specified position.
     *
     * @return the identified row
     *
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= Row.size())} 
     */
    public static Row getInstance(int index) { return ROWS.get(index); }

    /**
     * Returns the row obtained moving by a {@code delta} number of shift, counted with the proper sign.
     * Returns {@code null} if the shift leads outside the row boundaries.
     * For instance:
     * <pre>
     * {@code 
     * Row r0 = Row.R1;
     * Row r1 =r0.shift(+1); // r1 is equal to R2 
     * }
     *</pre>
     *
     * @return the row identified by the delta shift
     */
    public Row shift(int delta) {
	Row r;
	int index = ROWS.indexOf(this) + delta;
	if (index < 0 || index >= SIZE) r = null;
	else r = ROWS.get(index);
	return r;
    }


}