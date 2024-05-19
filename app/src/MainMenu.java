import api.HotelResource;
import model.customer.Customer;
import model.reservation.Reservation;
import model.room.IRoom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MainMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    public static void main(String[] args) {
        mainMenu();
    }

    public static void mainMenu() {
        String option;
        do {
            printMainMenu();
            option = scanner.nextLine();
            if (option.length() == 1) {
                switch (option.charAt(0)) {
                    case '1':
                        findAndReserveRoom();
                        break;
                    case '2':
                        seeMyReservations();
                        break;
                    case '3':
                        createAccount();
                        break;
                    case '4':
                        AdminMenu.adminMenu();
                        break;
                    case '5':
                        System.out.println("Exit");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } else {
                System.out.println("Error: Invalid action\n");
            }
        } while (!option.equals("5"));
    }

    public static void printMainMenu() {
        System.out.print("\nWelcome to the Hotel Reservation Application\n" +
                "--------------------------------------------\n" +
                "1. Find and reserve a room\n" +
                "2. See my reservations\n" +
                "3. Create an Account\n" +
                "4. Admin\n" +
                "5. Exit\n" +
                "--------------------------------------------\n" +
                "Please select a number for the menu option:\n");
    }

    private static void createAccount() {
        try {
            System.out.println("Enter your email address:");
            String email = scanner.nextLine();
            while (!isValidEmail(email)) {
                System.out.println("Invalid email address. Please enter a valid email address:");
                email = scanner.nextLine();
            }
            System.out.println("Enter your first name:");
            String firstName = scanner.nextLine();
            System.out.println("Enter your last name:");
            String lastName = scanner.nextLine();
            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Account created successfully!");
        } catch (Exception e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
    }

    private static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private static void findAndReserveRoom() {
        System.out.println("Enter your email address:");
        String customerEmail = scanner.nextLine();
        while (!isValidEmail(customerEmail)) {
            System.out.println("Invalid email address. Please enter a valid email address:");
            customerEmail = scanner.nextLine();
        }
        Customer customer = hotelResource.getCustomer(customerEmail);
        if (customer == null) {
            System.out.println("No customer found for the email address: " + customerEmail);
            return;
        }
        Date checkInDate = getCheckInDate();
        Date checkOutDate = getCheckOutDate();
        while (checkOutDate.before(checkInDate)) {
            System.out.println("Check-out date cannot be before check-in date.");
            checkInDate = getCheckInDate();
            checkOutDate = getCheckOutDate();
        }

        Collection<IRoom> rooms = hotelResource.findARoom(checkInDate, checkOutDate);
        if (rooms != null && !rooms.isEmpty()) {
            System.out.println("Available rooms:");
            for (IRoom room : rooms) {
                System.out.println(room);
            }
            System.out.println("Do you want to reserve a room? (y/n)");
            String reserve = scanner.nextLine();
            if (reserve.equalsIgnoreCase("y")) {
                reserveRoom(customerEmail, checkInDate, checkOutDate);
            } else {
                System.out.println("Reservation cancelled.");
            }
        } else {
            System.out.println("No rooms available for the selected dates.");
        }
    }

    private static void reserveRoom(String customerEmail, Date checkInDate, Date checkOutDate) {
        System.out.println("Enter room number:");
        String roomNumber = scanner.nextLine();
        IRoom room = hotelResource.getRoom(roomNumber);
        while (room == null) {
            System.out.println("Invalid room number. Please enter a valid room number:");
            roomNumber = scanner.nextLine();
            room = hotelResource.getRoom(roomNumber);
        }
        hotelResource.bookARoom(customerEmail, room, checkInDate, checkOutDate);
        System.out.println("Room has been booked successfully");
    }

    private static Date getCheckInDate() {
        System.out.println("Enter check-in date (MM/dd/yyyy):");
        return getInputDate();
    }

    private static Date getCheckOutDate() {
        System.out.println("Enter check-out date (MM/dd/yyyy):");
        return getInputDate();
    }

    private static Date getInputDate() {
        while (true) {
            try {
                String dateStr = scanner.nextLine();
                return new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(dateStr);
            } catch (ParseException ex) {
                System.out.println("Invalid date format. Please enter the date in MM/dd/yyyy format:");
            }
        }
    }

    private static void seeMyReservations() {
        System.out.println("Enter your email address:");
        String email = scanner.nextLine();
        while (!isValidEmail(email)) {
            System.out.println("Invalid email address. Please enter a valid email address:");
            email = scanner.nextLine();
        }
        Customer customer = hotelResource.getCustomer(email);
        if (customer == null) {
            System.out.println("No reservations found for the email address: " + email);
        } else {
            Collection<Reservation> reservations = hotelResource.getCustomerReservations(email);
            if (reservations.isEmpty()) {
                System.out.println("No reservations found for the email address: " + email);
            } else {
                System.out.println("Reservations for the email address: " + email);
                for (Reservation reservation : reservations) {
                    System.out.println(reservation);
                }
            }
        }
    }
}
