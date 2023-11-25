import java.util.ArrayList;
import java.util.List;

public class Resolver {
    private List<Rule> rules;

    public Resolver(List<Rule> rules) {
        this.rules = rules;
    }

    public void resolve(Scenario scenario) {
        Solution solution = new Solution();
        //DO WHILE

        do {
            for (Rule rule : rules) {
                if (rule.matches(scenario)) {
                    if (rule.isTech()) { //Rule 1 or 2 or 3
                        solution.addStep(scenario.getName() + " is unavailable because of paper jam");
                        solution.addStep("Agent 1 Technician has removed the jam from Kiosk " + scenario.getName());
                        scenario.setJam(false); //remove jam Rule 123 become Rule 456
                        break;
                    } else if (rule.isRep() && rule.isWarning() && !rule.isAvailable()) { //Rule 4
                        solution.addStep(scenario.getName() + " is unavailable because out of paper");
                        solution.addStep("Agent 2 Representative has added a new roll to Kiosk " + scenario.getName());
                        scenario.setPaper(0); //set paper to high(0), Rule 4 become Rule 6
                        scenario.setWarning(false); //remove warning
                        break;
                    } else if (rule.isRep() && rule.isWarning() && rule.isAvailable()) { //Rule 5
                        solution.addStep(scenario.getName() + " is available because no jam and low paper");
                        solution.addStep("Agent 2 Representative has added a new roll to Kiosk " + scenario.getName());
                        scenario.setPaper(0); //set paper to high(0), Rule 5 become Rule 6
                        break;
                    } else { //Rule 6
                        solution.addStep("Kiosk " + scenario.getName() + " is available");
                        rule.applies(scenario); //remove warning and set kiosk to available
                        break;
                    }
                }
            } // end for loop
        } while (!scenario.isAvailable() || scenario.isWarning());

        solution.printSolution();
    } // end resolve method

    public void resolveAll(List<Scenario> scenarios) {
        for (Scenario scenario : scenarios) {
            resolve(scenario);
        }
    }

}

