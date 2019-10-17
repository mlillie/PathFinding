package main.pathfinding.impl;

import main.pathfinding.Grid;
import main.pathfinding.Node;
import main.pathfinding.Pathfinder;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Implementation of iterative deepening a star search. Requires a different way of following the path due to its recursive nature, and
 * is highly dependent on the heuristic used.
 * <p>
 * https://en.wikipedia.org/wiki/Iterative_deepening_A*
 */
public class IDAStarSearch extends Pathfinder {

    /**
     * The heuristic used for the algorithm.
     */
    private final Heuristics heuristic;

    /**
     * Constructs a new Pathfinder
     *
     * @param grid             The Grid used for the algorithm.
     * @param diagonalMovement If the neighbors found are allowed to be diagonal
     * @param heuristic        The heuristic being used for the algorithm.
     */
    public IDAStarSearch(Grid grid, boolean diagonalMovement, Heuristics heuristic) {
        super(grid, diagonalMovement);
        this.heuristic = heuristic;
    }

    @Override
    protected List<Node> doInBackground() throws Exception {
        float threshold = heuristic.calculate(grid.getStartNode(), grid.getGoalNode());

        List<Node> path = new ArrayList<>();

        path.add(grid.getStartNode());

        while (true) {
            Object temp = search(grid.getStartNode(), 0f, threshold, path);

            if (temp == null) {
                break;
            }

            if (temp instanceof Node && temp == grid.getGoalNode()) {
                return path;
            }

            if ((float) temp == Float.MAX_VALUE) {
                return null;
            }

            threshold = (float) temp;
        }

        return null;
    }

    /**
     * Recursive helper function used for the algorithm for searching.
     *
     * @param current   The current node examined.
     * @param g         The g value.
     * @param threshold The maximum cut-off threshold.
     * @param path      The path found.
     * @return A float or a Node depending on the status of the algorithm
     * @throws InterruptedException Algorithm may be interrupted.
     */
    private Object search(Node current, float g, float threshold, List<Node> path) throws InterruptedException {
        float f = g + heuristic.calculate(current, grid.getGoalNode());

        if (f > threshold) {
            return f;
        }

        if (current == grid.getGoalNode()) {
            return current;
        }

        current.incrementTimesVisited();
        Thread.sleep(10);
        grid.repaint();

        float min = Float.MAX_VALUE;

        for (Node neighbor : getNewNeighbors(current)) {

            if (!path.contains(neighbor)) {
                path.add(neighbor);

                Object temp = search(neighbor, g + getMovementCost(current, neighbor), threshold, path);

                if (temp instanceof Node && temp == grid.getGoalNode()) {
                    return temp;
                }

                if (temp instanceof Float) {
                    if ((float) temp < min) {
                        min = (float) temp;
                    }
                }
                path.remove(path.size() - 1);
            }

            neighbor.incrementTimesVisited();
            Thread.sleep(10);
            grid.repaint();
        }

        return min;
    }

    /**
     * Gets neighbors sorted by g + h values.
     *
     * @param current The current node examined
     * @return The queue of sorted nodes.
     */
    private PriorityQueue<Node> getNewNeighbors(Node current) {
        PriorityQueue<Node> queue = new PriorityQueue<>((node, t1) -> {
            float g = getMovementCost(current, node);
            float g1 = getMovementCost(current, t1);

            float h = heuristic.calculate(node, grid.getGoalNode());
            float h1 = heuristic.calculate(t1, grid.getGoalNode());

            return Float.compare(g + h, g1 + h1);
        });

        queue.addAll(super.getNeighbors(current));

        return queue;
    }

    /**
     * Calculates the cost of going from one node to the next
     *
     * @param one The first node
     * @param two The second node
     * @return If the movement is diagonal, it will return the cost of moving diagonally otherwise straight.
     */
    private float getMovementCost(Node one, Node two) {
        boolean diagonal = Math.abs(one.getX() - two.getX()) != 0 &&
                Math.abs(one.getY() - two.getY()) != 0;

        return diagonal ? Heuristics.DIAGONAL_COST : Heuristics.STRAIGHT_COST;
    }

}