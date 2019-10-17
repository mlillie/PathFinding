package main.pathfinding.impl;

import main.pathfinding.Grid;
import main.pathfinding.Node;
import main.pathfinding.Pathfinder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * This class represents the A* path finding algorithm.
 * <p>
 * https://en.wikipedia.org/wiki/A*_search_algorithm
 *
 * @author Matthew Lillie
 */
public class Astar extends Pathfinder {

    /**
     * The heuristic being used for the algorithm.
     */
    private final Heuristics heuristic;

    /**
     * Constructs a new path A* finding algorithm.
     *
     * @param grid             The Grid being used for this algorithm.
     * @param diagonalMovement Whether or not we can move diagonally.
     * @param heuristic        The heuristic used for the algorithm.
     */
    public Astar(Grid grid, boolean diagonalMovement, Heuristics heuristic) {
        super(grid, diagonalMovement);
        this.heuristic = heuristic;
    }

    @Override
    protected List<Node> doInBackground() throws Exception {
        Map<Node, Float> gValues = new HashMap<>();
        Map<Node, Float> fValues = new HashMap<>();

        // Priority queue used to represent the open set (similar to a min heap)
        PriorityQueue<Node> open = new PriorityQueue<>((node, node1) ->
                Float.compare(fValues.getOrDefault(node, Float.MAX_VALUE), fValues.getOrDefault(node1, Float.MAX_VALUE))
        );

        // Set the standard values for the starting node and add it the open set
        gValues.put(grid.getStartNode(), 0f);
        fValues.put(grid.getStartNode(), heuristic.calculate(grid.getStartNode(), grid.getGoalNode()) + 0f);
        open.add(grid.getStartNode());

        while (!open.isEmpty()) {

            Node current = open.poll();
            // Found our goal, break and create the path
            if (current == grid.getGoalNode()) {
                return constructPath();
            }

            // "Close" the current
            current.incrementTimesVisited();

            // Go through our current neighbors and run the algorithm on it
            for (Node neighbor : getNeighbors(current)) {

                if (neighbor.getTimesVisited() > 0) {
                    // Done purely for visualization
                    neighbor.incrementTimesVisited();
                    continue;
                }

                float tentativeG = gValues.getOrDefault(current, Float.MAX_VALUE) + getMovementCost(current, neighbor);

                if (open.contains(neighbor) && tentativeG >= gValues.getOrDefault(neighbor, Float.MAX_VALUE)) {
                    continue;
                }

                if (tentativeG < gValues.getOrDefault(neighbor, Float.MAX_VALUE)) {
                    open.remove(neighbor);
                    gValues.put(neighbor, tentativeG);
                    fValues.put(neighbor, heuristic.calculate(neighbor, grid.getGoalNode()) + tentativeG);
                    neighbor.setParent(current);
                    open.add(neighbor);

                    // Done purely for visualization
                    neighbor.incrementTimesVisited();
                }
            }

            Thread.sleep(15);
            grid.repaint();

        }

        // No path found, return null
        return null;
    }

    /**
     * Calculates the cost of going from one node to the next
     *
     * @param one The first Node
     * @param two The second Node
     * @return If the movement is diagonal, it will return the cost of moving diagonally otherwise straight.
     */
    private float getMovementCost(Node one, Node two) {
        boolean diagonal = Math.abs(one.getX() - two.getX()) != 0 &&
                Math.abs(one.getY() - two.getY()) != 0;

        return diagonal ? Heuristics.DIAGONAL_COST : Heuristics.STRAIGHT_COST;
    }

}
