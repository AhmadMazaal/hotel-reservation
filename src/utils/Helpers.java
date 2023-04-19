package utils;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {
    private final Scanner scanner;

    public Helpers() {
        this.scanner = new Scanner(System.in);
    }

    public char readYesNo(String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = this.scanner.nextLine().trim();
            if (
                input.length() == 1 &&
                (
                        input.equalsIgnoreCase("y") ||
                        input.equalsIgnoreCase("n")
                )
            ) {
                return input.toLowerCase().charAt(0);
            }
            System.out.println("Invalid input. Please enter 'y' (Yes) or 'n' (No).");
        }
    }

    public static boolean isInvalidEmail(String email){
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return !matcher.find();
    }

//    public boolean readPromptWithCondition(String prompt, boolean condition, String promptError) {
//        String input;
//        do{
//            System.out.println(prompt);
//            input = this.scanner.nextLine().trim();
//            if (condition) {
//                System.out.println(promptError);
//            }
//        }while(condition);
//        return true;
//    }

}
