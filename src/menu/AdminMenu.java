package menu;

import api.AdminResource;
import model.Customer;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final Scanner scanner;

    public AdminMenu(){
        scanner = new Scanner(System.in);
    }

    public void initAdminMenu(){
        int choice = 0;
        do{
            System.out.println("Admin Menu");
            System.out.println("\n----------------------------");
            System.out.println("1. See all customers");
            System.out.println("2. See all rooms");
            System.out.println("3. See all reservations");
            System.out.println("4. Add a room");
            System.out.println("5. Back to main menu");
            System.out.println("\n----------------------------");
            System.out.println("Please select a number for the menu option");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice){
                case 1:{
                    handleDisplayAllCustomers();
                    break;
                }
                case 2:{
                    handleDisplayAllRooms();
                    break;
                }
                case 3:{
                    handleDisplayAllReservations();
                    break;
                }
                case 4:{
                    handleAddRooms();
                    break;
                }

                case 5:{
                    System.out.println("Going back..");
                    return;
                }

                default:{
                    System.out.println("Invalid choice, please try again.");
                    break;
                }
            }
        }while(choice!= 5);
    }

    private void handleDisplayAllRooms(){
        AdminResource adminResource = new AdminResource();
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
        AdminResource adminResource = new AdminResource();
        Collection<Customer> totalCustomers =  adminResource.getAllCustomers();
        if(totalCustomers.size() == 0 ){
            System.out.println("We have no customers yet! Go back to the main menu to create new customers.");
            return;
        }
        System.out.println("We have the following customers registered in our Hotel: \n");
        for(Customer customer:totalCustomers){
            System.out.println(customer);
        }
    }

    private void handleDisplayAllReservations(){
        AdminResource adminResource = new AdminResource();
        adminResource.displayAllReservations();
    }

    private void handleAddRooms(){
        List<IRoom> currentRooms = new ArrayList<>();
        char addRoomChoice;
        AdminResource adminResource = new AdminResource();
        do{
            currentRooms.add(createNewRoom());
            while (true) {
                System.out.println("Do you want to add more rooms? y/n");
                String input = scanner.next().toLowerCase();
                if (input.equals("y")) {
                    addRoomChoice = 'y';
                    break;
                } else if (input.equals("n")) {
                    addRoomChoice = 'n';
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'y' (Yes) or 'n' (No)");
                }
            }
            adminResource.addRoom(currentRooms);
        }while(addRoomChoice!='n');
    }

    private IRoom createNewRoom(){
        System.out.println("Enter room number");
        String roomNumber = scanner.next();
        scanner.nextLine();

        AdminResource adminResource = new AdminResource();
        IRoom existingRoom = null;
        existingRoom = adminResource.getRoom(roomNumber);
        if(existingRoom != null){
            System.out.println("Room " + roomNumber + " already exists, please choose another room number.");
            return createNewRoom();
        }

        System.out.println("Enter price per night");
        double price = scanner.nextDouble();

        String roomType;
        do {
            System.out.println("Enter room type: \n1 for single bed\n2 for double bed");
            roomType = scanner.next();
            if(!roomType.equals("1") && !roomType.equals("2")){
                System.out.println("Please enter 1 for single bed or 2 for double bed");
            }
        } while (!roomType.equals("1") && !roomType.equals("2"));
        //TODO: Check if this needs to be changed
        return new Room(roomNumber, roomType.equals("1") ? RoomType.SINGLE : RoomType.DOUBLE, true, price);
    }

}
