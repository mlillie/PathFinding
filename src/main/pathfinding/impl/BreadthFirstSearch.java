package main.pathfinding.impl;

import main.pathfinding.Grid;
import main.pathfinding.Node;
import main.pathfinding.Pathfinder;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This class represents the Breadth First Search path finding algorithm.
 * <p>
 * https://en.wikipedia.org/wiki/Breadth-first_search
 *
 * @author Matthew Lillie
 */
public class BreadthFirstSearch extends Pathfinder {

    public BreadthFirstSearch(Grid grid, boolean diagonalMovement) {
        super(grid, diagonalMovement);
    }

    @Override
    protected List<Node> doInBackground() throws Exception {
        Queue<Node> open = new LinkedList<>();
        open.add(grid.getStartNode());

        grid.getStartNode().incrementTimesVisited();

        while (!open.isEmpty()) {
            Node current = open.poll();

            if (current == grid.getGoalNode()) {
                return constructPath();
            }

            for (Node neighbor : getNeighbors(current)) {
                if (neighbor.getTimesVisited() == 0) {
                    neighbor.setParent(current);
                    open.offer(neighbor);
                }

                neighbor.incrementTimesVisited();

            }

            Thread.sleep(15);
            grid.repaint();
        }

        return null;
    }
}
