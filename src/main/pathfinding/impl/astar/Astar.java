package main.pathfinding.impl.astar;

import main.pathfinding.Grid;
import main.pathfinding.Node;
import main.pathfinding.Pathfinder;

import java.util.*;

/**
 * This class represents the A* path finding algorithm.
 *
 * https://en.wikipedia.org/wiki/A*_search_algorithm
 *
 * @author Matthew Lillie
 */
public class Astar extends Pathfinder {

    /**
     * A map that connects a regular grid node to its Astar variant.
     */
    private final Map<Node, AstarNode> createdNodes = new HashMap<>();

    /**
     * The heuristic being used for the algorithm.
     */
    private final AstarHeuristics heuristic;

    /**
     * Constructs a new path A* finding algorithm.
     * @param grid The Grid being used for this algorithm.
     * @param diagonalMovement Whether or not we can move diagonally.
     * @param heuristic The heuristic used for the algorithm.
     */
    public Astar(Grid grid, boolean diagonalMovement, AstarHeuristics heuristic) {
        super(grid, diagonalMovement);
        this.heuristic = heuristic;
    }

    @Override
    protected List<Node> doInBackground() throws Exception {
        // Turn the regular nodes into the Astar variants
        AstarNode start = createdNodes.computeIfAbsent(grid.getStartNode(), AstarNode::new);

        // Priority queue used to represent the open set (similar to a min heap)
        PriorityQueue<AstarNode> open = new PriorityQueue<>((node, node1) -> Float.compare(node.getF(), node1.getF()));

        // Set the standard values for the starting node and add it the open set
        start.setH(heuristic.calculate(start.getNode(), grid.getGoalNode()));
        start.setG(0);
        start.setF(start.getG() + start.getH());
        open.add(start);

        while(!open.isEmpty()) {

            AstarNode current = open.poll();
            // Found our goal, break and create the path
            if(current.getNode() == grid.getGoalNode()) {
                return constructPath();
            }

            current.getNode().incrementTimesVisited();

            // Go through our current neighbors and run the algorithm on it
            for (Node nodeNeighbor : getNeighbors(current.getNode())) {

                if(nodeNeighbor.getTimesVisited() > 0) {
                    nodeNeighbor.incrementTimesVisited();
                    continue;
                }

                AstarNode neighbor = createdNodes.computeIfAbsent(nodeNeighbor, AstarNode::new);

                float tentativeG = current.getG() + getMovementCost(current, neighbor);

                if(open.contains(neighbor) && tentativeG >= neighbor.getG()) {
                    continue;
                }

                if(tentativeG < neighbor.getG()) {
                    open.remove(neighbor);
                    neighbor.setG(tentativeG);
                    neighbor.setH(heuristic.calculate(neighbor.getNode(), grid.getGoalNode()));
                    neighbor.setF(neighbor.getG() + neighbor.getH());
                    neighbor.getNode().setParent(current.getNode());
                    open.add(neighbor);
                }

                nodeNeighbor.incrementTimesVisited();

                Thread.sleep(10);
                grid.repaint();
             }

        }

        // No path found, return null
        return null;
    }

    /**
     * Calculates the cost of going from one node to the next
     * @param one The first AstarNode
     * @param two The second AstarNode
     * @return If the movement is diagonal, it will return the cost of moving diagonally otherwise straight.
     */
    private float getMovementCost(AstarNode one, AstarNode two) {
        boolean diagonal = Math.abs(one.getNode().getX() - two.getNode().getX()) != 0 &&
                Math.abs(one.getNode().getY() - two.getNode().getY()) != 0;

        return diagonal ? AstarHeuristics.DIAGONAL_COST : AstarHeuristics.STRAIGHT_COST;
    }

}
