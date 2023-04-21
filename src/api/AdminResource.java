package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {

    private static final CustomerService customerService = CustomerService.getInstance();
    private static final ReservationService reservationService =  ReservationService.getInstance();
    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }

    // Kindly note that I think this should accept only IRoom instead of List<IRoom>, this way prevented me to create single rooms using the resource.
    public void addRoom(List<IRoom> rooms){
        for(IRoom room:rooms){
            reservationService.addRoom(room);
        }
    }

    public Collection<IRoom> getAllRooms(){
        return reservationService.getAllRooms();
    }

    public Collection<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    public void displayAllReservations(){
        reservationService.printAllReservations();;
    }

    public IRoom getRoom(String roomNumber){
        return reservationService.getARoom(roomNumber);
    }

}
