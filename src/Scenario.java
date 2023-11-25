public class Scenario {

    private String name;
    private boolean jam;
    private int paper;
    private boolean warning;
    private boolean available;

    public Scenario(String name, boolean jam, int paper) {
        this.name = name;
        this.jam = jam;
        this.paper = paper;
        this.warning = true;
        this.available = false;
    }

    public String getName() {
        return name;
    }

    public boolean isJam() {
        return jam;
    }

    public int isPaper() {
        return paper;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJam(boolean jam) {
        this.jam = jam;
    }

    public void setPaper(int paper) {
        this.paper = paper;
    }

    public int getPaper() {
        return paper;
    }

    public boolean isWarning() {
        return warning;
    }

    public void setWarning(boolean warning) {
        this.warning = warning;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String toString() {
        String jammed = jam ? "Jam" : "No Jam";
        String paper = this.paper == 2 ? "No Paper" : this.paper == 1 ? "Low Paper" : "High Paper";
        return name + " : " + paper + ", " + jammed;
    }


}
