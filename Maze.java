
public class Maze {

    public static final int SIZE = 13;

    private int[][] grid;
    private Cell start;
    private Cell end;
    private String name;

    /** Build a maze from a grid, auto-detecting Start/End. */
    public Maze(String name, int[][] sourceGrid) {
        this.name = name;
        this.grid = deepCopy(sourceGrid);
        this.start = findFirstOpenCell();
        this.end = findLastOpenCell();
    }

    /** Build a maze from a grid with an explicit Start/End. */
    public Maze(String name, int[][] sourceGrid, Cell start, Cell end) {
        this.name = name;
        this.grid = deepCopy(sourceGrid);
        this.start = start;
        this.end = end;
    }

    private static int[][] deepCopy(int[][] source) {
        int[][] copy = new int[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            copy[r] = source[r].clone();
        }
        return copy;
    }

    private Cell findFirstOpenCell() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (grid[r][c] == 0) return new Cell(r, c);
            }
        }
        return new Cell(0, 0);
    }

    private Cell findLastOpenCell() {
        for (int r = SIZE - 1; r >= 0; r--) {
            for (int c = SIZE - 1; c >= 0; c--) {
                if (grid[r][c] == 0) return new Cell(r, c);
            }
        }
        return new Cell(SIZE - 1, SIZE - 1);
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] newGrid) {
        this.grid = deepCopy(newGrid);
    }

    public Cell getStart() {
        return start;
    }

    public void setStart(Cell start) {
        this.start = start;
    }

    public Cell getEnd() {
        return end;
    }

    public void setEnd(Cell end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInBounds(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    public boolean isWall(int r, int c) {
        return isInBounds(r, c) && grid[r][c] == 1;
    }

    public boolean isWalkable(int r, int c) {
        return isInBounds(r, c) && grid[r][c] == 0;
    }

    public void setWall(int r, int c, boolean wall) {
        if (isInBounds(r, c)) {
            grid[r][c] = wall ? 1 : 0;
        }
    }

    public boolean isStart(int r, int c) {
        return start != null && start.row == r && start.col == c;
    }

    public boolean isEnd(int r, int c) {
        return end != null && end.row == r && end.col == c;
    }

    /** Returns an independent copy of this maze (grid + start + end). */
    public Maze copy() {
        return new Maze(name, grid, start, end);
    }
}
