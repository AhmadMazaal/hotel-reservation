package model;

public enum MainMenuQuestionType {
    FIND_AND_RESERVE_ROOM(1, "Find and reserve a room"),
    SEE_MY_RESERVATIONS(2, "See my reservations"),
    CREATE_AN_ACCOUNT(3, "Create an account"),
    ADMIN(4, "Admin"),
    EXIT(5, "Exit");

    private final int value;
    private final String question;

    MainMenuQuestionType(int value, String question) {
        this.value = value;
        this.question = question;
    }

    public int getValue() {
        return value;
    }

    public String getQuestion() {
        return question;
    }

    public static MainMenuQuestionType fromValue(int value) {
        for (MainMenuQuestionType option : values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        throw new IllegalArgumentException("Invalid MenuOption value: " + value);
    }
}
