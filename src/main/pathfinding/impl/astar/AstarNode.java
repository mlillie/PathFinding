package main.pathfinding.impl.astar;

import main.pathfinding.Node;

public class AstarNode {

    private final Node node;

    private float f, g, h;

    public AstarNode(Node node){
        this.node = node;
        this.h = 0;
        this.f = 0;
        this.g = Float.MAX_VALUE;
    }

    public Node getNode() {
        return node;
    }

    public float getF() {
        return f;
    }

    public void setF(float f) {
        this.f = f;
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = g;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }
}
