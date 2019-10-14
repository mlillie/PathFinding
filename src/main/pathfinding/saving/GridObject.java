package main.pathfinding.saving;

/**
 *
 * @author Matthew Lillie
 */
public class GridObject {

    /**
     * The data that represents the size of the grid and the node.
     */
    private int gridWidth, gridHeight, nodeSize;

    /**
     * The node types being used
     */
    private Character[][] gridValues;

    public GridObject(int gridWidth, int gridHeight, int nodeSize, Character[][] gridValues) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.nodeSize = nodeSize;
        this.gridValues = gridValues;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public int getNodeSize() {
        return nodeSize;
    }

    public Character[][] getGridValues() {
        return gridValues;
    }
}
