import java.util.ArrayList;
import java.util.List;


public class Recursion {

    private static final int[][] DIRS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    private Maze maze;
    private boolean[][] visited;
    private List<AlgorithmResult.Step> steps;
    private Cell end;
    private int visitedCount;

    public static AlgorithmResult solve(Maze maze) {
        return new Recursion().run(maze);
    }

    private AlgorithmResult run(Maze maze) {
        AlgorithmResult result = new AlgorithmResult();
        result.algorithmName = "Recursion";
        long startTime = System.nanoTime();

        this.maze = maze;
        this.visited = new boolean[Maze.SIZE][Maze.SIZE];
        this.steps = new ArrayList<>();
        this.visitedCount = 0;
        this.end = maze.getEnd();

        List<Cell> path = new ArrayList<>();
        boolean found = recurse(maze.getStart(), path);

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

    /** Returns true if a path to End was found starting from "current". */
    private boolean recurse(Cell current, List<Cell> path) {
        if (!maze.isWalkable(current.row, current.col) || visited[current.row][current.col]) {
            return false;
        }

        visited[current.row][current.col] = true;
        visitedCount++;
        path.add(current);
        steps.add(new AlgorithmResult.Step(current, AlgorithmResult.StepType.VISIT));

        if (current.equals(end)) {
            return true;
        }

        for (int[] d : DIRS) {
            Cell next = new Cell(current.row + d[0], current.col + d[1]);
            if (maze.isWalkable(next.row, next.col) && !visited[next.row][next.col]) {
                if (recurse(next, path)) {
                    return true;
                }
            }
        }

        // No neighbor led to End - unwind (backtrack).
        path.remove(path.size() - 1);
        steps.add(new AlgorithmResult.Step(current, AlgorithmResult.StepType.BACKTRACK));
        return false;
    }
}
