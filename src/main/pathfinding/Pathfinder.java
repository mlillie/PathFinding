package main.pathfinding;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/**
 * Abstract class to represent a way to find a path.
 * <p>
 * Extends the SwingWorker class so that the path finding algorithm being ran can be executed on a separate thread.
 *
 * @author Matthew Lillie
 */
public abstract class Pathfinder extends SwingWorker<List<Node>, Object> {

    /**
     * The Grid being used for the algorithm.
     */
    protected final Grid grid;

    /**
     * Whether or not we can move diagonally.
     */
    protected final boolean diagonalMovement;

    /**
     * Constructs a new Pathfinder
     *
     * @param grid             The Grid used for the algorithm.
     * @param diagonalMovement If the neighbors found are allowed to be diagonal
     */
    public Pathfinder(Grid grid, boolean diagonalMovement) {
        this.grid = grid;
        this.diagonalMovement = diagonalMovement;
    }

    /**
     * Constructs a path that back traces from the goal Node to the start node.
     *
     * @return A path from the start Node to the goal Node.
     */
    protected List<Node> constructPath() {
        List<Node> path = new ArrayList<>();

        Node current = grid.getGoalNode();

        while (current != null) {
            path.add(0, current);

            if (current == grid.getStartNode()) {
                break;
            }

            current = current.getParent();
        }

        return path;
    }

    /**
     * Calculates the neighbors of the current node in 8 directions. Will exclude blocked nodes.
     *
     * @param current The current node looked at.
     * @return A HashSet of all the valid neighbors.
     */
    protected Set<Node> getNeighbors(Node current) {
        Set<Node> neighbors = new HashSet<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                // Skip the current node
                if (dx == 0 && dy == 0) {
                    continue;
                }

                // Skip diagonals if needed
                if (!diagonalMovement && dx != 0 && dy != 0) {
                    continue;
                }

                int x = current.getX() + dx;
                int y = current.getY() + dy;

                // Skip nodes off the grid
                if (x < 0 || y < 0 || grid.getNodes().length <= x || grid.getNodes()[x].length <= y) {
                    continue;
                }

                // Skip blocked nodes
                if (grid.getNodes()[x][y].getType() == Node.NodeType.BLOCKED) {
                    continue;
                }

                neighbors.add(grid.getNodes()[x][y]);
            }
        }

        return neighbors;
    }

    @Override
    protected void done() {
        try {
            List<Node> path = get();
            if (path == null || path.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Failed to find path.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                // Once the path has been finished, we can immediately draw it on the main GUI
                grid.setPathFound(path);
            }
            // May have allocated a lot memory, have gc attempt to clean it up if possible
            System.gc();
        } catch (InterruptedException | ExecutionException | CancellationException e) {
            //e.printStackTrace();
        }
    }

}
