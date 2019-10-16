package main.pathfinding;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;

/**
 * A grid being painted onto a JPanel. The grid consists of nodes where we can specify the size of the node.
 * <p>
 * This panel handles all the clicking and dragging associated with changes the start node, end node, and blocked nodes.
 *
 * @author Matthew Lillie
 */
public class Grid extends JPanel implements MouseListener, MouseMotionListener {

    /**
     * The size (width and height) of the individual nodes
     */
    private int nodeSize = 20;

    /**
     * The data used for storing the nodes as well as the start and end point
     */
    private Node[][] nodes;
    private Node startNode, goalNode;

    /**
     * Represents the date necessary for dragging
     */
    private boolean mouseDragged = false;
    private int dragStartX = -1, dragStartY = -1, draggedButton = -1;

    /**
     * The list of nodes that represent the path that has been found.
     */
    private List<Node> pathFound;

    /**
     * Constructor for this class, where we attach the mouse listeners
     */
    Grid() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Create the nodes, must be done here because width and height of this component are not set until after initialization
        if (nodes == null) {
            nodes = new Node[getWidth() / nodeSize][getHeight() / nodeSize];

            for (int x = 0; x < getWidth() / nodeSize; x++) {
                for (int y = 0; y < getHeight() / nodeSize; y++) {
                    nodes[x][y] = new Node(x, y);
                }
            }

            startNode = nodes[0][0];
            startNode.setType(Node.NodeType.START);
            goalNode = nodes[nodes.length-1][nodes[0].length-1];
            goalNode.setType(Node.NodeType.GOAL);
        }

        Graphics2D graphics = (Graphics2D) g;
        // Enable anti aliasing and prefer quality rendering
        //graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

        // Draw all the nodes
        for (int x = 0; x < nodes.length; x++) {
            for (int y = 0; y < nodes[x].length; y++) {
                Node node = nodes[x][y];

                int realX = (x * nodeSize) % getWidth();
                int realY = (y * nodeSize) % getHeight();

                if (node.getTimesVisited() > 0) {
                    Color color = Color.MAGENTA;

                    for (int i = 1; i < node.getTimesVisited() / 3; i++) {
                        color = color.darker();
                    }

                    graphics.setColor(color);

                    if (node.getTimesVisited() == 1) {
                        graphics.drawOval(realX + nodeSize / 2, realY + nodeSize / 2, (int) (nodeSize / 2), (int) (nodeSize / 2));
                    } else {
                        graphics.fillOval(realX + nodeSize / 2, realY + nodeSize / 2, (int) (nodeSize / 2), (int) (nodeSize / 2));
                    }

                    graphics.setColor(Color.LIGHT_GRAY);
                    graphics.drawRect(realX, realY, nodeSize, nodeSize);
                }
                switch(node.getType()) {
                    case START:
                        graphics.setColor(Color.GREEN);
                        graphics.fillRect(realX, realY, nodeSize, nodeSize);
                        break;
                    case GOAL:
                        graphics.setColor(Color.RED);
                        graphics.fillRect(realX, realY, nodeSize, nodeSize);
                        break;
                    case BLOCKED:
                        graphics.setColor(Color.GRAY);
                        graphics.fillRect(realX, realY, nodeSize, nodeSize);
                        break;
                    case NORMAL:
                        graphics.setColor(Color.LIGHT_GRAY);
                        graphics.drawRect(realX, realY, nodeSize, nodeSize);
                        break;
                }

            }
        }


        //Draw the path if the path has been found
        if (pathFound != null) {
            for (int i = 0; i < pathFound.size() - 1; i++) {
                if(pathFound.get(i) == null || pathFound.get(i + 1) == null) break;

                Node current = pathFound.get(i);
                Node next = pathFound.get(i + 1);

                int realX = (current.getX() * nodeSize) % getWidth();
                int realY = (current.getY() * nodeSize) % getHeight();

                int realNextX = (next.getX() * nodeSize) % getWidth();
                int realNextY = (next.getY() * nodeSize) % getHeight();

                graphics.setColor(Color.BLACK);

                graphics.fillOval(realX + nodeSize / 2, realY + nodeSize / 2, nodeSize / 3, nodeSize / 3);
                graphics.fillOval(realNextX + nodeSize / 2, realNextY + nodeSize / 2, nodeSize / 3, nodeSize / 3);

                graphics.drawLine(realX + nodeSize / 2, realY + nodeSize / 2, realNextX + nodeSize / 2, realNextY + nodeSize / 2);
            }
        }
    }

    /**
     * Sets the path found.
     * @param pathFound The found path to display.
     */
    public void setPathFound(List<Node> pathFound) {
        this.pathFound = pathFound;
        repaint();
    }

    /**
     * Resets all the nodes within the grid
     */
    public void reset() {
        for (Node[] node : nodes) {
            for (Node value : node) {
                value.reset();
            }
        }

        startNode = nodes[0][0];
        startNode.setType(Node.NodeType.START);
        goalNode = nodes[nodes.length-1][nodes[0].length-1];
        goalNode.setType(Node.NodeType.GOAL);

        pathFound = null;

        repaint();
    }

    /**
     * Gets the size of nodes on the grid.
     * @return the size of the nodes.
     */
    public int getNodeSize() {
        return nodeSize;
    }

    /**
     * Sets the size of the nodes and updates the grid.
     * @param nodeSize The new size of the nodes.
     */
    public void setNodeSize(int nodeSize) {
        this.nodeSize = nodeSize;
        this.pathFound = null;

        nodes = new Node[getWidth() / nodeSize][getHeight() / nodeSize];

        for (int x = 0; x < getWidth() / nodeSize; x++) {
            for (int y = 0; y < getHeight() / nodeSize; y++) {
                nodes[x][y] = new Node(x, y);
            }
        }

        startNode = nodes[0][0];
        startNode.setType(Node.NodeType.START);
        goalNode = nodes[getWidth() / nodeSize - 1][getHeight() / nodeSize - 1];
        goalNode.setType(Node.NodeType.GOAL);
    }

    /**
     * Sets the nodes for this grid.
     * @param nodes The new nodes for this grid.
     */
    public void setNodes(Node[][] nodes) {
        this.nodes = nodes;
    }

    /**
     * Gets all the nodes within this Grid.
     *
     * @return The nodes within the Grid.
     */
    public Node[][] getNodes() {
        return nodes;
    }

    /**
     * Gets the start node for this Grid.
     *
     * @return The start node for this Grid.
     */
    public Node getStartNode() {
        return startNode;
    }

    /**
     * Gets the end node for this Grid.
     *
     * @return The end node for this Grid.
     */
    public Node getGoalNode() {
        return goalNode;
    }

    /**
     * Sets the new start node.
     * @param startNode The new start node.
     */
    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    /**
     * Sets the new end node.
     * @param goalNode The new end node.
     */
    public void setGoalNode(Node goalNode) {
        this.goalNode = goalNode;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        // Convert the position to fit the 2d array
        int x = mouseEvent.getX() / nodeSize;
        int y = mouseEvent.getY() / nodeSize;

        // Ensure it fits
        if (x >= nodes.length || y >= nodes[x].length) {
            return;
        }

        // Left click changes start node
        if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
            if (nodes[x][y] != startNode && nodes[x][y] != goalNode) {
                startNode.setType(Node.NodeType.NORMAL);
                startNode = nodes[x][y];
                startNode.setType(Node.NodeType.START);
                repaint();
            }
            // Middle click changes whether or not the hovered node is blocked
        } else if (mouseEvent.getButton() == MouseEvent.BUTTON2) {
            if (nodes[x][y] != startNode && nodes[x][y] != goalNode) {
                if(nodes[x][y].getType() == Node.NodeType.BLOCKED) {
                    nodes[x][y].setType(Node.NodeType.NORMAL);
                } else {
                    nodes[x][y].setType(Node.NodeType.BLOCKED);
                }
                repaint();
            }
            // Right click changes the end node
        } else if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
            if (nodes[x][y] != goalNode && nodes[x][y] != startNode) {
                goalNode.setType(Node.NodeType.NORMAL);
                goalNode = nodes[x][y];
                goalNode.setType(Node.NodeType.GOAL);
                repaint();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (mouseDragged) {
            int currentX = event.getX();
            int currentY = event.getY();

            int startX = Math.min(currentX, dragStartX) / nodeSize;
            int startY = Math.min(currentY, dragStartY) / nodeSize;

            int endX = Math.max(currentX, dragStartX) / nodeSize;
            int endY = Math.max(currentY, dragStartY) / nodeSize;

            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y <= endY; y++) {

                    if (x < 0 || y < 0 || x >= nodes.length || y >= nodes[x].length) {
                        break;
                    }

                    if (nodes[x][y] == startNode || nodes[x][y] == goalNode) {
                        continue;
                    }

                    if(draggedButton == MouseEvent.BUTTON1) {
                        nodes[x][y].setType(Node.NodeType.BLOCKED);
                    } else {
                        nodes[x][y].setType(Node.NodeType.NORMAL);
                    }
                }
            }

            repaint();
        }

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        mouseDragged = true;
        dragStartX = mouseEvent.getX();
        dragStartY = mouseEvent.getY();
        draggedButton = mouseEvent.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        mouseDragged = false;
        draggedButton = dragStartX = dragStartY = -1;
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

}

