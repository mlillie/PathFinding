package main.pathfinding;

import main.pathfinding.impl.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * This class is a JPanel that holds all the different settings used for the application.
 *
 * @author Matthew Lillie
 */
public class Settings extends JPanel {

    /**
     * Data used for the combo boxes
     */
    private static final String[] ALGORITHM_NAMES = {
            "Depth First Search", "Breadth First Search",
            "Dijkstra's", "Astar", "Beam Search", "IDAStar"};

    private static final String[] HEURISTICS = {"Manhattan", "Euclidean", "Octile", "Chebyshev"};

    /**
     * The combo boxes.
     */
    private final JComboBox optionsBox;
    private final JComboBox heuristicsBox;

    /**
     * The beam width used for the beam search algorithm
     */
    private final JTextField beamWidthField;

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
    private static final Insets WEST_INSETS = new Insets(0, 0, 0, 2);
    private static final Insets EAST_INSETS = new Insets(0, 2, 0, 0);

    Settings(Grid grid) {
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Settings: "),
                BorderFactory.createEmptyBorder(3, 3, 1, 3)));

        // Beam width
        beamWidthField = new JTextField("4");
        this.add(beamWidthField, createGbc(0,2));
        beamWidthField.setVisible(false);

        // Diagonal checks
        checkDiagonal = new JCheckBox("Diagonal Movement Allowed:");
        checkDiagonal.setSelected(true);
        checkDiagonal.setHorizontalTextPosition(SwingConstants.LEFT);
        checkDiagonal.setToolTipText("Whether or not the algorithm can check for diagonal moves. Best if off for mazes.");
        this.add(checkDiagonal, createGbc(3, 0));

        // Combo boxes
        optionsBox = new JComboBox<>(ALGORITHM_NAMES);
        heuristicsBox = new JComboBox<>(HEURISTICS);
        heuristicsBox.setVisible(false);

        optionsBox.addActionListener(actionEvent -> {
            if (optionsBox.getSelectedIndex() == 3 || optionsBox.getSelectedIndex() == 4 || optionsBox.getSelectedIndex() == 5) {
                heuristicsBox.setVisible(true);
                //beamWidthField.setVisible(optionsBox.getSelectedIndex() == 4);
            } else {
                heuristicsBox.setVisible(false);
                beamWidthField.setVisible(false);
            }
        });

        this.add(heuristicsBox, createGbc(0, 1));

        this.add(optionsBox, createGbc(0, 0));

        // Buttons
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
                case 4:
                case 5:
                    Heuristics heuristic;
                    switch (heuristicsBox.getSelectedIndex()) {
                        case 0:
                            heuristic = Heuristics.MANHATTAN;
                            break;
                        case 1:
                            heuristic = Heuristics.EUCLIDEAN;
                            break;
                        case 2:
                            heuristic = Heuristics.OCTILE;
                            break;
                        case 3:
                            heuristic = Heuristics.CHEBYSHEV;
                            break;
                        default:
                            heuristic = Heuristics.MANHATTAN;
                            break;
                    }
                    currentlyRunningFinder =
                            optionsBox.getSelectedIndex() == 3 ? new Astar(grid, checkDiagonal.isSelected(), heuristic) :
                                    optionsBox.getSelectedIndex() == 4 ? new BeamSearch(grid, checkDiagonal.isSelected(), heuristic)
                                            : new IDAStarSearch(grid, checkDiagonal.isSelected(), heuristic);
                    break;

                default:
                    break;

            }

            // Start the algorithm
            if(currentlyRunningFinder != null) {
                currentlyRunningFinder.execute();
            }
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

        JButton maze = new JButton("Generate Maze");

        maze.addActionListener(actionEvent -> {
            // Stop any algorithm running
            if (currentlyRunningFinder != null) {
                currentlyRunningFinder.cancel(true);
                currentlyRunningFinder = null;
            }

            // Reset the grid
            grid.reset();
            grid.setPathFound(null);

            // Generate the maze
            Maze.generateMaze(grid);
        });

        this.add(maze, createGbc(1, 2));

        // Slider for the node size
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
    };

    /**
     * Updates the value of the node size slider.
     *
     * @param nodeSize The new node size.
     */
    public void updateNodeSizeSlider(int nodeSize) {
        this.nodeSizeSlider.setValue(nodeSize);
    }

    /**
     * Creates grid bag constraints given where it should be within the panel.
     *
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
        gbc.weighty = 0.75;
        return gbc;
    }

}
