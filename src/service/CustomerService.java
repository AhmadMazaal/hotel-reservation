package service;

import model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class  CustomerService {

    private static CustomerService CUSTOMER_INSTANCE;
    private static final Map<String, Customer> customers = new HashMap<>();

    public static CustomerService getInstance() {
        if(CUSTOMER_INSTANCE == null) {
            CUSTOMER_INSTANCE = new CustomerService();
        }
        return CUSTOMER_INSTANCE;
    }

    public void addCustomer(String email, String firstName, String lastName) {
        Customer customer = new Customer(email, firstName, lastName);
        customers.put(email, customer);
    }

    public Customer getCustomer(String customerEmail) {
        return customers.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }

}
