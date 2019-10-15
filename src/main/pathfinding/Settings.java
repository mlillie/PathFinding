package main.pathfinding;

import main.pathfinding.impl.BreadthFirstSearch;
import main.pathfinding.impl.DepthFirstSearch;
import main.pathfinding.impl.Dijkstra;
import main.pathfinding.impl.astar.Astar;
import main.pathfinding.impl.astar.AstarHeuristics;

import javax.swing.*;
import java.awt.*;

/**
 * This class is a JPanel that holds all the different settings used for the application.
 *
 * @author Matthew Lillie
 */
public class Settings extends JPanel {

    /**
     * Data used for the combo boxes
     */
    private static final String[] ALGORITHM_NAMES = {"Depth First Search", "Breadth First Search", "Dijkstra's", "Astar"};
    private static final String[] HEURISTICS = {"Manhattan", "Euclidean", "Diagonal",};

    /**
     * The combo boxes.
     */
    private final JComboBox optionsBox;
    private final JComboBox heuristicsBox;

    /**
     * The slider representing the size of the nodes.
     */
    private final JSlider nodeSizeSlider;

    /**
     * The check box determining if we can move diagonally.
     */
    private final JCheckBox checkDiagonal;

    /**
     * The swing worker thread currently being ran.
     */
    private SwingWorker currentlyRunningFinder;

    /**
     * Insets used for the Grid Bag Constraints
     */
    private static final Insets WEST_INSETS = new Insets(5, 0, 5, 5);
    private static final Insets EAST_INSETS = new Insets(5, 5, 5, 0);

    Settings(Grid grid) {
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Settings: "),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));


        checkDiagonal = new JCheckBox("Diagonal Movement Allowed:");
        checkDiagonal.setSelected(true);
        checkDiagonal.setHorizontalTextPosition(SwingConstants.LEFT);

        this.add(checkDiagonal, createGbc(3, 0));

        optionsBox = new JComboBox<>(ALGORITHM_NAMES);


        heuristicsBox = new JComboBox<>(HEURISTICS);
        heuristicsBox.setVisible(false);


        optionsBox.addActionListener(actionEvent -> {
            if (optionsBox.getSelectedIndex() == 3) {
                heuristicsBox.setSize(optionsBox.getSize());
                heuristicsBox.setVisible(true);
            } else {
                heuristicsBox.setVisible(false);
            }
        });

        this.add(heuristicsBox, createGbc(0, 1));

        this.add(optionsBox, createGbc(0, 0));

        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(actionEvent -> {

            if (currentlyRunningFinder != null) {
                currentlyRunningFinder.cancel(true);
                currentlyRunningFinder = null;
            }

            for (Node[] nodes : grid.getNodes()) {
                for (Node node : nodes) {
                    node.setTimesVisited(0);
                    node.setParent(null);
                }
            }

            grid.setPathFound(null);

            switch (optionsBox.getSelectedIndex()) {
                case 0:
                    currentlyRunningFinder = new DepthFirstSearch(grid, checkDiagonal.isSelected());
                    break;

                case 1:
                    currentlyRunningFinder = new BreadthFirstSearch(grid, checkDiagonal.isSelected());
                    break;

                case 2:
                    currentlyRunningFinder = new Dijkstra(grid, checkDiagonal.isSelected());
                    break;

                case 3:
                    AstarHeuristics heuristic;
                    switch (heuristicsBox.getSelectedIndex()) {
                        case 0:
                            heuristic = AstarHeuristics.MANHATTAN;
                            break;
                        case 1:
                            heuristic = AstarHeuristics.EUCLIDEAN;
                            break;
                        case 2:
                            heuristic = AstarHeuristics.DIAGONAL;
                            break;
                        default:
                            heuristic = AstarHeuristics.MANHATTAN;
                            break;
                    }
                    currentlyRunningFinder = new Astar(grid, checkDiagonal.isSelected(), heuristic);
                    break;

                default:
                    break;

            }

            currentlyRunningFinder.execute();

        });

        this.add(searchButton, createGbc(1, 0));

        JButton resetButton = new JButton("Reset");

        resetButton.addActionListener(actionEvent -> {
            if (currentlyRunningFinder != null) {
                currentlyRunningFinder.cancel(true);
                currentlyRunningFinder = null;
            }

            grid.reset();
        });

        this.add(resetButton, createGbc(1, 1));

        this.add(new JLabel("Node Size:", JLabel.LEFT), createGbc(2, 0));

        nodeSizeSlider = new JSlider(JSlider.HORIZONTAL, 5, 50, grid.getNodeSize());

        nodeSizeSlider.setMajorTickSpacing(5);
        nodeSizeSlider.setMinorTickSpacing(1);
        nodeSizeSlider.setPaintTicks(true);
        nodeSizeSlider.setPaintLabels(true);

        nodeSizeSlider.addChangeListener(changeEvent -> {
            grid.setNodeSize(nodeSizeSlider.getValue());
            grid.repaint();
        });

        this.add(nodeSizeSlider, createGbc(2, 1));
    }

    /**
     * Updates the value of the node size slider.
     * @param nodeSize The new node size.
     */
    public void updateNodeSizeSlider(int nodeSize) {
        this.nodeSizeSlider.setValue(nodeSize);
    }

    /**
     * Creates grid bag constraints given where it should be within the panel.
     * @param x The x position
     * @param y The y position
     * @return The GridBagConstraint
     */
    private GridBagConstraints createGbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        gbc.anchor = x == 0 ? GridBagConstraints.WEST : GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.insets = (x == 0) ? WEST_INSETS : EAST_INSETS;
        gbc.weightx = (x == 0) ? 0.1 : 1.0;
        gbc.weighty = 1.0;
        return gbc;
    }

}
