package main.pathfinding;

import java.util.*;

/**
 * Generates a random maze using recursive backtracking.
 *
 * @author Matthew Lillie
 */
public class Maze {

    /**
     * The possible directions that we can do in.
     */
    private enum Directions {
        NORTH(0, 2), SOUTH(0, -2), EAST(2,0), WEST(-2,0);

        private final int dx, dy;

        Directions(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public int getDx() {
            return dx;
        }

        public int getDy() {
            return dy;
        }
    }

    /**
     * The random generator
     */
    private static final Random RANDOM = new Random();

    /**
     * Node used to set the end point for the randomly generated maze/
     */
    private static Node lastVisitedNode;

    /**
     * Generates a random maze using the given grid.
     * @param grid The grid to generate the maze on.
     */
    public static void generateMaze(Grid grid) {

        // Reset the grid
        grid.reset();
        grid.setPathFound(null);

        // Initialize the nodes and have them all be blocked
        int width = grid.getWidth() / grid.getNodeSize();
        int height = grid.getHeight() / grid.getNodeSize();

        Node[][] nodes = grid.getNodes();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                nodes[x][y].setType(Node.NodeType.BLOCKED);
            }
        }

        int startX = 1;
        int startY = 1;

        // Generate the maze
        generateHelper(nodes, startX, startY, width, height);

        // Reset visited and update the start, goal, etc
        for(Node[] nodes1 : nodes) {
            for(Node node : nodes1) {
                node.setTimesVisited(0);
            }
        }

        nodes[startX][startY].setType(Node.NodeType.START);
        lastVisitedNode.setType(Node.NodeType.GOAL);

        grid.setStartNode(nodes[startX][startY]);
        grid.setGoalNode(lastVisitedNode);

        // Set and paint
        grid.setNodes(nodes);
        grid.repaint();
    }

    /**
     * Recursive helper for generating the random maze.
     * @param nodes All the nodes on the grid.
     * @param currentX The current x
     * @param currentY The current y
     * @param width The width of the nodes
     * @param height The height of the nodes
     */
    private static void generateHelper(Node[][] nodes, int currentX, int currentY, int width, int height) {
        if(currentX >= 1 && currentY >= 1 && currentX < width - 1 && currentY < height - 1) {
            Node current = nodes[currentX][currentY];
            // Update last visited
            lastVisitedNode = current;
            // Set visited
            current.incrementTimesVisited();

            // While there are unvisited neighbors
            boolean unvisitedNeighbors = hasUnvisitedNeighbors(nodes, current, width, height);

            while(hasUnvisitedNeighbors(nodes, current, width, height)) {
                while(true) {
                    Directions direction = Directions.values()[RANDOM.nextInt(Directions.values().length)];

                    int nextX = currentX + direction.getDx();
                    int nextY = currentY + direction.getDy();
                    if(nextX >= 1 && nextY >= 1 && nextX < width - 1 && nextY < height - 1) {
                        if(nodes[nextX][nextY].getTimesVisited() == 0) {
                            // Connect the current node to the node between
                            nodes[currentX][currentY].setType(Node.NodeType.NORMAL);
                            int betweenX = (nextX - currentX) / 2;
                            int betweenY = (nextY - currentY) / 2;
                            nodes[currentX + betweenX][currentY+betweenY].setType(Node.NodeType.NORMAL);
                            // Recursively go to the next
                            generateHelper(nodes, nextX, nextY, width, height);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks to see if any neighbors have not been visited
     * @param nodes The nodes on the grid
     * @param current The current node
     * @param width The width of the grid
     * @param height He height of the grid
     * @return True if any neighbors have not been visited
     */
    private static boolean hasUnvisitedNeighbors(Node[][] nodes, Node current, int width, int height) {
        int currentX = current.getX();
        int currentY = current.getY();

        //Check north
        if(currentY + 2 < height - 1) {
            if (nodes[currentX][currentY + 2].getTimesVisited() == 0) {
                return true;
            }
        }

        //Check south
        if(currentY - 2 >= 1) {
            if (nodes[currentX][currentY - 2].getTimesVisited() == 0) {
                return true;
            }
        }

        //Check east
        if(currentX + 2 < width - 1) {
            if (nodes[currentX + 2][currentY].getTimesVisited() == 0) {
                return true;
            }
        }

        //Check west
        if(currentX - 2 >= 1) {
            if (nodes[currentX - 2][currentY].getTimesVisited() == 0) {
                return true;
            }
        }

        return false;
    }

}
