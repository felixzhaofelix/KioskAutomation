import java.util.ArrayList;
import java.util.List;

public class Status {
    private static String status;

    private static List<Rule> rules;

    private static List<Scenario> scenarios;

    public static void setScenarios(List<Scenario> scenarios) {
        Status.status = "";
        Status.scenarios = scenarios;
    }

    public static void setRules(List<Rule> rules) {
        Status.rules = rules;
    }

    private static void printStatus() {
        System.out.println(status);
    }

    /**
     * Displays the details of each scenario.
     *
     * @param scenarios List of Scenario objects to be displayed.
     */
    public static void showScenarios(List<Scenario> scenarios) {
        for (Scenario scenario : scenarios) {
            System.out.println("Kiosk " + (scenarios.indexOf(scenario) + 1) + " " + scenario);
        }
        System.out.println("Agent 1: Technician");
        System.out.println("Agent 2: Representative");
    }

    /**
     * Assess the statuses of all scenarios based on the provided rules.
     */
    public static void assessStatuses() {
        for (Scenario scenario : scenarios) {
            assessStatus(scenario);
        }
        System.out.println("1 technician and 1 representative are available");
    }

    /**
     * Assess the status of a specific scenario based on the provided rules.
     *
     * @param scenario Scenario object to be assessed.
     */
    private static void assessStatus(Scenario scenario) {
        // this part is to determine the statuses using the rules
        for (Rule rule : rules) {
            if (rule.matches(scenario)) {
                if (!rule.isAvailable()) { // Rule 1 or 2 or 3 or 4
                    if (!rule.isTech()) { // Rule 4
                        status = ("Kiosk " + scenario.getName() + " is unavailable because out of paper");
                    }
                    status = ("Kiosk " + scenario.getName() + " is unavailable because of paper jam"); // Rule 1 or 2 or 3
                    break;
                } else if (rule.isWarning()) { // Rule 5
                    status = ("Kiosk " + scenario.getName() + " is available because no jam and low paper");
                    break;
                } else { // Rule 6
                    status = ("Kiosk " + scenario.getName() + " is available");
                    break;
                }
            }
        }
        printStatus();
    }
}
