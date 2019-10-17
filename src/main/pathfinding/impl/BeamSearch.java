package main.pathfinding.impl;

import main.pathfinding.Grid;
import main.pathfinding.Node;
import main.pathfinding.Pathfinder;

import java.util.*;

/**
 * Implementation of the BeamSearch algorithm
 * <p>
 * https://en.wikipedia.org/wiki/Beam_search
 *
 * @author Matthew Lillie
 */
public class BeamSearch extends Pathfinder {

    /**
     * The heuristic being used for the algorithm.
     */
    private final Heuristics heuristic;

    /**
     * Constructs a new Pathfinder
     *
     * @param grid             The Grid used for the algorithm.
     * @param diagonalMovement If the neighbors found are allowed to be diagonal
     * @param heuristic        The heuristic used for the algorithm.
     */
    public BeamSearch(Grid grid, boolean diagonalMovement, Heuristics heuristic) {
        super(grid, diagonalMovement);
        this.heuristic = heuristic;
    }

    @Override
    protected List<Node> doInBackground() throws Exception {
        Set<Node> beam = new HashSet<>();
        PriorityQueue<Node> set = new PriorityQueue<>(Comparator.comparing(node ->
                heuristic.calculate(node, grid.getGoalNode())));

        int beamWidth = diagonalMovement ? 8 : 4;

        beam.add(grid.getStartNode());

        while (!beam.isEmpty()) {

            for (Node node : beam) {
                for (Node neighbor : getNeighbors(node)) {
                    if (neighbor.getTimesVisited() == 0) {
                        neighbor.setParent(node);
                    }

                    if (node == grid.getGoalNode()) {
                        return constructPath();
                    }

                    set.add(neighbor);
                }
            }

            beam.clear();

            // Only allow beamWidth items into the true open (beam) set
            while (!set.isEmpty() && beamWidth > beam.size()) {
                Node state = set.poll();

                if (state == null) {
                    continue;
                }

                if (state.getTimesVisited() == 0) {
                    beam.add(state);
                }

                state.incrementTimesVisited();
            }

            // Clear and paint
            set.clear();
            Thread.sleep(15);
            grid.repaint();
        }

        return null;
    }
}
