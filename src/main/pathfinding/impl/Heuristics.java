package main.pathfinding.impl;

import main.pathfinding.Node;

/**
 * The different possible heuristics used for multiple algorithms.
 *
 * @author Matthew Lillie
 */
public enum Heuristics {

    /**
     * Best used for 4 directional movement
     */
    MANHATTAN {
        @Override
        public float calculate(Node one, Node two) {
            float dx = Math.abs(one.getX() - two.getX());
            float dy = Math.abs(one.getY() - two.getY());
            return STRAIGHT_COST * (dx + dy);
        }
    },

    /**
     * Best used for 8 directional movement
     */
    OCTILE {
        @Override
        public float calculate(Node one, Node two) {
            float dx = Math.abs(one.getX() - two.getX());
            float dy = Math.abs(one.getY() - two.getY());
            return STRAIGHT_COST * (dx + dy) + (DIAGONAL_COST - 2f * STRAIGHT_COST) * Math.min(dx, dy);
        }
    },

    /**
     * Best used for 8 directional movement, though differs slightly with straight/diagonal costs
     */
    CHEBYSHEV {
        @Override
        public float calculate(Node one, Node two) {
            float dx = Math.abs(one.getX() - two.getX());
            float dy = Math.abs(one.getY() - two.getY());
            return STRAIGHT_COST * (dx + dy) + (STRAIGHT_COST - 2f * STRAIGHT_COST) * Math.min(dx, dy);
        }
    },

    /**
     * Best used when you can move in any direction (not limited to grid like movement)
     */
    EUCLIDEAN {
        @Override
        public float calculate(Node one, Node two) {
            float dx = Math.abs(one.getX() - two.getX());
            float dy = Math.abs(one.getY() - two.getY());
            return STRAIGHT_COST * (dx + dx * dy * dy);
        }
    },

    ;

    public abstract float calculate(Node one, Node two);

    public static final float STRAIGHT_COST = 1f;
    public static final float DIAGONAL_COST = 1.41421356237f; // sqrt(2)
}
