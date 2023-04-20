package model;

public enum AdminMenuQuestionType {
    SEE_ALL_CUSTOMERS(1, "See all customers"),
    SEE_ALL_ROOMS(2, "See all rooms"),
    SEE_ALL_RESERVATIONS(3, "See all reservations"),
    ADD_ROOM(4, "Add a room"),
    BACK_TO_MAIN_MENU(5, "Back to main menu");

    private final int value;
    private final String question;

    AdminMenuQuestionType(int value, String question) {
        this.value = value;
        this.question = question;
    }

    public int getValue() {
        return value;
    }

    public String getQuestion() {
        return question;
    }

    public static AdminMenuQuestionType fromValue(int value) {
        for (AdminMenuQuestionType option : values()) {
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
