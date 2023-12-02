import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //load rules from json
        List<Rule> rules = RuleManager.loadRulesFromJson("src/newRules.json"); //load rules from json file

        //create scenarios
        List<Scenario> scenarios = new ArrayList<>();

        // User input for scenarios
        Scanner scanner = new Scanner(System.in);

        //welcome message
        System.out.println("\nKiosk Automation System\n");

        System.out.println("Use user defined scenarios? (true or false): ");
        boolean userDefined = scanner.nextBoolean();
        if (userDefined) {
            System.out.print("Enter the number of scenarios: ");
            int numScenarios = scanner.nextInt();

            for (int i = 1; i <= numScenarios; i++) {
                System.out.println("\nScenario " + i + ":");
                Scenario scenario = createScenario(scanner);
                scenarios.add(scenario);
            }
        } else {
            System.out.println("Generating default scenarios");
            System.out.println("loading ...........................");
            Scenario scenario1 = new Scenario("CarRental", true, 1);
            Scenario scenario2 = new Scenario("Park", false, 1);
            Scenario scenario3 = new Scenario("Ticket Shop", true, 1);
            Scenario scenario4 = new Scenario("Main Hall", false, 2);
            Scenario scenario5 = new Scenario("Restaurant", false, 1);
            Scenario scenario6 = new Scenario("Atrium", false, 0);

            scenarios.add(scenario1);
            scenarios.add(scenario2);
            scenarios.add(scenario3);
            scenarios.add(scenario4);
            scenarios.add(scenario5);
            scenarios.add(scenario6);
        }

        // Close the scanner
        scanner.close();


        //show scenarios
        Status.setScenarios(scenarios);
        Status.showScenarios(scenarios);
        System.out.println();
        //show statuses
        Status.assessStatuses();
        System.out.println();
        //resolve scenarios
        Resolver resolver = new Resolver(rules);
        resolver.resolveAll(scenarios);

    }



    private static Scenario createScenario(Scanner scanner) {
        System.out.print("Enter the name of the kiosk: ");
        String name = scanner.next();
        System.out.print("Is the kiosk jammed? (true or false): ");
        boolean jam = scanner.nextBoolean();
        System.out.print("Is the paper low? (0 for high, 1 for low and 2 for out): ");
        int paper = scanner.nextInt();

        return new Scenario(name, jam, paper);
    }
}



