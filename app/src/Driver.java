import model.customer.Customer;

public class Driver {
    public static void main(String[] args) {
        Customer customer = new Customer("first", "second", "email");
        System.out.println(customer);
    }
}
