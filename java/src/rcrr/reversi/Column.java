/*
 *  Column.java
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
 * The board column.
 */
public enum Column {
    /**
     * First column.
     */
    A("a"),
    /**
     * Second column.
     */
    B("b"),
    /**
     * Third column.
     */
    C("c"),
    /**
     * Fourth column.
     */
    D("d"),
    /**
     * Fifth column.
     */
    E("e"),
    /**
     * Sixth column.
     */
    F("f"),
    /**
     * Seventh column.
     */
    G("g"),
    /**
     * Eighth column.
     */
    H("h");
    
    private final String label;
    
    Column(String label) {
	this.label = label;
    }

    /**
     * Returns a {@code String} that represents the column's label.
     *
     * @return the column's label
     */
    public String label() { return label; }

    private static List<Column> columns() {
	List<Column> columns = new ArrayList<Column>();
	for (Column c : Column.values()) {
	    columns.add(c);
	}
	return columns;
    }

    private static List<Column> COLUMNS = columns();

    private static int SIZE = COLUMNS.size();

    /**
     * Returns the number of columns.
     *
     * @return the number of columns
     */
    public static int size() { return SIZE; }

    /**
     * Returns the column at the specified position.
     *
     * @return the identified column
     *
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= Column.size())} 
     */
    public static Column getInstance(int index) { return COLUMNS.get(index); }

    /**
     * Returns the column obtained moving by a {@code delta} number of shift, counted with the proper sign.
     * Returns {@code null} if the shift leads outside the column boundaries.
     * For instance:
     * <pre>
     * {@code 
     * Column c0 = Column.A;
     * Column c1 =c0.shift(+1); // c1 is equal to B 
     * }
     *</pre>
     *
     * @return the column identified by the delta shift
     */
    public Column shift(int delta) {
	Column c;
	int index = COLUMNS.indexOf(this) + delta;
	if (index < 0 || index >= SIZE) c = null;
	else c = COLUMNS.get(index);
	return c;
    }


}