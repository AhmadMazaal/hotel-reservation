package menu;

import model.*;
import seeders.Seeders;

import java.util.Scanner;

public class TestDataMenu {
    private static final Scanner scanner = new Scanner(System.in);

    public void initMenu(){
        boolean running = true;
        while (running) {
            int choice = getUserInput();
            TestDataQuestionsType option = TestDataQuestionsType.fromValue(choice);
            switch (option){
                case GENERATE_CUSTOMERS:
                    Seeders.generateCustomers();
                    break;
                case GENERATE_ROOMS:
                    Seeders.generateRooms();
                    break;
                case GENERATE_RESERVATIONS:
                    Seeders.generateReservations();
                    break;
                case BACK_TO_MAIN_MENU:
                    System.out.println("Going back..");
                    running = false;
                    break;
                default:{
                    System.out.println("Invalid choice, please try again.");
                    break;
                }
            }
        }
    }

    private int getUserInput(){
        int choice;
        int minOption = 1;
        int maxOption = TestDataQuestionsType.values().length;
        boolean validInput;
        do {
            TestDataQuestionsType.printMenuHeader();
            for (TestDataQuestionsType option : TestDataQuestionsType.values()) {
                System.out.println(option.getValue() + ". " + option.getQuestion());
            }
            String input = scanner.nextLine();
            if (input.length() != 1 || !Character.isDigit(input.charAt(0))) {
                System.out.println("Invalid input. Enter a number between " +minOption + " & "+ maxOption);
                validInput = false;
                choice = 0;
            } else {
                choice = Integer.parseInt(input);
                validInput = (choice >= minOption && choice <= maxOption);
            }
        } while (!validInput);
        return  choice;
    }

}
