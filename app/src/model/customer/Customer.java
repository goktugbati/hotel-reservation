package model.customer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Customer {
    private final String firstName;
    private final String lastName;
    private String email;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public Customer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        setEmail(email);
    }

    private boolean isValidEmail(String email){
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (isValidEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

    @Override
    public String toString() {
        return "First Name: " + this.firstName
                + " Last Name: " + this.lastName
                + " Email: " + this.email;
    }
}
