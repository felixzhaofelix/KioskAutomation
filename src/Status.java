import java.util.ArrayList;
import java.util.List;

public class Status {
    private static String status;

    private static List<Scenario> scenarios;

    public static void setScenarios(List<Scenario> scenarios) {
        Status.status = "";
        Status.scenarios = scenarios;
    }

//    public static void addStep(String step) {
//        Status.steps.add(step);
//    }
//
//    public static List<String> getSteps() {
//        return Status.steps;
//    }

    public static void printStatus() {
        System.out.println(status);
    }

    public static void showScenarios(List<Scenario> scenarios) {
        for (Scenario scenario : scenarios) {
            System.out.println("Kiosk " + (scenarios.indexOf(scenario) + 1) + " " + scenario);
        }
        System.out.println("Agent 1 : Technician");
        System.out.println("Agent 2 : Representative");
    }

    public static void assessStatuses() {
        for (Scenario scenario : scenarios) {
            assessStatus(scenario);
        }
        System.out.println("1 technician and 1 representative are available");
    }

    public static void assessStatus(Scenario scenario) {
            if (scenario.isJam()) {
                status = ("Kiosk " + scenario.getName() + " is unavailable because of paper jam");
            } else {
                if (!scenario.isJam() && scenario.isPaper() == 2) {
                    status = ("Kiosk " + scenario.getName() + " is unavailable because out of paper");
                } else if (!scenario.isJam() && scenario.isPaper() == 1) {
                    status = ("Kiosk " + scenario.getName() + " is available because no jam and low paper");
                } else if (!scenario.isJam() && scenario.isPaper() == 0) {
                    status = ("Kiosk " + scenario.getName() + " is available");
                }
            }
            printStatus();
    }
}
