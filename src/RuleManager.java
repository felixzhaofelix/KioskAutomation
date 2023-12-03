import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RuleManager {

    public static void main(String[] args) {

        // Demonstrations

        // Create a list of rules
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(true, 2, true, true, true,false)); //jam, no paper
        rules.add(new Rule(true, 1, true, true, true,false)); //jam, low paper
        rules.add(new Rule(true, 0, true, false, false, false)); //jam, high paper
        rules.add(new Rule(false, 2, false, true, true, false)); //no jam, no paper
        rules.add(new Rule(false, 1, false, true, true, true)); //no jam, low paper
        rules.add(new Rule(false, 0, false, false, false,true)); //no jam, high paper

        // Serialize the rules to JSON and save to a file
        dumpRulesToJson(rules, "src/Rules.json");

        // Load the rules from the JSON file
        List<Rule> loadedRules = loadRulesFromJson("src/Rules.json");

        // Print the loaded rules
        for (Rule rule : loadedRules) {
            System.out.println(rule);
        }
    }

    /**
     * Serializes a list of Rule objects to JSON and saves it to a file.
     *
     * @param rules    List of Rule objects to be serialized.
     * @param filename Name of the file to save the JSON data.
     */
    public static void dumpRulesToJson(List<Rule> rules, String filename) {
        JSONArray jsonArray = convertRulesToJsonArray(rules);

        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(jsonArray.toString(2));
            System.out.println("Rules serialized and saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a JSON file and converts its content into a list of Rule objects.
     *
     * @param filename Name of the JSON file to read.
     * @return List of Rule objects parsed from the JSON file.
     */
    public static List<Rule> loadRulesFromJson(String filename) {
        List<Rule> rules = new ArrayList<>();
        List<JSONObject> jsonRules = readJsonFile(filename);

        for (JSONObject jsonRule : jsonRules) {
            rules.add(new Rule(jsonRule));
        }
        return rules;
    }

    /**
     * Reads a JSON file and converts it into a list of JSONObject.
     *
     * @param filePath Path to the JSON file.
     * @return List of JSONObject parsed from the JSON file.
     */
    private static List<JSONObject> readJsonFile(String filePath) {
        List<JSONObject> jsonObjects = new ArrayList<>();

        try {
            // Read the entire file content as a string
            String jsonString = new String(Files.readAllBytes(Paths.get(filePath)));

            // Parse the string into a JSONArray
            JSONArray jsonArray = new JSONArray(jsonString);

            // Convert each element in the array to a JSONObject and add to the list
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjects.add(jsonArray.getJSONObject(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonObjects;
    }

    /**
     * Converts a list of Rule objects to a JSONArray.
     *
     * @param ruleList List of Rule objects to be converted.
     * @return JSONArray containing the serialized Rule objects.
     */
    private static JSONArray convertRulesToJsonArray(List<Rule> ruleList) {
        JSONArray jsonArray = new JSONArray();

        for (Rule rule : ruleList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("jam", rule.isJam());
            jsonObject.put("paper", rule.getPaper());
            jsonObject.put("tech", rule.isTech());
            jsonObject.put("rep", rule.isRep());
            jsonObject.put("warning", rule.isWarning());
            jsonObject.put("available", rule.isAvailable());

            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }
}
