import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class MazePanel extends JPanel {

    public static final int CELL_SIZE = 40;

    public enum RenderState { NONE, VISITED, PATH }

    /** Functional callback fired when the user clicks a cell. */
    public interface CellClickListener {
        void onCellClicked(int row, int col);
    }

    private static final Color WALL_COLOR = new Color(34, 32, 38);
    private static final Color EMPTY_COLOR = new Color(247, 244, 237);
    private static final Color START_COLOR = new Color(115, 213, 180);
    private static final Color END_COLOR = new Color(244, 196, 66);
    private static final Color GRID_LINE_COLOR = new Color(214, 209, 199);

    private static final Color BFS_VISIT_COLOR = new Color(126, 200, 232);
    private static final Color BFS_PATH_COLOR = new Color(22, 140, 120);

    private static final Color TRAIL_VISIT_COLOR = new Color(242, 153, 74);
    private static final Color TRAIL_PATH_COLOR = new Color(214, 92, 30);

    private Maze maze;
    private final RenderState[][] renderState = new RenderState[Maze.SIZE][Maze.SIZE];
    private String activeAlgorithm = "BFS";
    private CellClickListener clickListener;

    public MazePanel() {
        setPreferredSize(new Dimension(Maze.SIZE * CELL_SIZE, Maze.SIZE * CELL_SIZE));
        setBackground(EMPTY_COLOR);
        clearOverlay();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / CELL_SIZE;
                int row = e.getY() / CELL_SIZE;
                if (maze != null && maze.isInBounds(row, col) && clickListener != null) {
                    clickListener.onCellClicked(row, col);
                }
            }
        });
    }

    public void setClickListener(CellClickListener listener) {
        this.clickListener = listener;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        clearOverlay();
    }

    public void setActiveAlgorithm(String algorithm) {
        this.activeAlgorithm = algorithm;
        repaint();
    }

    public void clearOverlay() {
        for (int r = 0; r < Maze.SIZE; r++) {
            for (int c = 0; c < Maze.SIZE; c++) {
                renderState[r][c] = RenderState.NONE;
            }
        }
        repaint();
    }

    /** Applies one animation Step produced by an algorithm and repaints. */
    public void applyStep(AlgorithmResult.Step step) {
        int r = step.cell.row;
        int c = step.cell.col;
        switch (step.type) {
            case VISIT:
                renderState[r][c] = RenderState.VISITED;
                break;
            case BACKTRACK:
                renderState[r][c] = RenderState.NONE;
                break;
            case PATH:
                renderState[r][c] = RenderState.PATH;
                break;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (maze == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        boolean isBFS = "BFS".equals(activeAlgorithm);

        for (int r = 0; r < Maze.SIZE; r++) {
            for (int c = 0; c < Maze.SIZE; c++) {
                Color color;
                if (maze.isWall(r, c)) {
                    color = WALL_COLOR;
                } else if (maze.isStart(r, c)) {
                    color = START_COLOR;
                } else if (maze.isEnd(r, c)) {
                    color = END_COLOR;
                } else {
                    RenderState state = renderState[r][c];
                    if (state == RenderState.PATH) {
                        color = isBFS ? BFS_PATH_COLOR : TRAIL_PATH_COLOR;
                    } else if (state == RenderState.VISITED) {
                        color = isBFS ? BFS_VISIT_COLOR : TRAIL_VISIT_COLOR;
                    } else {
                        color = EMPTY_COLOR;
                    }
                }

                g2.setColor(color);
                g2.fillRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g2.setColor(GRID_LINE_COLOR);
                g2.drawRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }
}