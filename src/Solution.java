import java.util.ArrayList;
import java.util.List;

public class Solution {
    private List<String> steps;

    public Solution() {
        this.steps = new ArrayList<>();
    }

    public void addStep(String step) {
        steps.add(step);
    }

    public List<String> getSteps() {
        return steps;
    }

    public void printSolution() {
        for (String step : steps) {
            System.out.println(step);
        }
    }
}
