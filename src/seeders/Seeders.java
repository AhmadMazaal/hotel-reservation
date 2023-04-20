package seeders;

import api.AdminResource;
import api.HotelResource;
import model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Seeders {
    private static final Random random = new Random();
    private static final String[] FIRST_NAMES = {"John", "Jane", "Mary", "James", "Sarah", "Michael", "Elizabeth", "David", "Jennifer", "William"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Brown", "Taylor", "Miller", "Davis", "Wilson", "Moore", "Anderson", "Jackson"};
    private static final String[] EMAIL_PROVIDERS = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "aol.com"};
    private static final String[] ROOM_NUMBERS = {"101", "102", "103", "201", "202", "203", "301", "302", "303", "401"};
    private static final RoomType[] ROOM_TYPES = {RoomType.SINGLE, RoomType.DOUBLE};
    private static final double[] ROOM_PRICES = {100, 150, 200, 250, 0};
    private static final int USERS_COUNT = FIRST_NAMES.length;
    private static final int ROOM_COUNT = 10;
    private static final int MAX_RESERVATIONS_PER_CUSTOMER = 3;

    private static final AdminResource adminResource = new AdminResource();
    private static final HotelResource hotelResource = new HotelResource();
    public static void generateCustomers(){
        System.out.println("Generating customers...");
            for(int i=0; i<USERS_COUNT; i++){
                int randomIndex = random.nextInt(EMAIL_PROVIDERS.length);
                String lastName = LAST_NAMES[i];
                String firstName = FIRST_NAMES[i];
                String email = firstName + "_" + lastName +"@"+ EMAIL_PROVIDERS[randomIndex];
                hotelResource.createACustomer(email, firstName, lastName);
            }
        System.out.println("Generated " +USERS_COUNT+ " users successfully." );
    }


    public static void generateRooms() {
        List<IRoom> totalRooms = new ArrayList<>();
        for (int i = 0; i < ROOM_COUNT; i++) {
            RoomType roomType = ROOM_TYPES[i % ROOM_TYPES.length];
            String roomNumber = ROOM_NUMBERS[i % ROOM_NUMBERS.length];
            double price = ROOM_PRICES[i % ROOM_PRICES.length];
            IRoom room;
            if(price == 0.0 ){
                System.out.println("Creating new free room..");
                room = new FreeRoom(roomNumber, roomType, true);
            }else{
                room = new Room(roomNumber, roomType, false, price);
            }

            totalRooms.add(room);
        }
        adminResource.addRoom(totalRooms);
        System.out.println("Generated " +totalRooms.size()+ " rooms successfully." );
    }

    public static void generateReservations() {
        Random random = new Random();
        Collection<Customer> totalCustomers = adminResource.getAllCustomers();
        for (Customer customer :totalCustomers ) {
            int numReservations = random.nextInt(MAX_RESERVATIONS_PER_CUSTOMER) + 1;
            for (int i = 0; i < numReservations; i++) {
                IRoom room = getRandomAvailableRoom();
                if (room != null) {
                    Date checkInDate = generateRandomDate();
                    Date checkOutDate = generateRandomDate();
                    hotelResource.bookARoom(customer.getEmail(), room, checkInDate, checkOutDate);
                }
            }
        }
        System.out.println("Generated " +totalCustomers.size()+ " reservations successfully." );
    }

    private static IRoom getRandomAvailableRoom() {
        List<IRoom> availableRooms = new ArrayList<>();
        Collection<Reservation> totalReservations = getAllReservationsFromCustomers();;

        for (IRoom room : adminResource.getAllRooms()) {
            boolean isAvailable = true;
            for (Reservation reservation : totalReservations) {
                if (room.getRoomNumber().equals(reservation.getRoom().getRoomNumber()) &&
                        !(reservation.getCheckOutDate().before(new Date()) || reservation.getCheckInDate().after(new Date()))) {
                    isAvailable = false;
                    break;
                }
            }
            if (isAvailable) {
                availableRooms.add(room);
            }
        }
        if (availableRooms.isEmpty()) {
            return null;
        } else {
            return availableRooms.get(new Random().nextInt(availableRooms.size()));
        }
    }

    private static Date generateRandomDate() {
        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        long minDay = LocalDate.of(2023, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2023, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        String formattedDate = randomDate.format(DATE_FORMATTER);
        try {
            return new SimpleDateFormat("MM/dd/yyyy").parse(formattedDate);
        } catch (ParseException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return null;
    }

    private static Collection<Reservation> getAllReservationsFromCustomers(){
        Collection<Reservation> totalReservations = new ArrayList<>();
        Collection<Customer> totalCustomers = adminResource.getAllCustomers();

        for (Customer customer : totalCustomers) {
            Collection<Reservation> customerReservations = hotelResource.getCustomersReservations(customer.getEmail());
            totalReservations.addAll(customerReservations);
        }

        return totalReservations;
    }


    //returns a string
//    private static String generateRandomDate() {
//        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//        long minDay = LocalDate.of(2023, 1, 1).toEpochDay();
//        long maxDay = LocalDate.of(2023, 12, 31).toEpochDay();
//        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
//        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
//        return randomDate.format(DATE_FORMATTER);
//    }

}
