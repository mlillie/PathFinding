package main.pathfinding.impl;

import main.pathfinding.Grid;
import main.pathfinding.Node;
import main.pathfinding.Pathfinder;

import java.util.List;
import java.util.Stack;

/**
 *  This class represents the Depth First Search path finding algorithm.
 *
 *  https://en.wikipedia.org/wiki/Depth-first_search
 *
 * @author Matthew Lillie
 */
public class DepthFirstSearch extends Pathfinder {

    public DepthFirstSearch(Grid grid, boolean diagonalMovement) {
        super(grid, diagonalMovement);
    }

    @Override
    protected List<Node> doInBackground() throws Exception {
        Stack<Node> open = new Stack<>();
        open.add(grid.getStartNode());

        grid.getStartNode().incrementTimesVisited();

        while(!open.isEmpty()) {
            Node current = open.pop();

            if(current == grid.getGoalNode()) {
                return constructPath();
            }

            for (Node neighbor : getNeighbors(current)) {

                if(neighbor.getTimesVisited() == 0) {
                    neighbor.setParent(current);
                    open.add(neighbor);
                }

                neighbor.incrementTimesVisited();

            }

            Thread.sleep(15);
            grid.repaint();
        }

        return null;
    }
}
