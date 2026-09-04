import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;


public class BFS {

    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public static AlgorithmResult solve(Maze maze) {
        AlgorithmResult result = new AlgorithmResult();
        result.algorithmName = "BFS";
        long startTime = System.nanoTime();

        int size = Maze.SIZE;
        boolean[][] visited = new boolean[size][size];
        Cell[][] parent = new Cell[size][size];

        Cell start = maze.getStart();
        Cell end = maze.getEnd();

        Queue<Cell> queue = new ArrayDeque<>();
        queue.add(start);
        visited[start.row][start.col] = true;

        boolean found = false;

        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            result.steps.add(new AlgorithmResult.Step(current, AlgorithmResult.StepType.VISIT));
            result.visitedCount++;

            if (current.equals(end)) {
                found = true;
                break;
            }

            for (int[] d : DIRS) {
                int nr = current.row + d[0];
                int nc = current.col + d[1];
                if (maze.isWalkable(nr, nc) && !visited[nr][nc]) {
                    visited[nr][nc] = true;
                    parent[nr][nc] = current;
                    queue.add(new Cell(nr, nc));
                }
            }
        }

        result.solved = found;

        if (found) {
            List<Cell> path = new ArrayList<>();
            Cell step = end;
            path.add(step);
            while (!step.equals(start)) {
                step = parent[step.row][step.col];
                path.add(step);
            }
            Collections.reverse(path);
            result.path = path;
            for (Cell c : path) {
                result.steps.add(new AlgorithmResult.Step(c, AlgorithmResult.StepType.PATH));
            }
        }

        result.timeTakenNanos = System.nanoTime() - startTime;
        return result;
    }
}
