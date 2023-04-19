package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
        ZoneId zoneId = ZoneId.of("UTC");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy").withZone(zoneId);

        LocalDate checkInLocalDate = checkInDate.toInstant().atZone(zoneId).toLocalDate();
        String formattedCheckInDate = checkInLocalDate.format(formatter);

        LocalDate checkOutLocalDate = checkOutDate.toInstant().atZone(zoneId).toLocalDate();
        String formattedCheckOutDate = checkOutLocalDate.format(formatter);

        System.out.println("Searching for a room between " + formattedCheckInDate + " & " + formattedCheckOutDate + "...");
        Collection<IRoom> availableRooms = new ArrayList<>();
        for(IRoom room: rooms.values()){
            boolean isAvailable = true;
            for(Reservation reservation:reservations){
                  if(
                      room.getRoomNumber().equals(reservation.getRoom().getRoomNumber()) &&
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
