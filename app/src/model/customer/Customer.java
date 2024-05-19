package model.customer;
import java.util.regex.Pattern;

public class Customer {
    private static final String EMAIL_REGEX = "^(.+)@(.+).(.+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    private final String firstName;
    private final String lastName;
    private final String email;

    public Customer(String firstName, String lastName, String email) {
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "First Name: " + this.firstName
                + " Last Name: " + this.lastName
                + " Email: " + this.email;
    }
}
