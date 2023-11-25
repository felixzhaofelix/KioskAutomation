import org.json.JSONObject;

public class Rule {
    private boolean jam;
    private int paper;
    private boolean tech;
    private boolean rep;
    private boolean warning;
    private boolean available;

    public Rule(boolean jam, int paper, boolean tech, boolean rep, boolean warning, boolean available) {
        this.jam = jam;
        this.paper = paper;
        this.tech = tech;
        this.rep = rep;
        this.warning = warning;
        this.available = available;
    }

    public Rule(JSONObject ruleJson) {
        this.jam = ruleJson.getBoolean("jam");
        this.paper = ruleJson.getInt("paper");
        this.tech = ruleJson.getBoolean("tech");
        this.rep = ruleJson.getBoolean("rep");
        this.warning = ruleJson.getBoolean("warning");
        this.available = ruleJson.getBoolean("available");
    }


    public boolean matches(Scenario scenario) {
        return jam == scenario.isJam() &&
                paper == scenario.getPaper();
    }

    public void applies(Scenario scenario) {
        scenario.setWarning(this.warning);
        scenario.setAvailable(this.available);
    }

    public boolean isJam() {
        return jam;
    }

    public int getPaper() {
        return paper;
    }

    public boolean isTech() {
        return tech;
    }

    public boolean isRep() {
        return rep;
    }

    public boolean isWarning() {
        return warning;
    }

    public boolean isAvailable() {
        return available;
    }

    public String toString() {
        return "Rule: jam=" + jam + ", paper=" + paper + ", tech=" + tech + ", rep=" + rep + ", warning=" + warning + ", available=" + available;
    }
}
