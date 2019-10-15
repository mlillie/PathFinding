package main.pathfinding.impl;

import main.pathfinding.Grid;
import main.pathfinding.Node;
import main.pathfinding.Pathfinder;
import main.pathfinding.impl.astar.AstarHeuristics;

import java.util.*;

/**
 *  This class represents the Dijkstra path finding algorithm.
 *
 *  https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
 *
 * @author Matthew Lillie
 */
public class Dijkstra extends Pathfinder {

    public Dijkstra(Grid grid, boolean diagonalMovement) {
        super(grid, diagonalMovement);
    }

    @Override
    protected List<Node> doInBackground() throws Exception {
        Map<Node, Float> distances = new HashMap<>();

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparing(distances::get));

        distances.put(grid.getStartNode(), 0f);
        open.add(grid.getStartNode());

        while (!open.isEmpty()) {
            Node current = open.poll();

            if (current == grid.getGoalNode()) {
                return constructPath();
            }

            current.incrementTimesVisited();

            for (Node neighbor : getNeighbors(current)) {

                if(neighbor.getTimesVisited() > 0) {
                    neighbor.incrementTimesVisited();
                    continue;
                }

                float tentativeCost = distances.getOrDefault(current, Float.MAX_VALUE) + getMovementCost(current, neighbor);

                if(tentativeCost < distances.getOrDefault(neighbor, Float.MAX_VALUE)) {
                    open.remove(neighbor);
                    distances.put(neighbor, tentativeCost);
                    neighbor.setParent(current);
                    open.add(neighbor);
                    neighbor.incrementTimesVisited();
                }
            }

            Thread.sleep(15);
            grid.repaint();
        }
        return null;
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

        return diagonal ? AstarHeuristics.DIAGONAL_COST : AstarHeuristics.STRAIGHT_COST;
    }

}
