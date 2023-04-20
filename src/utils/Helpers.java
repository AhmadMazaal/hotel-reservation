package utils;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {

    public static char readYesNo(String prompt) {
        final Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
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

    public static char readChoices(String prompt) {
        final Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
            if (
                    input.length() == 1 &&
                            (
                                input.equalsIgnoreCase("1") ||
                                input.equalsIgnoreCase("2")
                            )
            ) {
                return input.toLowerCase().charAt(0);
            }
            System.out.println("Invalid input. Please enter 'y' (Yes) or 'n' (No).");
        }
    }

    public static String emailPrompt() {
        final Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter your email address (format should as such: name@domain.com)");
            String email = scanner.nextLine().trim();
            if (!isInvalidEmail(email)) {
                return email;
            }
            System.out.println("Invalid email address, please try again.");
        }
    }

    public static boolean isInvalidEmail(String email){
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return !matcher.find();
    }

    public static boolean isDateInvalid(String dateStr){
        String dateRegex = "^\\d{2}/\\d{2}/\\d{4}$";
        if (!dateStr.matches(dateRegex)) {
            System.out.println("Invalid date format, please enter in the format of mm/dd/yyyy");
            return true;
        }
        return false;
    }

    public static String formatDate(Date date){
        ZoneId zoneId = ZoneId.of("UTC");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy").withZone(zoneId);
        LocalDate localDate = date.toInstant().atZone(zoneId).toLocalDate();
        return localDate.format(formatter);
    }

}
