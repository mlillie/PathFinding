package main.pathfinding;

import main.pathfinding.saving.LoadingSaving;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * main.pathfinding.Main class used for starting the application.
 *
 * @author Matthew Lillie
 */
public class Main {

    private static final String HELP_MESSAGE =
            "Move the starting location (green) to a point by left clicking. \n " +
                    "Move the goal location (red) to a point by right clicking. \n " +
                    "Add blocked locations by left clicking and dragging or using middle mouse button. \n" +
                    "Blocked locations can be removed by pressing middle mouse or right click dragging. \n" +
                    "Blue colored rects will form once the algorithm runs which displays the amount a node has been visited.\n" +
                    "The more a node gets visited (though not necessarily processed), the darker and more filled it will be.\n" +
                    "If using IDA*, you may find better results with diagonals NOT allowed.";

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pathfinding Visualizer");
            // Basic layout and options
            frame.setSize(900, 600);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            // Create grid and settings panels
            Grid grid = new Grid();
            Settings settings = new Settings(grid);

            frame.add(grid, BorderLayout.CENTER);
            frame.add(settings, BorderLayout.SOUTH);

            // Create menu bar.
            JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("File");
            menuBar.add(fileMenu);

            // Buttons
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(actionEvent -> {
                try {
                    LoadingSaving.saveGrid(grid);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            JButton loadButton = new JButton("Load");

            loadButton.addActionListener(actionEvent -> {
                try {
                    LoadingSaving.loadGrid(grid, settings);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            fileMenu.add(saveButton);
            fileMenu.add(loadButton);

            // Help
            JMenu helpMenu = new JMenu("Help");

            JButton helpButton = new JButton("Instructions");

            helpButton.addActionListener(actionEvent -> {
                JOptionPane.showMessageDialog(null, HELP_MESSAGE, "Instructions", JOptionPane.INFORMATION_MESSAGE);
            });

            helpMenu.add(helpButton);
            menuBar.add(helpMenu);

            frame.add(menuBar, BorderLayout.NORTH);

            frame.setVisible(true);
        });
    }
}
