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
            if (step.equals("loading")) {
                addDelay();//add delay but dont print "loading"
                continue;
            }
            System.out.println(step);
        }
    }
    public void addDelay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
