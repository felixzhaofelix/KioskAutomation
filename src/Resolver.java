import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Resolver {
    private List<Rule> rules;

    public Resolver(List<Rule> rules) {
        this.rules = rules;
    }

    /**
     * Resolves a scenario based on the provided rules.
     *
     * @param scenario Scenario object to be resolved.
     * @return Solution object containing the steps to resolve the scenario.
     */
    public Solution resolve(Scenario scenario) {
        Solution solution = new Solution();
        //DO WHILE

        do {
            for (Rule rule : rules) {
                if (rule.matches(scenario)) {
                    if (rule.isTech()) { //Rule 1 or 2 or 3
                        solution.addStep("Kiosk " + scenario.getName() + " is unavailable because of paper jam");
                        solution.addStep("loading");
                        solution.addStep("Agent 1 Technician has removed the jam from Kiosk " + scenario.getName());
                        scenario.setJam(false); //remove jam Rule 123 become Rule 456
                        break;
                    } else if (rule.isRep() && rule.isWarning() && !rule.isAvailable()) { //Rule 4
                        solution.addStep("Kiosk " + scenario.getName() + " is unavailable because out of paper");
                        solution.addStep("loading");
                        solution.addStep("Agent 2 Representative has added a new roll to Kiosk " + scenario.getName());
                        scenario.setPaper(0); //set paper to high(0), Rule 4 become Rule 6
                        scenario.setWarning(false); //remove warning
                        break;
                    } else if (rule.isRep() && rule.isWarning() && rule.isAvailable()) { //Rule 5
                        solution.addStep("Kiosk " + scenario.getName() + " is available because no jam and low paper");
                        solution.addStep("loading");
                        solution.addStep("Agent 2 Representative has added a new roll to Kiosk " + scenario.getName());
                        scenario.setPaper(0); //set paper to high(0), Rule 5 become Rule 6
                        scenario.setWarning(false); //remove warning
                        break;
                    } else { //Rule 6
                        solution.addStep("Kiosk " + scenario.getName() + " is available and has high paper level");
                        rule.applies(scenario); //remove warning and set kiosk to available
                        break;
                    }
                }
            }


        } while (!scenario.isAvailable() || scenario.isWarning());

        return solution;
    }

    /**
     * Resolves all scenarios based on the provided rules.
     *
     * @param scenarios List of Scenario objects to be resolved.
     * @return List of Solution objects containing the steps to resolve the scenarios in an random order.
     */
    public List<Solution> resolveAll(List<Scenario> scenarios) {
        List<Solution> solutions = new ArrayList<>();
        Collections.shuffle(scenarios);

        for (Scenario scenario : scenarios) {
            solutions.add(resolve(scenario));
        }
        return solutions;
    }



}

