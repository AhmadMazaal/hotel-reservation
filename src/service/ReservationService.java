package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import utils.Helpers;

import java.time.temporal.ChronoUnit;
import java.util.*;

public class ReservationService {
    private static final Map<String, IRoom> rooms = new HashMap<>();
    private static final Collection<Reservation> reservations = new ArrayList<>();

    private static ReservationService RESERVATION_INSTANCE;

    public static ReservationService getInstance() {
        if(RESERVATION_INSTANCE == null) {
            RESERVATION_INSTANCE = new ReservationService();
        }
        return RESERVATION_INSTANCE;
    }


    public void addRoom(IRoom room){
        rooms.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomNumber){
        return rooms.get(roomNumber);
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate){
        Reservation newReservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(newReservation);
        return newReservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate, boolean shouldDisplayFilter){
        boolean isFreeRoomFilterEnabled = false;
        if(shouldDisplayFilter){
            char searchCriteria = Helpers.readChoices("Enter search criteria: \n1 for free rooms\n2 for paid rooms");
            isFreeRoomFilterEnabled = searchCriteria == '1';
        }

        String formattedCheckInDate = Helpers.formatDate(checkInDate);
        String formattedCheckOutDate = Helpers.formatDate(checkOutDate);

        System.out.println("Searching for available " + "rooms between " + formattedCheckInDate + " & " + formattedCheckOutDate + "...");
        return findAvailableRooms(checkInDate, checkOutDate, isFreeRoomFilterEnabled);

    }

    private Collection<IRoom> findAvailableRooms(Date checkInDate, Date checkOutDate, boolean isFreeRoomFilterEnabled){

        Collection<IRoom> availableRooms = new ArrayList<>();

        for(IRoom room: rooms.values()){
            boolean isAvailable = true;

            if (!isFreeRoomFilterEnabled) {
                if (room.isFree()) {
                    isAvailable = false;
                }else{
                    for(Reservation reservation:reservations){

                        if(
                                room.getRoomNumber().equals(reservation.getRoom().getRoomNumber()) &&
                                        !(checkOutDate.before(reservation.getCheckInDate()) || checkInDate.after(reservation.getCheckOutDate()))){
                            isAvailable = false;
                            break;
                        }
                    }
                }

            } else {
                if (!room.isFree()) {
                    isAvailable = false;
                } else {
                    for(Reservation reservation:reservations){
                        if(room.getRoomNumber().equals(reservation.getRoom().getRoomNumber()) &&
                                !(checkOutDate.before(reservation.getCheckInDate()) || checkInDate.after(reservation.getCheckOutDate()))){
                            isAvailable = false;
                            break;
                        }
                    }
                }
            }

            if(isAvailable){
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }


    public Collection<Reservation> getCustomersReservation(Customer customer){
        Collection<Reservation> customerReservations = new ArrayList<>();
        for(Reservation reservation: reservations){
            if(reservation.getCustomer().equals(customer)){
                customerReservations.add(reservation);
            }
        }
        return customerReservations;
    }

    public void printAllReservations(){
        if(reservations.size() == 0 ){
            System.out.println("We have no reservations yet! Go back to the main menu to create new reservations.");
            return;
        }

        System.out.println("Total reservations we have in our Hotel: ");
        for(Reservation reservation: reservations){
            System.out.println(reservation);
        }
    }

    public Collection<IRoom> getAllRooms(){
        return rooms.values();
    }
}
