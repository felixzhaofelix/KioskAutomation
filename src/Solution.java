import java.util.ArrayList;
import java.util.List;

public class Solution {
    private List<String> steps;

    public Solution() {
        this.steps = new ArrayList<>();
    }

    /**
     * Adds a step to the solution.
     *
     * @param step Step to be added to the solution.
     */
    public void addStep(String step) {
        steps.add(step);
    }

    /**
     * Prints the solution.
     */
    public void printSolution() {
        for (String step : steps) {
            if (step.equals("loading")) {
                addDelay();//add delay but don't print "loading"
                continue;
            }
            System.out.println(step);
        }
    }


    private void addDelay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
