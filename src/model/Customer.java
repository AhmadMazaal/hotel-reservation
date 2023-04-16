package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Customer {

    private String firstName;
    private String lastName;
    private String email;

    public Customer(String firstName, String lastName, String email){
        try{
            if(isInvalidEmail(email)){
                throw new IllegalArgumentException("Wrong email format");
            }
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }catch(IllegalArgumentException exception){
            System.out.println("Error creating a new customer: "  +exception.getLocalizedMessage());
        }

    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        if(isInvalidEmail(email)){
            throw new IllegalArgumentException("Wrong email format");
        }
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    private static boolean  isInvalidEmail(String email){
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return !matcher.find();
    }
}