/*
 *  MoveRecord.java
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

import org.joda.time.Instant;

/**
 * An instance of a move record.
 * <p>
 * A {@code MoveRecord} object holds the information of a proposed move.
 * <p>
 * {@code MoveRecord} is immutable.
 */
public class MoveRecord {

    /** The move field. */
    private final Move move;

    /** The clock field. */
    private final Clock clock;

    /** The timestamp field. */
    private final Instant timestamp;;

    /**
     * Private constructor.
     * <p>
     * Parameters {@code move} and {@code clock} must be not null.
     *
     * @param move  the move
     * @param clock the clock
     */
    private MoveRecord(Move move, Clock clock) {
	assert (move != null) : "Parameter move cannot be null.";
	assert (clock != null) : "Parameter clock cannot be null.";
	this.move = move;
	this.clock = clock;
	this.timestamp = new Instant(System.currentTimeMillis());
    }

    /**
     * Base static factory for the class.
     * <p>
     * Parameter {@code move} cannot be null.
     * Parameter {@code clock} cannnot be null.
     *
     * @param  move  the move send by the player's strategy
     * @param  clock the new clock after the move
     * @return        a new move record
     * @throws NullPointerException when move or clock parameters are null
     */
    public static MoveRecord valueOf(Move move, Clock clock) {
	if (move == null) throw new NullPointerException("Parameter move cannot be null.");
	if (clock == null) throw new NullPointerException("Parameter clock cannot be null.");
	return new MoveRecord(move, clock);
    }

    /**
     * Returns the record creation instant.
     *
     * @return the object creation instant, as registered by the constructor
     */
    public Instant timestamp() { return timestamp; }

    /**
     * Returns the clock field.
     *
     * @return the clock field
     */
    public Clock clock() { return clock; }

    /**
     * Returns the move field.
     *
     * @return the move field
     */
    public Move move() { return move; }

    /**
     * Returns a {@code String} representing the {@code MoveRecord} object.
     *
     * @return a {@code String} representing the move string
     */
    @Override public String toString() {
	return "[" + move() + "; " + clock() + "; " + timestamp + "]";
    }

}