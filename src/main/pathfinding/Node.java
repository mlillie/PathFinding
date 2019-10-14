package main.pathfinding;

/**
 * This class represents a node that is used within the different path finding algorithms.
 *
 * @author Matthew Lillie
 */
public class Node {

    /**
     * Represents the different types of Nodes
     * @author Matthew Lillie
     */
    public enum NodeType {
        START('s'),
        GOAL('g'),
        BLOCKED('x'),
        NORMAL('o');

        /**
         * The save code used for loading/saving
         */
        private final char saveCode;

        /**
         * Creates a new type with a given save code.
         * @param saveCode The save code.
         */
        NodeType(char saveCode) {
            this.saveCode = saveCode;
        }

        /**
         * Gets the save code for the type.
         * @return The save code.
         */
        public char getSaveCode() {
            return saveCode;
        }
    }

    /**
     * The position this node is at.
     */
    private int x, y;

    /**
     * The type of this node.
     */
    private NodeType type;

    /**
     * The amount of times this node has been visited.
     */
    private int timesVisited ;

    /**
     * This nodes parent (for the pathfinding).
     */
    private Node parent;

    /**
     * Constructs a new node with a given position.
     * @param x The x position.
     * @param y The y position.
     */
    public Node(int x, int y) {
        this.timesVisited = 0;
        this.x = x;
        this.y = y;
        this.type = NodeType.NORMAL;
    }

    /**
     * Resets this node.
     */
    public void reset() {
        this.timesVisited = 0;
        this.type = NodeType.NORMAL;
        this.parent = null;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTimesVisited() {
        return timesVisited;
    }

    public void setTimesVisited(int timesVisited) {
        this.timesVisited = timesVisited;
    }

    public void incrementTimesVisited() {
        this.timesVisited++;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }
}
