package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;

public class HotelResource {

    private static final CustomerService customerService = CustomerService.getInstance();
    private static final ReservationService reservationService = ReservationService.getInstance();
    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName){
        customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber){
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date CheckOutDate){
        Customer targetCustomer = customerService.getCustomer(customerEmail);
        return reservationService.reserveARoom(targetCustomer,room, checkInDate, CheckOutDate );
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail){
        Customer targetCustomer = customerService.getCustomer(customerEmail);
        return reservationService.getCustomersReservation(targetCustomer);
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut, boolean shouldDisplayFilter){
        return reservationService.findRooms(checkIn, checkOut, shouldDisplayFilter);
    }
}
