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
import java.util.*;

public class MainMenu {
    private final Scanner scanner;
    private int queryAttempts;

    public MainMenu(){
        this.scanner = new Scanner(System.in);
        this.queryAttempts = 0;
    }

    public void initMainMenu(){
        int choice;
        do{
            System.out.println("*****************************************************************************");
            System.out.println("**\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t   **");
            System.out.println("**\t\t\t\tWelcome to the Hotel Reservation Application\t\t\t   **");
            System.out.println("**\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t   **");
            System.out.println("*****************************************************************************");
            for (MainMenuQuestionType option : MainMenuQuestionType.values()) {
                System.out.println(option.getValue() + ". " + option.getQuestion());
            }
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input, please enter a number");
                scanner.next();
            }
            choice = this.scanner.nextInt();
            scanner.nextLine();
            MainMenuQuestionType option = MainMenuQuestionType.fromValue(choice);
            switch (option){
                case FIND_AND_RESERVE_ROOM:
                    handleFindAndReserveRoom();
                    break;
                case SEE_MY_RESERVATIONS:
                    handleGetCustomerReservations();
                    break;
                case CREATE_AN_ACCOUNT:
                    handleAddNewCustomer();
                    break;
                case ADMIN:
                    startAdminMenu();
                    break;
                case EXIT:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }while(choice!= 5);
    }

    public void startAdminMenu(){
        AdminMenu adminMenu = new AdminMenu();
        adminMenu.initAdminMenu();
    }

    public String handleAddNewCustomer(){
        String email = "";
        try{
            System.out.println("Enter your email address(format should be as such: name@domain.com)");
             email = scanner.nextLine();
            if(Helpers.isInvalidEmail(email)){
                throw new IllegalArgumentException("Invalid email address");
            }
        }catch(IllegalArgumentException e){
            System.out.println(e.getLocalizedMessage());
        }

        System.out.println("Enter your first name");
        String firstName = scanner.nextLine();

        System.out.println("Enter your last name");
        String lastName = scanner.nextLine();

        HotelResource hotelResource = new HotelResource();
        hotelResource.createACustomer(email, firstName, lastName);

        return email;
    }

    public void handleGetCustomerReservations(){
        try{
            HotelResource hotelResource = new HotelResource();
            System.out.println("Enter your email address(format should be as such: name@domain.com)");
            String email = scanner.nextLine();
            if(Helpers.isInvalidEmail(email)){
                throw new IllegalArgumentException("Invalid email format");
            }
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
        }catch(Exception e){
            System.out.println(e.getLocalizedMessage());
        }

    }

    public void handleFindAndReserveRoom() {
        Helpers helpers = new Helpers();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy").withZone(ZoneId.of("UTC"));
        LocalDate checkInLocalDate = null, checkOutLocalDate = null;
        Collection<IRoom> availableRooms;

        do {
            System.out.println("Enter the check-in date in the format of mm/dd/yyyy (for example, 04/16/2023)");
            String checkInDateStr = scanner.nextLine();

            if (isDateInvalid(checkInDateStr)) {
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

            if (isDateInvalid(checkOutDateStr)) {
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

        HotelResource hotelResource = new HotelResource();
        ZoneId zoneId = ZoneId.of("UTC");
        Date checkInDate = Date.from(checkInLocalDate.atStartOfDay(zoneId).toInstant());
        Date checkOutDate = Date.from(checkOutLocalDate.atStartOfDay(zoneId).toInstant());
        availableRooms = hotelResource.findARoom(checkInDate, checkOutDate);

        if(availableRooms.size() == 0){
            this.queryAttempts++;
            if(this.queryAttempts == 3){
                System.out.println("Too many attempts, please contact the admin to find an available room in your specified dates.");
                return;
            }
            System.out.println("No rooms are available in the date provided, please try another date");
            handleFindAndReserveRoom();
            return;
        }
        System.out.println("We have the following rooms available in the specified date:");
        System.out.println("---------------");
        for(IRoom room:availableRooms){
            System.out.println(room);
        }
        System.out.println("---------------\n");

        char shouldReserveRoom = helpers.readYesNo("Would you like to reserve a room? y/n");
        if(shouldReserveRoom == 'n'){
            return;
        }

        String email;
        char shouldCreateAccount = helpers.readYesNo("Do you have an account with us? y/n");
        if(shouldCreateAccount == 'n'){
            System.out.println("Creating a new account...");
            email = handleAddNewCustomer();
        } else{
            Customer customer = null;
            boolean isInvalidValidEmail = false;
            do{
                System.out.println("Enter your email address(format should be as such: name@domain.com)");
                email = scanner.nextLine();
                if(Helpers.isInvalidEmail(email)){
                    System.out.println("invalid email format");
                    isInvalidValidEmail = true;
                    continue;
                }
                customer = hotelResource.getCustomer(email);
                if(customer == null){
                    System.out.println("Email does not exist, please try again");
                }
            }while(customer == null || isInvalidValidEmail);
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

    private boolean isDateInvalid(String dateStr){
        String dateRegex = "^\\d{2}/\\d{2}/\\d{4}$";
        if (!dateStr.matches(dateRegex)) {
            System.out.println("Invalid date format, please enter in the format of mm/dd/yyyy");
            return true;
        }
        return false;
    }


}
