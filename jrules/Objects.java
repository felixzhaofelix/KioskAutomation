// Object Definitions with Enums

// Represents the initial screen displayed on the kiosk.
class InitialScreen {
    boolean isOn;  // Indicates whether the initial screen is currently displayed.
}

// Represents the status screen displayed on the kiosk.
class StatusScreen {
    boolean isOn;  // Indicates whether the status screen is currently displayed.
}

// Represents the solution screen displayed on the kiosk.
class SolutionScreen {
    boolean isOn;  // Indicates whether the solution screen is currently displayed.
}

// Represents a kiosk with various attributes.
class Kiosk {
    String name;        // Name or identifier of the kiosk.

    int number;         // Number of the kiosk.
    JamStatus jam;      // Current status of paper jam on the kiosk.
    PaperStatus paper;   // Current status of paper on the kiosk.
    Availability availability; // Availability status of the kiosk.

    // Enum for Jam Status
    enum JamStatus {
        JAMMED,
        NOT_JAMMED
    }

    // Enum for Paper Status
    enum PaperStatus {
        OUT_OF_PAPER,
        LOW_PAPER,
        HIGH_PAPER
    }

    // Enum for Availability Status
    enum Availability {
        AVAILABLE,
        NOT_AVAILABLE
    }
}

// Represents an agent with a specific role and availability status.
class Agent {
    Title title;       // Role or title of the agent.
    int number;         // Identifier or number of the agent.
    Availability availability; // Availability status of the agent.


    // Enum for Title
    enum Title {
        TECHNICIAN,
        REPRESENTATIVE,
    }
    // Enum for Availability Status
    enum Availability {
        AVAILABLE,
        NOT_AVAILABLE
    }
}
