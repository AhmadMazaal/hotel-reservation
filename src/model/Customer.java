package model;

import utils.Helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Customer {

    private String firstName;
    private String lastName;
    private String email;

    public Customer(String email, String firstName, String lastName){
        try{
            if(Helpers.isInvalidEmail(email)){
                throw new IllegalArgumentException("Wrong email format");
            }
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }catch(IllegalArgumentException exception){
            System.out.println("Error creating a new customer: "  +exception.getLocalizedMessage());
        }

    }

    public final String getFirstName() {
        return this.firstName;
    }

    public final void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public final String getLastName() {
        return this.lastName;
    }

    public final void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public final String getEmail() {
        return this.email;
    }

    public final void setEmail(String email) {
        try {
            if(Helpers.isInvalidEmail(email)){
                throw new IllegalArgumentException("Wrong email format");
            }
            this.email = email;
        }catch(IllegalArgumentException exception){
            System.out.println("Error creating a new customer: "  +exception.getLocalizedMessage());
        }
    }

    @Override
    public String toString() {
        return "Customer: " +
                "firstName: " + this.firstName +
                ", lastName: " + this.lastName +
                ", email: " + this.email;
    }
}