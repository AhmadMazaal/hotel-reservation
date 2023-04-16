package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {

    private static final CustomerService customerService = new CustomerService();
    private static final ReservationService reservationService = new ReservationService();
    public Customer getCustomer(String email){
        return this.customerService.getCustomer(email);
    }

//    public void addRoom(List<IRoom> rooms){
//        this.reservationService.addRoom();
//    }

    public Collection<IRoom> getAllRooms(){
        return this.reservationService.getAllRooms();
    }

    public Collection<Customer> getAllCustomers(){
        return this.customerService.getAllCustomers();
    }

    public void displayAllReservations(){
        this.reservationService.printAllReservations();;
    }
}
