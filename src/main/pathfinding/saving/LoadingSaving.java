package main.pathfinding.saving;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.pathfinding.Grid;
import main.pathfinding.Node;
import main.pathfinding.Settings;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Helper class that allows for loading and saving of the grid and the nodes within.
 *
 * @author Matthew Lillie
 */
public class LoadingSaving {

    /**
     * The current path to the directory where the file will be saved to.
     */
    private static String currentPathDirectory = null;

    /**
     * Loads a Grid from a JSON file.
     *
     * @param grid The Grid to update with the given file.
     * @throws IOException An exception may be thrown if invalid File.
     */
    public static void loadGrid(Grid grid, Settings settings) throws IOException {
        String directory = getLoadDirectory();

        if (directory == null) {
            return;
        }

        Gson gson = new Gson();

        try(FileReader reader = new FileReader(directory)) {
            GridObject gridObject = gson.fromJson(reader, GridObject.class);

            if (gridObject != null) {
                Node[][] nodes = new Node[gridObject.getGridWidth()][gridObject.getGridHeight()];

                grid.setNodeSize(gridObject.getNodeSize());
                settings.updateNodeSizeSlider(gridObject.getNodeSize());

                for (int x = 0; x < gridObject.getGridWidth(); x++) {
                    for (int y = 0; y < gridObject.getGridHeight(); y++) {
                        nodes[x][y] = new Node(x, y);

                        Node gridNode = nodes[x][y];

                        Character value = gridObject.getGridValues()[x][y];
                        if (value == Node.NodeType.START.getSaveCode()) {
                            gridNode.setType(Node.NodeType.START);
                            grid.setStartNode(gridNode);
                        } else if (value == Node.NodeType.GOAL.getSaveCode()) {
                            gridNode.setType(Node.NodeType.GOAL);
                            grid.setGoalNode(gridNode);
                        } else if (value == Node.NodeType.BLOCKED.getSaveCode()) {
                            gridNode.setType(Node.NodeType.BLOCKED);
                        } else if (value == Node.NodeType.NORMAL.getSaveCode()) {
                            gridNode.setType(Node.NodeType.NORMAL);
                        }

                    }
                }

                grid.setNodes(nodes);
                grid.repaint();
            }
        }
    }

    /**
     * Saves the currently used grid in JSON format.
     *
     * @param grid The Grid to be saved
     * @throws IOException Exception may be thrown if invalid File or directory.
     */
    public static void saveGrid(Grid grid) throws IOException {
        // Find the path
        if (currentPathDirectory == null) {
            String directory = getSaveDirectory();
            if (directory != null) {
                currentPathDirectory = directory;
            } else {
                return;
            }
        }

        Character[][] gridValues = new Character[grid.getNodes().length][grid.getNodes()[0].length];

        // Turn the nodes into a 2d character array
        for (int x = 0; x < grid.getNodes().length; x++) {
            for (int y = 0; y < grid.getNodes()[x].length; y++) {
                Node node = grid.getNodes()[x][y];

                gridValues[x][y] = node.getType().getSaveCode();
            }
        }

        // Create the JSon object
        int nodeSize = grid.getNodeSize();
        int width = gridValues.length;
        int height = gridValues[0].length;

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        GridObject gridObject = new GridObject(width, height, nodeSize, gridValues);
        String jsonGrid = gson.toJson(gridObject);

        // Write the file and close the writer.
        try(FileWriter fileWriter = new FileWriter(currentPathDirectory)) {
            fileWriter.write(jsonGrid);
        }
    }

    /**
     * Attempts to get the directory of where to save the JSON file.
     *
     * @return The directory to save the JSON file.
     */
    private static String getSaveDirectory() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choose a directory to save your file: ");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = jfc.showSaveDialog(null);

        System.out.println(returnValue);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (jfc.getSelectedFile().isDirectory()) {
                return jfc.getSelectedFile().getAbsolutePath() + File.separator + "GridSave.json";
            }
        } else if (returnValue == JFileChooser.CANCEL_OPTION) {
            return null;
        }

        // Invalid file found or something
        JOptionPane.showMessageDialog(null, "Invalid Grid!", "Loading Error", JOptionPane.ERROR_MESSAGE);
        return null;
    }

    /**
     * Attemps to get the load directory of the file the user is attempting to load.
     *
     * @return The directory of the file to load.
     */
    private static String getLoadDirectory() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choose a grid json file to load from: ");
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON FILES", "json");
        jfc.setFileFilter(filter);

        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {

            if (jfc.getSelectedFile().isFile()) {
                if (getExtension(jfc.getSelectedFile()).equals("json")) {
                    return jfc.getSelectedFile().getAbsolutePath();
                }
            }
        }

        return null;
    }

    /**
     * Gets the type of extension that a file has.
     *
     * @param f The file.
     * @return The extension of the file.
     */
    private static String getExtension(File f) {
        String ext = "";
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

}
