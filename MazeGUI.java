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

        pack();
        setLocationRelativeTo(null);
    }

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
    }
}