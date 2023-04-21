package menu;

import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.MainMenuQuestionType;
import model.Reservation;
import utils.Helpers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class MainMenu {
    private final Scanner scanner;
    private static final HotelResource hotelResource = new HotelResource();

    public MainMenu(){
        this.scanner = new Scanner(System.in);
    }

    public void initMainMenu(){
        boolean running = true;
        while (running) {
            int choice = this.getUserInput();
            MainMenuQuestionType option = MainMenuQuestionType.fromValue(choice);
            switch (option){
                case FIND_AND_RESERVE_ROOM->handleFindAndReserveRoom();
                case SEE_MY_RESERVATIONS->handleGetCustomerReservations();
                case CREATE_AN_ACCOUNT->handleAddNewCustomer();
                case ADMIN->startAdminMenu();
                case EXIT-> {
                    running = false;
                    System.out.println("Exiting...");
                }
                default->System.out.println("Invalid choice");

            }
        }

    }

    private int getUserInput(){
        int choice;
        int minOption = 1;
        int maxOption = MainMenuQuestionType.values().length;
        boolean validInput = true;
        do {
            MainMenuQuestionType.printMenuHeader();
            for (MainMenuQuestionType option : MainMenuQuestionType.values()) {
                System.out.println(option.getValue() + ". " + option.getQuestion());
            }
            String input = this.scanner.nextLine();
            if (input.length() != 1 || !Character.isDigit(input.charAt(0))) {
                System.out.println("Invalid input. Enter a number between " +minOption + " & "+ maxOption);
                choice = 0;
                validInput = false;
            } else {
                choice = Integer.parseInt(input);
                validInput = (choice >= minOption && choice <= maxOption);
            }
        } while (!validInput);
        return choice;
    }

    public void startAdminMenu(){
        AdminMenu adminMenu = new AdminMenu();
        adminMenu.initAdminMenu();
    }

    public String handleAddNewCustomer(){
        boolean doesCustomerExist = false;
        String email;
        do{
            email = Helpers.emailPrompt();
            Customer customer = hotelResource.getCustomer(email);
            if(customer != null){
                System.out.println("Email already exists! try another one.");
                doesCustomerExist = true;
            }else{
                doesCustomerExist = false;
            }
        }while(doesCustomerExist);


        System.out.println("Enter your first name");
        String firstName = scanner.nextLine();

        System.out.println("Enter your last name");
        String lastName = scanner.nextLine();

        hotelResource.createACustomer(email, firstName, lastName);

        return email;
    }

    public void handleGetCustomerReservations(){
        String email = Helpers.emailPrompt();
        Collection<Reservation> totalReservations =  hotelResource.getCustomersReservations(email);
        if(totalReservations.size() == 0 ){
            System.out.println(email + " has no reservations yet! Press 1 to make a new reservation.");
            return;
        }
        System.out.println(email + " has the following reservations: \n");
        System.out.println("---------------");
        for(Reservation reservation:totalReservations){
            System.out.println(reservation+"\n");
        }
        System.out.println("---------------\n");
    }

    public void handleFindAndReserveRoom() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy").withZone(ZoneId.of("UTC"));
        LocalDate checkInLocalDate = null, checkOutLocalDate = null;
        Collection<IRoom> availableRooms;

        do {
            System.out.println("Enter the check-in date in the format of mm/dd/yyyy (for example, 04/16/2023)");
            String checkInDateStr = scanner.nextLine();

            if (Helpers.isDateInvalid(checkInDateStr)) {
                continue;
            }

            try {
                checkInLocalDate = LocalDate.parse(checkInDateStr, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format, please enter in the format of mm/dd/yyyy");
            }

        } while (checkInLocalDate == null);

        do {
            System.out.println("Enter the check-out date in the format of mm/dd/yyyy (for example, 04/18/2023)");
            String checkOutDateStr = scanner.nextLine();

            if (Helpers.isDateInvalid(checkOutDateStr)) {
                continue;
            }

            try {
                checkOutLocalDate = LocalDate.parse(checkOutDateStr, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format, please enter in the format of mm/dd/yyyy");
                continue;
            }

            if (checkOutLocalDate.isBefore(checkInLocalDate)) {
                System.out.println("Check-out date must be after check-in date, please enter again");
                checkOutLocalDate = null;
            }

        } while (checkOutLocalDate == null);

        ZoneId zoneId = ZoneId.of("UTC");
        Date checkInDate = Date.from(checkInLocalDate.atStartOfDay(zoneId).toInstant());
        Date checkOutDate = Date.from(checkOutLocalDate.atStartOfDay(zoneId).toInstant());
        availableRooms = hotelResource.findARoom(checkInDate, checkOutDate, true);

        if(availableRooms.size() == 0){
                checkInDate = Date.from(checkInDate.toInstant().plus(7, ChronoUnit.DAYS));
                checkOutDate = Date.from(checkOutDate.toInstant().plus(7, ChronoUnit.DAYS));
                String formattedNewCheckInDate = Helpers.formatDate(checkInDate);
                String formattedNewCheckOutDate = Helpers.formatDate(checkOutDate);
                System.out.println("No rooms are available in the date provided. Refining the search for: " + formattedNewCheckInDate +" & " + formattedNewCheckOutDate);
                availableRooms = hotelResource.findARoom(checkInDate, checkOutDate, false);
                if(availableRooms.size() > 0){
                    System.out.println(availableRooms.size() + " room(s) found!\n");
                }
                if(availableRooms.size() == 0){
                    boolean shouldRetrySearch = Helpers.readYesNo("No rooms are available. Would you like to try different dates?");
                    if(shouldRetrySearch){
                        handleFindAndReserveRoom();
                        return;
                    }
                    return;
                }
        }

        System.out.println("We have the following rooms available in the specified date:");
        System.out.println("---------------");
        for(IRoom room:availableRooms){
            System.out.println(room);
        }
        System.out.println("---------------\n");

        boolean shouldReserveRoom = Helpers.readYesNo("Would you like to reserve a room?");
        if(!shouldReserveRoom){
            return;
        }

        String email;
        boolean shouldCreateAccount = Helpers.readYesNo("Do you have an account with us?");
        if(!shouldCreateAccount){
            System.out.println("Creating a new account...");
            email = handleAddNewCustomer();
        } else{
            Customer customer = null;
            do{
                email = Helpers.emailPrompt();
                customer = hotelResource.getCustomer(email);
                if(customer == null){
                    System.out.println("Email does not exist, please try again");
                }
            }while(customer == null);
        }

        IRoom targetRoom = null;
        do{
            System.out.println("Which room would you like to book? (Enter room number)");
            String roomNumber = scanner.nextLine();
            for (IRoom room : availableRooms) {
                if (room.getRoomNumber().equals(roomNumber)) {
                    targetRoom = room;
                }
            }

            if(targetRoom == null){
                System.out.println("Invalid room number. Please select a an existing room");
            }
        }while(targetRoom == null);

        Reservation newReservation = hotelResource.bookARoom(email, targetRoom, checkInDate, checkOutDate);
        System.out.println(newReservation);
    }


}
