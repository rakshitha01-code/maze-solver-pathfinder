import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Random;

public class MazeData {

    public static final String[] MAZE_NAMES = {
            "Default Maze", "Maze 2", "Maze 3", "Maze 4",
            "Maze 5", "Maze 6", "Maze 7", "Maze 8"
    };

    // EXACT Default Maze matrix - do not modify.
    // 1 = Wall, 0 = Walkable. 13 x 13 = 169 cells.
    private static final int[][] DEFAULT_MAZE_GRID = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1},
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
        {1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1},
        {1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1},
        {1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1},
        {1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    public static Maze getMaze(int index) {
        if (index == 0) {
            return new Maze(MAZE_NAMES[0], DEFAULT_MAZE_GRID);
        }
        int[][] grid = generatePerfectMaze(2000L + index);
        return new Maze(MAZE_NAMES[index], grid);
    }

    /** Generates a brand new random (but always solvable) 13x13 maze. */
    public static Maze generateRandomMaze() {
        long seed = System.nanoTime();
        int[][] grid = generatePerfectMaze(seed);
        return new Maze("Random Maze", grid);
    }

    /** A blank, all-open 13x13 canvas (with an outer wall) for Create Maze. */
    public static Maze createEmptyMaze() {
        int size = Maze.SIZE;
        int[][] grid = new int[size][size];
        for (int[] row : grid) {
            Arrays.fill(row, 0);
        }
        for (int i = 0; i < size; i++) {
            grid[0][i] = 1;
            grid[size - 1][i] = 1;
            grid[i][0] = 1;
            grid[i][size - 1] = 1;
        }
        Cell start = new Cell(1, 1);
        Cell end = new Cell(size - 2, size - 2);
        grid[start.row][start.col] = 0;
        grid[end.row][end.col] = 0;
        return new Maze("Custom Maze", grid, start, end);
    }

    /**
     * Classic "recursive backtracker" perfect-maze generator.
     */
    private static int[][] generatePerfectMaze(long seed) {
        int size = Maze.SIZE;
        int[][] grid = new int[size][size];
        for (int[] row : grid) {
            Arrays.fill(row, 1);
        }

        Random random = new Random(seed);
        boolean[][] visited = new boolean[size][size];
        Deque<int[]> stack = new ArrayDeque<>();

        grid[0][0] = 0;
        visited[0][0] = true;
        stack.push(new int[]{0, 0});

        int[][] dirs = {{-2, 0}, {2, 0}, {0, -2}, {0, 2}};

        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            List<int[]> options = new ArrayList<>();

            for (int[] d : dirs) {
                int nr = current[0] + d[0];
                int nc = current[1] + d[1];
                if (nr >= 0 && nr < size && nc >= 0 && nc < size && !visited[nr][nc]) {
                    options.add(new int[]{nr, nc, d[0] / 2, d[1] / 2});
                }
            }

            if (options.isEmpty()) {
                stack.pop();
            } else {
                int[] choice = options.get(random.nextInt(options.size()));
                int nr = choice[0];
                int nc = choice[1];
                int wallR = current[0] + choice[2];
                int wallC = current[1] + choice[3];

                grid[wallR][wallC] = 0;
                grid[nr][nc] = 0;
                visited[nr][nc] = true;
                stack.push(new int[]{nr, nc});
            }
        }

        return grid;
    }
}