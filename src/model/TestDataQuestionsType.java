package model;

public enum TestDataQuestionsType {

    GENERATE_CUSTOMERS(1, "Generate customers"),
    GENERATE_ROOMS(2, "Generate rooms"),
    GENERATE_RESERVATIONS(3, "Generate reservations(Not tested well yet)"),
    BACK_TO_MAIN_MENU(4, "Back to main menu");

    private final int value;
    private final String question;

    TestDataQuestionsType(int value, String question) {
        this.value = value;
        this.question = question;
    }

    public int getValue() {
        return value;
    }

    public String getQuestion() {
        return question;
    }

    public static TestDataQuestionsType fromValue(int value) {
        for (TestDataQuestionsType option : values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        throw new IllegalArgumentException("Invalid MainMenuOption value: " + value);
    }


    public static void printMenuHeader(){
        System.out.println("*****************************************************************************");
        System.out.println("**\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t   **");
        System.out.println("**\t\t\t\t\t\t\t\t Admin access\t\t\t\t\t\t\t   **");
        System.out.println("**\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t   **");
        System.out.println("*****************************************************************************");
    }
}
