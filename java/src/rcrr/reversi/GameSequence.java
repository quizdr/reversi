/*
 *  GameSequence.java
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

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import org.joda.time.Duration;


/**
 * An game sequence instance collects an ordered list of game snapshots.
 * <p>
 * The sequence must have at last one element, and cannot have null values.
 * <p>
 * {@code GameSequence} is immutable.
 */
public final class GameSequence {

    /**
     * An instance of this class encapsulates the information needed to instantiate
     * and initialize a game sequence object. That process is triggered when the {@code build()}
     * method is called.
     * <p>
     * The builder has one property, the {@code snapshots} field. It is initialized as follow:
     * <ul>
     *   <li>{@code snapshots = new ArrayList<GameSnapshot>();}</li>
     *   <li>{@code add(new GameSnapshot.Builder().build());}</li>
     * </ul>
     * <p>
     * The {@code Builder} class is mutable, and it is thread-safe.
     * The object status is guarded by a lock on {@code this}.
     */
    public static final class Builder {

        /** The snapshots field. */
        private List<GameSnapshot> snapshots;

        /**
         * Construct a new builder.
         */
        public Builder() {
            this.snapshots = new ArrayList<GameSnapshot>();
            add(new GameSnapshot.Builder().build());
        }

        /**
         * Returns a new instance of a game sequence object.
         *
         * @return the game sequence instance as prepared by the current game sequence's builder
         */
        public synchronized GameSequence build() {
            return GameSequence.valueOf(snapshots);
        }

        /**
         * The method returns the square's state to the given board's square.
         *
         * @param index index of the game snapshot to return from the sequence
         * @return       the selected game snapshot
         */
        private synchronized GameSnapshot get(final int index) {
            return this.snapshots.get(index);
        }

        /**
         * The method add a game snapshot to the end of the sequence.
         *
         * @param snapshot the game snapshot to be appended to the sequence
         */
        private synchronized void add(final GameSnapshot snapshot) {
            this.snapshots.add(snapshot);
        }

        /**
         * The method insert a game snapshot at the specified position.
         *
         * @param index    the index at which insert the game snapshot
         * @param snapshot the game snapshot to be appended to the sequence
         */
        private synchronized void add(final int index, final GameSnapshot snapshot) {
            this.snapshots.add(index, snapshot);
        }

        /**
         * Replaces the game snapshot at the specified position in this sequence
         * with the specified one.
         *
         * @param index the index at which set the game snapshot
         * @param snapshot the game snapshot to be set in the sequence
         */
        private synchronized void set(final int index, final GameSnapshot snapshot) {
            this.snapshots.set(index, snapshot);
        }

        /**
         * Removes the game snapshot at the specified position in this sequence.
         *
         * @param index the index at which remove the game snapshot
         */
        private synchronized void remove(final int index) {
            this.snapshots.remove(index);
        }

        /**
         * The setter method for the squares field.
         *
         * @param snapshots the update for the snapshots field
         */
        private synchronized void setSnapshots(final List<GameSnapshot> snapshots) {
            this.snapshots = new ArrayList<GameSnapshot>(snapshots);
        }

        /**
         * Returns the {@code this} reference after adding the new {@code snapshot}
         * to the {@code snapshots} field.
         *
         * @param snapshot the added game snapshot
         * @return         the {@code this} reference
         */
        public GameSequence.Builder withAnAddedSnapshot(final GameSnapshot snapshot) {
            add(snapshot);
            return this;
        }

        /**
         * Returns the {@code this} reference after setting the new {@code snapshots}
         * field value.
         *
         * @param snapshots the game snapshot sequence
         * @return          the {@code this} reference
         */
        public GameSequence.Builder withSnapshots(final List<GameSnapshot> snapshots) {
            setSnapshots(snapshots);
            return this;
        }

        /**
         * Returns the {@code this} reference after setting the new {@code snapshots}
         * field value.
         *
         * @param snapshots the game snapshot sequence
         * @return          the {@code this} reference
         */
        public GameSequence.Builder withSnapshots(final GameSnapshot... snapshots) {
            setSnapshots(Arrays.asList(snapshots));
            return this;
        }
    }

   /**
     * A static factory that returns an initial game sequence.
     * <p>
     * The game sequence is composed by one game snapshot. It has a clock
     * valued by the {@code gameDuration} parameter, and an initial game position.
     * <p>
     * Parameter {@code gameDuration} cannot be {@code null}.
     *
     * @param gameDuration the initial game time assigned to players
     * @return             a new game sequence initialised with a initial game snapshot
     *
     * @throws NullPointerException if parameter {@code gameDuration} is null
     */
    public static GameSequence initialGameSequence(final Duration gameDuration) {
        if (gameDuration == null) {
            throw new NullPointerException("Parameter gameDuration cannot be null.");
        }
        return valueOf(Arrays.asList(GameSnapshot.initialGameSnapshot(gameDuration)));
    }

   /**
     * The base static factory.
     * <p>
     * Parameter {@code snapshots} cannot be null, cannot contain null values,
     * and cannot be empty.
     *
     * @param snapshots the sequence of game snapshot
     * @return          the game built with the given sequence
     *
     * @throws NullPointerException if parameter {@code snapshots} is null
     * @throws NullPointerException if parameter {@code snapshots} contains null values
     * @throws IllegalArgumentException if parameter {@code snapshots} is empty
     */
    public static GameSequence valueOf(final List<GameSnapshot> snapshots) {
        if (snapshots == null) {
            throw new NullPointerException("Parameter snapshots cannot be null.");
        }
        if (snapshots.contains(null)) {
            throw new NullPointerException("Parameter snapshots cannot contain null values.");
        }
        if (snapshots.isEmpty()) {
            throw new IllegalArgumentException("Parameter snapshots cannot be empty.");
        }
        return new GameSequence(snapshots);
    }

    /** The game snapshot sequence field. */
    private final List<GameSnapshot> snapshots;

    /**
     * Class constructor.
     * <p>
     * Parameter {@code snapshots} cannot be null, cannot contain null values,
     * and cannot be empty.
     *
     * @param snapshots the sequence of game snapshot
     */
    private GameSequence(final List<GameSnapshot> snapshots) {
        assert (snapshots != null) : "Parameter snapshots cannot be null.";
        assert (!snapshots.contains(null)) : "Parameter snapshots cannot contain null values.";
        assert (!snapshots.isEmpty()) : "Parameter snapshots cannot be empty.";
        this.snapshots = Collections.unmodifiableList(new ArrayList<GameSnapshot>(snapshots));
    }

   /**
     * Returns a new game having the new state added to the current sequence.
     * <p>
     * Parameter {@code gameSnapshot} cannot be null.
     *
     * @param gameSnapshot the new snapshot to add to the sequence
     * @return             a new game modified by adding the game snapshot parameter
     *
     * @throws NullPointerException if parameter {@code gameSnapshot} is null
     */
    public GameSequence add(final GameSnapshot gameSnapshot) {
        if (gameSnapshot == null) {
            throw new NullPointerException("Parameter gameSnapshot cannot be null.");
        }
        final List<GameSnapshot> transientSnapshots = new ArrayList<GameSnapshot>(snapshots);
        transientSnapshots.add(gameSnapshot);
        return valueOf(transientSnapshots);
    }

   /**
     * Returns the game snapshot identified by {@code index}.
     *
     * @param index the game snapshot index in the sequence
     * @return      the game snapshot identified by the index parameter
     *
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    public GameSnapshot get(final int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Parameter index must be greather than 0,"
                                                + " and less or equal to size."
                                                + " index=" + index + ", size=" + size() + ".");
        }
        return snapshots.get(index);
    }

   /**
     * Returns the last game snapshot.
     *
     * @return the last game snapshot
     */
    public GameSnapshot last() {
        return snapshots.get(size() - 1);
    }

    /**
     * Returns the number of game snapshot recordered.
     * <p>
     * When the game starts from the initial game snapshot the size is
     * equal to the number of moves already played plus one.
     *
     * @return the size of the sequence of the game snapshot recordered
     */
    public int size() {
        return snapshots.size();
    }

}
