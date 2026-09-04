<<<<<<< HEAD
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class MazeGUI extends JFrame {

    private enum Mode { NORMAL, CREATE }
    private enum EditTool { WALL, SET_START, SET_END }

    private Maze currentMaze;
    private int currentMazeIndex = 0; // -1 = Random / Custom maze (not a predefined slot)
    private Mode mode = Mode.NORMAL;
    private EditTool editTool = EditTool.WALL;

    private MazePanel mazePanel;
    private JComboBox<String> mazeSelector;
    private JComboBox<String> algorithmSelector;
    private JButton startButton;
    private JButton resetButton;
    private JButton createMazeButton;
    private JButton randomMazeButton;
    private JButton compareButton;
    private JLabel statusLabel;

    private JPanel editToolBar;
    private JToggleButton wallToolBtn;
    private JToggleButton startToolBtn;
    private JToggleButton endToolBtn;

    private Timer animationTimer;
    private List<AlgorithmResult.Step> currentSteps;
    private int stepIndex;

    private static final int ANIMATION_DELAY_MS = 35;

    public MazeGUI() {
        super("Maze Solver - BFS / DFS / Recursion / Backtracking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        buildUI();
        loadPredefinedMaze(0);
=======
import java.awt.*;
import javax.swing.*;

public class MazeGUI extends JFrame {

    private final Maze maze;

    // Left Control & Panel
    private JComboBox<String> algoLeftSelect;
    private MazePanel panelLeft;
    private JLabel statsLeftLabel;
    private Timer timerLeft;

    // Right Control & Panel
    private JComboBox<String> algoRightSelect;
    private MazePanel panelRight;
    private JLabel statsRightLabel;
    private Timer timerRight;

    // Global Controls
    private JButton startBtn;
    private JButton resetBtn;
    private JSlider speedSlider;

    private static final String[] ALGORITHMS = { "BFS", "DFS", "Backtracking", "Recursion" };

    public MazeGUI() {
        super("Side-by-Side Maze Pathfinding Visualizer");
        this.maze = new Maze("Default Maze", MazeData.DEFAULT_MAZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Controls Panel
        JPanel controlBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlBar.setBackground(new Color(45, 45, 48));

        startBtn = new JButton("Start Comparison");
        resetBtn = new JButton("Reset Grid");

        speedSlider = new JSlider(1, 100, 50);
        JLabel speedLabel = new JLabel("Speed:");
        speedLabel.setForeground(Color.WHITE);

        controlBar.add(startBtn);
        controlBar.add(resetBtn);
        controlBar.add(speedLabel);
        controlBar.add(speedSlider);

        // Center Dual Grid Panel
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.setBackground(new Color(30, 30, 30));

        // Left Panel Unit
        JPanel leftContainer = new JPanel(new BorderLayout(5, 5));
        leftContainer.setBackground(new Color(30, 30, 30));
        
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        leftHeader.setBackground(new Color(45, 45, 48));
        JLabel leftLabel = new JLabel("Left Algo: ");
        leftLabel.setForeground(Color.WHITE);
        algoLeftSelect = new JComboBox<>(ALGORITHMS);
        algoLeftSelect.setSelectedItem("BFS");
        leftHeader.add(leftLabel);
        leftHeader.add(algoLeftSelect);

        panelLeft = new MazePanel();
        panelLeft.setMaze(maze);
        panelLeft.setActiveAlgorithm((String) algoLeftSelect.getSelectedItem());

        statsLeftLabel = new JLabel("Status: Ready | Visited: 0 | Time: 0 ms", SwingConstants.CENTER);
        statsLeftLabel.setForeground(Color.WHITE);

        leftContainer.add(leftHeader, BorderLayout.NORTH);
        leftContainer.add(panelLeft, BorderLayout.CENTER);
        leftContainer.add(statsLeftLabel, BorderLayout.SOUTH);

        // Right Panel Unit
        JPanel rightContainer = new JPanel(new BorderLayout(5, 5));
        rightContainer.setBackground(new Color(30, 30, 30));

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rightHeader.setBackground(new Color(45, 45, 48));
        JLabel rightLabel = new JLabel("Right Algo: ");
        rightLabel.setForeground(Color.WHITE);
        algoRightSelect = new JComboBox<>(ALGORITHMS);
        algoRightSelect.setSelectedItem("DFS");
        rightHeader.add(rightLabel);
        rightHeader.add(algoRightSelect);

        panelRight = new MazePanel();
        panelRight.setMaze(maze);
        panelRight.setActiveAlgorithm((String) algoRightSelect.getSelectedItem());

        statsRightLabel = new JLabel("Status: Ready | Visited: 0 | Time: 0 ms", SwingConstants.CENTER);
        statsRightLabel.setForeground(Color.WHITE);

        rightContainer.add(rightHeader, BorderLayout.NORTH);
        rightContainer.add(panelRight, BorderLayout.CENTER);
        rightContainer.add(statsRightLabel, BorderLayout.SOUTH);

        centerPanel.add(leftContainer);
        centerPanel.add(rightContainer);

        add(controlBar, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        algoLeftSelect.addActionListener(e -> panelLeft.setActiveAlgorithm((String) algoLeftSelect.getSelectedItem()));
        algoRightSelect.addActionListener(e -> panelRight.setActiveAlgorithm((String) algoRightSelect.getSelectedItem()));

        startBtn.addActionListener(e -> startComparison());
        resetBtn.addActionListener(e -> resetGrids());
>>>>>>> ac4187b00f687c7fa041ef50446b19859b6b025c

        pack();
        setLocationRelativeTo(null);
    }

<<<<<<< HEAD
    private void buildUI() {
        getContentPane().setLayout(new BorderLayout(8, 8));
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // ---- Top control bar ----
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));

        mazeSelector = new JComboBox<>(MazeData.MAZE_NAMES);
        mazeSelector.addActionListener(e -> {
            if (mode == Mode.NORMAL) {
                loadPredefinedMaze(mazeSelector.getSelectedIndex());
            }
        });

        algorithmSelector = new JComboBox<>(new String[]{"BFS", "DFS", "Recursion", "Backtracking"});
        algorithmSelector.addActionListener(e ->
                mazePanel.setActiveAlgorithm((String) algorithmSelector.getSelectedItem()));

        startButton = new JButton("Start");
        startButton.addActionListener(this::onStart);

        resetButton = new JButton("Reset");
        resetButton.addActionListener(this::onReset);

        createMazeButton = new JButton("Create Maze");
        createMazeButton.addActionListener(this::onCreateMaze);

        randomMazeButton = new JButton("Random Maze");
        randomMazeButton.addActionListener(this::onRandomMaze);

        compareButton = new JButton("Compare Algorithms");
        compareButton.addActionListener(this::onCompare);

        controls.add(new JLabel("Maze:"));
        controls.add(mazeSelector);
        controls.add(new JLabel("Algorithm:"));
        controls.add(algorithmSelector);
        controls.add(startButton);
        controls.add(resetButton);
        controls.add(createMazeButton);
        controls.add(randomMazeButton);
        controls.add(compareButton);

        // ---- Edit tool bar (visible only while creating a maze) ----
        editToolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        ButtonGroup toolGroup = new ButtonGroup();
        wallToolBtn = new JToggleButton("Toggle Wall", true);
        startToolBtn = new JToggleButton("Set Start");
        endToolBtn = new JToggleButton("Set End");
        toolGroup.add(wallToolBtn);
        toolGroup.add(startToolBtn);
        toolGroup.add(endToolBtn);
        wallToolBtn.addActionListener(e -> editTool = EditTool.WALL);
        startToolBtn.addActionListener(e -> editTool = EditTool.SET_START);
        endToolBtn.addActionListener(e -> editTool = EditTool.SET_END);

        JButton doneEditingBtn = new JButton("Done Creating");
        doneEditingBtn.addActionListener(this::onDoneCreating);

        editToolBar.add(new JLabel("Editing:"));
        editToolBar.add(wallToolBtn);
        editToolBar.add(startToolBtn);
        editToolBar.add(endToolBtn);
        editToolBar.add(doneEditingBtn);
        editToolBar.setVisible(false);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(controls);
        topPanel.add(editToolBar);

        // ---- Maze panel (centered) ----
        mazePanel = new MazePanel();
        mazePanel.setClickListener(this::onCellClicked);

        JPanel mazeWrapper = new JPanel(new GridBagLayout());
        mazeWrapper.add(mazePanel);

        // ---- Status bar ----
        statusLabel = new JLabel("Ready.");
        statusLabel.setBorder(new EmptyBorder(6, 4, 0, 4));

        add(topPanel, BorderLayout.NORTH);
        add(mazeWrapper, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    // ---------------------------------------------------------------
    // Maze loading
    // ---------------------------------------------------------------

    private void loadPredefinedMaze(int index) {
        stopAnimation();
        currentMazeIndex = index;
        currentMaze = MazeData.getMaze(index);
        mazePanel.setActiveAlgorithm((String) algorithmSelector.getSelectedItem());
        mazePanel.setMaze(currentMaze);
        statusLabel.setText("Loaded " + currentMaze.getName() + ". Ready.");
    }

    // ---------------------------------------------------------------
    // Button handlers
    // ---------------------------------------------------------------

    private void onStart(ActionEvent e) {
        if (mode == Mode.CREATE) {
            JOptionPane.showMessageDialog(this,
                    "Finish creating your maze first (click \"Done Creating\").",
                    "Still Editing", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (animationTimer != null && animationTimer.isRunning()) {
            return; // already solving
        }

        clearSolution();
        String algorithm = (String) algorithmSelector.getSelectedItem();
        mazePanel.setActiveAlgorithm(algorithm);

        AlgorithmResult result = runAlgorithm(algorithm, currentMaze);
        animateResult(result);
    }

    private void onReset(ActionEvent e) {
        stopAnimation();
        if (mode == Mode.CREATE) {
            currentMaze = MazeData.createEmptyMaze();
            mazePanel.setMaze(currentMaze);
            statusLabel.setText("Editing cleared. Click cells to add walls.");
            return;
        }
        if (currentMazeIndex >= 0) {
            loadPredefinedMaze(currentMazeIndex); // restore the original, untouched maze
        } else {
            clearSolution(); // Random / Custom maze: keep layout, just clear the run
            statusLabel.setText("Reset. Ready.");
        }
    }

    private void onCreateMaze(ActionEvent e) {
        stopAnimation();
        mode = Mode.CREATE;
        editTool = EditTool.WALL;
        wallToolBtn.setSelected(true);
        mazeSelector.setEnabled(false);
        editToolBar.setVisible(true);

        currentMaze = MazeData.createEmptyMaze();
        currentMazeIndex = -1;
        mazePanel.setMaze(currentMaze);
        pack(); // recalculates window size to smoothly reveal the edit toolbar
        statusLabel.setText("Create Maze: click cells to toggle walls, or choose Set Start / Set End.");
    }

    private void onDoneCreating(ActionEvent e) {
        mode = Mode.NORMAL;
        mazeSelector.setEnabled(true);
        editToolBar.setVisible(false);
        pack(); // recalculates window size to hide edit toolbar
        clearSolution();
        statusLabel.setText("Custom maze ready. Press Start to solve it.");
    }

    private void onRandomMaze(ActionEvent e) {
        stopAnimation();
        mode = Mode.NORMAL;
        mazeSelector.setEnabled(true);
        editToolBar.setVisible(false);
        pack();

        currentMaze = MazeData.generateRandomMaze();
        currentMazeIndex = -1;
        mazePanel.setMaze(currentMaze);
        statusLabel.setText("Random Maze generated. Ready.");
    }

    private void onCellClicked(int row, int col) {
        if (mode != Mode.CREATE) return;

        switch (editTool) {
            case WALL:
                if (currentMaze.isStart(row, col) || currentMaze.isEnd(row, col)) return;
                currentMaze.setWall(row, col, !currentMaze.isWall(row, col));
                break;
            case SET_START:
                if (currentMaze.isEnd(row, col)) return;
                currentMaze.setWall(row, col, false);
                currentMaze.setStart(new Cell(row, col));
                break;
            case SET_END:
                if (currentMaze.isStart(row, col)) return;
                currentMaze.setWall(row, col, false);
                currentMaze.setEnd(new Cell(row, col));
                break;
        }
        mazePanel.repaint();
    }

    private void onCompare(ActionEvent e) {
        if (mode == Mode.CREATE) {
            JOptionPane.showMessageDialog(this,
                    "Finish creating your maze first (click \"Done Creating\").",
                    "Still Editing", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        stopAnimation();
        clearSolution();

        String[] names = {"BFS", "DFS", "Recursion", "Backtracking"};
        AlgorithmResult[] results = new AlgorithmResult[names.length];
        for (int i = 0; i < names.length; i++) {
            results[i] = runAlgorithm(names[i], currentMaze.copy()); // each runs on its own copy
        }

        showComparisonDialog(results);
    }

    // ---------------------------------------------------------------
    // Running & animating algorithms
    // ---------------------------------------------------------------

    private AlgorithmResult runAlgorithm(String algorithm, Maze maze) {
        switch (algorithm) {
            case "BFS": return BFS.solve(maze);
            case "DFS": return DFS.solve(maze);
            case "Recursion": return Recursion.solve(maze);
            case "Backtracking": return Backtracking.solve(maze);
            default: throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        }
    }

    private void animateResult(AlgorithmResult result) {
        currentSteps = result.steps;
        stepIndex = 0;
        statusLabel.setText("Running " + result.algorithmName + "...");

        animationTimer = new Timer(ANIMATION_DELAY_MS, e -> {
            if (stepIndex >= currentSteps.size()) {
                stopAnimation();
                finishAnimation(result);
                return;
            }
            mazePanel.applyStep(currentSteps.get(stepIndex));
            stepIndex++;
        });
        animationTimer.start();
    }

    private void finishAnimation(AlgorithmResult result) {
        if (result.solved) {
            statusLabel.setText(String.format(
                    "%s finished.  Path length: %d   |   Cells visited: %d   |   Time: %.2f ms",
                    result.algorithmName, result.getPathLength(), result.visitedCount,
                    result.getTimeTakenMillis()));
        } else {
            statusLabel.setText(result.algorithmName + ": No path found.");
            JOptionPane.showMessageDialog(this, "No path found.", "Result",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void stopAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
            animationTimer = null;
        }
    }

    private void clearSolution() {
        mazePanel.clearOverlay();
        statusLabel.setText("Ready.");
    }

    // ---------------------------------------------------------------
    // Compare Algorithms dialog
    // ---------------------------------------------------------------

    private void showComparisonDialog(AlgorithmResult[] results) {
        String[] columns = {"Algorithm", "Path Length", "Visited Cells", "Time (ms)", "Status"};
        Object[][] data = new Object[results.length][columns.length];
        for (int i = 0; i < results.length; i++) {
            AlgorithmResult r = results[i];
            data[i][0] = r.algorithmName;
            data[i][1] = r.solved ? r.getPathLength() : "-";
            data[i][2] = r.visitedCount;
            data[i][3] = String.format("%.2f", r.getTimeTakenMillis());
            data[i][4] = r.solved ? "Solved" : "No Path";
        }

        JTable table = new JTable(data, columns);
        table.setEnabled(false);
        table.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(540, 130));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Compare Algorithms - " + currentMaze.getName(),
                JOptionPane.PLAIN_MESSAGE);
=======
    private void startComparison() {
        startBtn.setEnabled(false);
        algoLeftSelect.setEnabled(false);
        algoRightSelect.setEnabled(false);

        panelLeft.clearOverlay();
        panelRight.clearOverlay();

        AlgorithmResult resultLeft = runSelectedAlgorithm((String) algoLeftSelect.getSelectedItem());
        AlgorithmResult resultRight = runSelectedAlgorithm((String) algoRightSelect.getSelectedItem());

        int delay = 105 - speedSlider.getValue();

        timerLeft = new Timer(delay, null);
        final int[] indexLeft = {0};
        long startTimeLeft = System.currentTimeMillis();

        timerLeft.addActionListener(e -> {
            if (indexLeft[0] < resultLeft.steps.size()) {
                panelLeft.applyStep(resultLeft.steps.get(indexLeft[0]++));
                statsLeftLabel.setText(String.format("Status: Running | Visited: %d | Time: %d ms",
                        indexLeft[0], System.currentTimeMillis() - startTimeLeft));
            } else {
                timerLeft.stop();
                statsLeftLabel.setText(String.format("Status: Done | Path: %d | Time: %.2f ms",
                        resultLeft.getPathLength(), resultLeft.getTimeTakenMillis()));
                checkAllFinished();
            }
        });

        timerRight = new Timer(delay, null);
        final int[] indexRight = {0};
        long startTimeRight = System.currentTimeMillis();

        timerRight.addActionListener(e -> {
            if (indexRight[0] < resultRight.steps.size()) {
                panelRight.applyStep(resultRight.steps.get(indexRight[0]++));
                statsRightLabel.setText(String.format("Status: Running | Visited: %d | Time: %d ms",
                        indexRight[0], System.currentTimeMillis() - startTimeRight));
            } else {
                timerRight.stop();
                statsRightLabel.setText(String.format("Status: Done | Path: %d | Time: %.2f ms",
                        resultRight.getPathLength(), resultRight.getTimeTakenMillis()));
                checkAllFinished();
            }
        });

        timerLeft.start();
        timerRight.start();
    }

    private AlgorithmResult runSelectedAlgorithm(String algo) {
        switch (algo) {
            case "BFS":          return BFS.solve(maze);
            case "DFS":          return DFS.solve(maze);
            case "Backtracking": return Backtracking.solve(maze);
            case "Recursion":    return Recursion.solve(maze);
            default:             return BFS.solve(maze);
        }
    }

    private void checkAllFinished() {
        if ((timerLeft == null || !timerLeft.isRunning()) && (timerRight == null || !timerRight.isRunning())) {
            startBtn.setEnabled(true);
            algoLeftSelect.setEnabled(true);
            algoRightSelect.setEnabled(true);
        }
    }

    private void resetGrids() {
        if (timerLeft != null) timerLeft.stop();
        if (timerRight != null) timerRight.stop();

        panelLeft.clearOverlay();
        panelRight.clearOverlay();

        statsLeftLabel.setText("Status: Ready | Visited: 0 | Time: 0 ms");
        statsRightLabel.setText("Status: Ready | Visited: 0 | Time: 0 ms");

        startBtn.setEnabled(true);
        algoLeftSelect.setEnabled(true);
        algoRightSelect.setEnabled(true);
>>>>>>> ac4187b00f687c7fa041ef50446b19859b6b025c
    }
}