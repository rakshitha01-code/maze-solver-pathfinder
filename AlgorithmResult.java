import java.util.ArrayList;
import java.util.List;


public class AlgorithmResult {

    /** The kind of animation event produced during solving. */
    public enum StepType {
        VISIT,      // a cell was explored / entered
        BACKTRACK,  // a cell was abandoned (dead end) and left the active path
        PATH        // a cell is part of the final solution path
    }

    /** A single animation event: "do X to cell Y". */
    public static class Step {
        public final Cell cell;
        public final StepType type;

        public Step(Cell cell, StepType type) {
            this.cell = cell;
            this.type = type;
        }
    }

    public String algorithmName = "";
    public List<Step> steps = new ArrayList<>();
    public List<Cell> path = new ArrayList<>();
    public boolean solved = false;
    public int visitedCount = 0;
    public long timeTakenNanos = 0;

    public int getPathLength() {
        return path.size();
    }

    public double getTimeTakenMillis() {
        return timeTakenNanos / 1_000_000.0;
    }
}
