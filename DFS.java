import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class DFS {

    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public static AlgorithmResult solve(Maze maze) {
        AlgorithmResult result = new AlgorithmResult();
        result.algorithmName = "DFS";
        long startTime = System.nanoTime();

        int size = Maze.SIZE;
        boolean[][] visited = new boolean[size][size];

        Cell start = maze.getStart();
        Cell end = maze.getEnd();

        Deque<Cell> pathStack = new ArrayDeque<>();
        Deque<Integer> nextDirStack = new ArrayDeque<>();

        pathStack.push(start);
        nextDirStack.push(0);
        visited[start.row][start.col] = true;
        result.steps.add(new AlgorithmResult.Step(start, AlgorithmResult.StepType.VISIT));
        result.visitedCount++;

        boolean found = start.equals(end);

        while (!pathStack.isEmpty() && !found) {
            Cell current = pathStack.peek();
            int dirIndex = nextDirStack.pop();

            boolean advanced = false;
            while (dirIndex < DIRS.length) {
                int[] d = DIRS[dirIndex];
                dirIndex++;
                int nr = current.row + d[0];
                int nc = current.col + d[1];

                if (maze.isWalkable(nr, nc) && !visited[nr][nc]) {
                    visited[nr][nc] = true;
                    Cell next = new Cell(nr, nc);

                    nextDirStack.push(dirIndex); // resume point for "current"
                    pathStack.push(next);
                    nextDirStack.push(0);        // start point for "next"

                    result.steps.add(new AlgorithmResult.Step(next, AlgorithmResult.StepType.VISIT));
                    result.visitedCount++;

                    if (next.equals(end)) {
                        found = true;
                    }
                    advanced = true;
                    break;
                }
            }

            if (!advanced) {
                // Dead end reached: abandon this cell and step back.
                pathStack.pop();
                result.steps.add(new AlgorithmResult.Step(current, AlgorithmResult.StepType.BACKTRACK));
            }
        }

        result.solved = found;

        if (found) {
            List<Cell> path = new ArrayList<>(pathStack); // top (End) first
            Collections.reverse(path);                    // Start -> End
            result.path = path;
            for (Cell c : path) {
                result.steps.add(new AlgorithmResult.Step(c, AlgorithmResult.StepType.PATH));
            }
        }

        result.timeTakenNanos = System.nanoTime() - startTime;
        return result;
    }
}
