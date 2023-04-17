package menu;

import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class MainMenu {
    private final Scanner scanner;

    public MainMenu(){
        scanner = new Scanner(System.in);
    }

    public void initMainMenu(){
        int choice = 0;
        do{
            System.out.println("Welcome to the Hotel Reservation Application");
            System.out.println("\n----------------------------");
            System.out.println("1. Find and reserve a room");
            System.out.println("2. See my reservations");
            System.out.println("3. Create an account");
            System.out.println("4. Admin");
            System.out.println("5. Exit");
            choice = this.scanner.nextInt();
            scanner.nextLine();
            switch (choice){
                case 1:{
                    handleFindAndReserveRoom();
                    break;
                }
                case 2:{
                    handleGetCustomerReservations();
                    break;
                }
                case 3:{
                    handleAddNewCustomer();
                    break;
                }
                case 4:
                    startAdminMenu();
                    break;

                default:
                    System.out.println("Exiting...");
                    break;
            }
        }while(choice!= 5);
    }

    public void startAdminMenu(){
        AdminMenu adminMenu = new AdminMenu();
        adminMenu.initAdminMenu();
    }

    public String handleAddNewCustomer(){
        System.out.println("Enter your email address(format should be as such: name@domain.com)");
        String email = scanner.nextLine();

        System.out.println("Enter your first name");
        String firstName = scanner.nextLine();

        System.out.println("Enter your last name");
        String lastName = scanner.nextLine();

        HotelResource hotelResource = new HotelResource();
        hotelResource.createACustomer(email, firstName, lastName);

        return email;
    }

    public void handleGetCustomerReservations(){
        HotelResource hotelResource = new HotelResource();
        System.out.println("Enter your email address(format should be as such: name@domain.com)");
        String email = scanner.nextLine();
        Collection<Reservation> totalReservations =  hotelResource.getCustomersReservations(email);
        System.out.println(email + " has the following reservations: \n"+totalReservations);
    }

    public void handleFindAndReserveRoom(){
        Date checkInDate;
        Date checkOutDate;
        String checkInDateStr;
        String checkOutDateStr;
        boolean isDateValid = false;
        String dateRegex = "^\\d{2}/\\d{2}/\\d{4}$"; // regex for mm/dd/yyyy format
        do {
            System.out.println("Enter the check-in date in the format of mm/dd/yyyy(for example, 04/16/2023");
            checkInDateStr = scanner.nextLine();

            System.out.println("Enter the check-out date in the format of mm/dd/yyyy(for example, 04/16/2023");
            checkOutDateStr = scanner.nextLine();
            isDateValid = checkInDateStr.matches(dateRegex) && checkOutDateStr.matches(dateRegex);

            if (!isDateValid) {
                System.out.println("Invalid date format. Please try again.");
            }
        } while (!isDateValid);
        DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        try {
            checkInDate = inputFormat.parse(checkInDateStr);
            checkOutDate = inputFormat.parse(checkOutDateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        HotelResource hotelResource = new HotelResource();
        Collection<IRoom> availableRooms = hotelResource.findARoom(checkInDate, checkOutDate);
        System.out.println(availableRooms);

        System.out.println("Do you have an account with us? y/n");
        char answer = scanner.next().charAt(0);
        String email;
        if(answer == 'n' ){
            System.out.println("Creating a new account...");
            email = handleAddNewCustomer();
        }else{
            System.out.println("Enter your email address(format should be as such: name@domain.com)");
            email = scanner.nextLine();
        }

        System.out.println("Which room would you like to reserve (Enter room number)?");
        String roomNumber = scanner.nextLine();

        IRoom room = hotelResource.getRoom(roomNumber);
        Reservation newReservation = hotelResource.bookARoom(email, room, checkInDate, checkOutDate);
    }

}
