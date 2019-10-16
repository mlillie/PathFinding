package main.pathfinding.impl;

import main.pathfinding.Node;

public enum Heuristics {

    MANHATTAN {
        @Override
        public float calculate(Node one, Node two) {
            float dx = Math.abs(one.getX() - two.getX());
            float dy = Math.abs(one.getY() - two.getY());
            return STRAIGHT_COST * (dx + dy);
        }
    },

    DIAGONAL {
        @Override
        public float calculate(Node one, Node two) {
            float dx = Math.abs(one.getX() - two.getX());
            float dy = Math.abs(one.getY() - two.getY());
            return STRAIGHT_COST * (dx + dy)  + (DIAGONAL_COST - 2f * STRAIGHT_COST) * Math.min(dx, dy);
        }
    },

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
    public static final float DIAGONAL_COST = (float) Math.sqrt(2);
}
