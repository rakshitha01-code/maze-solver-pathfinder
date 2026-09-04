import java.util.ArrayList;
import java.util.List;


public class Backtracking {

    private static final int[][] DIRS = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    private Maze maze;
    private boolean[][] visited;
    private List<AlgorithmResult.Step> steps;
    private Cell end;
    private int visitedCount;

    public static AlgorithmResult solve(Maze maze) {
        return new Backtracking().run(maze);
    }

    private AlgorithmResult run(Maze maze) {
        AlgorithmResult result = new AlgorithmResult();
        result.algorithmName = "Backtracking";
        long startTime = System.nanoTime();

        this.maze = maze;
        this.visited = new boolean[Maze.SIZE][Maze.SIZE];
        this.steps = new ArrayList<>();
        this.visitedCount = 0;
        this.end = maze.getEnd();

        List<Cell> path = new ArrayList<>();
        boolean found = solveFrom(maze.getStart(), path);

        result.steps = steps;
        result.visitedCount = visitedCount;
        result.solved = found;
        if (found) {
            result.path = path;
            for (Cell c : path) {
                steps.add(new AlgorithmResult.Step(c, AlgorithmResult.StepType.PATH));
            }
        }
        result.timeTakenNanos = System.nanoTime() - startTime;
        return result;
    }

    private boolean solveFrom(Cell current, List<Cell> path) {
        if (!maze.isWalkable(current.row, current.col) || visited[current.row][current.col]) {
            return false;
        }

        // ---- CHOOSE ----
        visited[current.row][current.col] = true;
        visitedCount++;
        path.add(current);
        steps.add(new AlgorithmResult.Step(current, AlgorithmResult.StepType.VISIT));

        if (current.equals(end)) {
            return true;
        }

        // ---- EXPLORE ----
        for (int[] d : DIRS) {
            Cell next = new Cell(current.row + d[0], current.col + d[1]);
            if (solveFrom(next, path)) {
                return true;
            }
        }

        // ---- UN-CHOOSE (backtrack) ----
        path.remove(path.size() - 1);
        steps.add(new AlgorithmResult.Step(current, AlgorithmResult.StepType.BACKTRACK));
        return false;
    }
}
