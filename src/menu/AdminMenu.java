package menu;

import api.AdminResource;
import model.*;
import utils.Helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AdminResource adminResource = new AdminResource();

    public void initAdminMenu(){
        boolean running = true;
        while (running) {
            int choice = getUserInput();
            AdminMenuQuestionType option = AdminMenuQuestionType.fromValue(choice);
            switch (option){
                case SEE_ALL_CUSTOMERS -> handleDisplayAllCustomers();
                case SEE_ALL_ROOMS ->handleDisplayAllRooms();
                case SEE_ALL_RESERVATIONS-> handleDisplayAllReservations();
                case ADD_ROOM->handleAddRooms();
                case TEST_DATA->startTestDataMenu();
                case BACK_TO_MAIN_MENU->running = false;
                default-> System.out.println("Invalid choice, please try again.");
            }
        }

    }

    public void startTestDataMenu(){
        TestDataMenu testDataMenu = new TestDataMenu();
        testDataMenu.initMenu();
    }

    private int getUserInput(){
        int choice;
        int minOption = 1;
        int maxOption = AdminMenuQuestionType.values().length;
        boolean validInput = true;
        do {
            AdminMenuQuestionType.printMenuHeader();
            for (AdminMenuQuestionType option : AdminMenuQuestionType.values()) {
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

    private void handleDisplayAllRooms(){
        Collection<IRoom> totalRooms =  adminResource.getAllRooms();
        if(totalRooms.size() == 0){
            System.out.println("Our hotel does not have any rooms yet! Press 4 to add new rooms");
            return;
        }
        System.out.println("We have the following room(s) in our Hotel: \n");
        for(IRoom room:totalRooms){
            System.out.println(room);
        }
    }

    private void handleDisplayAllCustomers(){
        Collection<Customer> totalCustomers =  adminResource.getAllCustomers();
        if(totalCustomers.size() == 0 ){
            System.out.println("We have no customers yet! Go to the main menu to create new customers.");
            return;
        }
        System.out.println("We have the following customers registered in our Hotel: \n");
        for(Customer customer:totalCustomers){
            System.out.println(customer);
        }
    }

    private void handleDisplayAllReservations(){
        adminResource.displayAllReservations();
    }

    private void handleAddRooms(){
        List<IRoom> currentRooms = new ArrayList<>();
        boolean shouldAddMoreRooms = true;
        while (shouldAddMoreRooms){
            currentRooms.add(createNewRoom());
            adminResource.addRoom(currentRooms);
            shouldAddMoreRooms = Helpers.readYesNo("Do you want to add more rooms?");
            scanner.nextLine();
        }
    }

    private IRoom createNewRoom(){
        System.out.println("Enter room number");
        String roomNumber = scanner.next();
        scanner.nextLine();

        IRoom existingRoom = null;
        existingRoom = adminResource.getRoom(roomNumber);
        if(existingRoom != null){
            System.out.println("Room " + roomNumber + " already exists, please choose another room number.");
            return createNewRoom();
        }

        System.out.println("Enter price per night (enter 0 if you want to make it for free)");
        double price = scanner.nextDouble();

        String roomType;
        do {
            System.out.println("Enter room type: \n1 for single bed\n2 for double bed");
            roomType = scanner.next();
            if(!roomType.equals("1") && !roomType.equals("2")){
                System.out.println("Please enter 1 for single bed or 2 for double bed");
            }
        } while (!roomType.equals("1") && !roomType.equals("2"));
        boolean isFree = price == 0;
        RoomType finalRoomType = roomType.equals("1") ? RoomType.SINGLE : RoomType.DOUBLE;
        IRoom room;
        if(isFree){
            room = new FreeRoom(roomNumber, finalRoomType, true);
        }else{
            room = new Room(roomNumber, finalRoomType, false, price);
        }
        return room;
    }

}
