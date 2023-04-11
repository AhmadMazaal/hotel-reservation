package service;

import model.Customer;

import java.util.Collection;

public class  CustomerService {

    private Collection<Customer> customers;
    public void addCustomer(String firstName, String lastName, String email){
        Customer customer = new Customer("First", "Second", "as.com");
        this.customers.add(customer);
    }

    public Customer getCustomer(String email){
        Customer foundCustomer = null;
        for(Customer customer:this.customers){
            if(customer.getEmail().equals(email)){
                foundCustomer = customer;
                break;
            }
        }
        return foundCustomer;
    }

    public Collection<Customer> getAllCustomers(){
        return this.customers;
    }


}
