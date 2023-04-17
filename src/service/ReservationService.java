package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

import java.util.*;

public class ReservationService {
    private static final Map<String, IRoom> rooms = new HashMap<>();
    private static final Collection<Reservation> reservations = new ArrayList<>();

    public void addRoom(IRoom room){
        rooms.put(room.getRoomNumber(), room);
    }
    public void addMultipleRooms(List<IRoom> rooms){
        for(IRoom room:rooms){
            ReservationService.rooms.put(room.getRoomNumber(), room);
        }
    }

    public IRoom getARoom(String roomNumber){
        return rooms.get(roomNumber);
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate){
        Reservation newReservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(newReservation);
        return newReservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate){
        System.out.println("Searching for a room between " + checkInDate + " & " + checkOutDate + "...");
        Collection<IRoom> availableRooms = new ArrayList<>();
        for(IRoom room: rooms.values()){
            boolean isAvailable = true;
            for(Reservation reservation:reservations){
                  if(
                      room.getRoomNumber().equals(room.getRoomNumber()) &&
                       !(
                           checkOutDate.before(reservation.getCheckInDate()) ||
                           checkInDate.after(reservation.getCheckOutDate())
                       )
                  ){
                      isAvailable = false;
                      break;
                  }
            }
            if(isAvailable){
                availableRooms.add(room);
            }
        }
        if(availableRooms.size() == 0 ){
            System.out.println("No rooms are available in the date provided");
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
