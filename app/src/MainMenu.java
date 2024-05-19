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
    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    public static void mainMenu() {
        String line = "";
        Scanner scanner = new Scanner(System.in);

        printMainMenu();

        try {
            do {
                line = scanner.nextLine();

                if (line.length() == 1) {
                    switch (line.charAt(0)) {
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
                            System.out.println("Unknown action\n");
                            break;
                    }
                } else {
                    System.out.println("Error: Invalid action\n");
                }
            } while (line.charAt(0) != '5' || line.length() != 1);
        } catch (StringIndexOutOfBoundsException ex) {
            System.out.println("Empty input received. Exiting program...");
        }
    }

    public static void printMainMenu()
    {
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
            MainMenu.mainMenu();
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
        Customer customer = hotelResource.getCustomer(customerEmail);
        if (customer == null) {
            System.out.println("No customer found for the email address: " + customerEmail);
            MainMenu.mainMenu();
        }

        System.out.println("Enter check-in date (mm/dd/yyyy):");
        Date checkInDate = getInputDate();
        System.out.println("Enter check-out date (mm/dd/yyyy):");
        Date checkOutDate = getInputDate();
        if (checkInDate != null || checkOutDate != null) {
            Collection<IRoom> rooms = hotelResource.findARoom(checkInDate, checkOutDate);
            if (rooms != null && !rooms.isEmpty()) {
                System.out.println("Available rooms:");
                for (IRoom room : rooms) {
                    System.out.println(room);
                }
                System.out.println("Do you want to reserve a room? (y/n)");
                String reserve = scanner.nextLine();
                if (reserve.equalsIgnoreCase("y")) {
                    System.out.println("Enter room number:");
                    String roomNumber = scanner.nextLine();
                    hotelResource.bookARoom(customerEmail, hotelResource.getRoom(roomNumber), checkInDate, checkOutDate);
                    System.out.println("Room has been booked successfully");
                    MainMenu.mainMenu();
                }
            } else {
                System.out.println("No rooms available for the selected dates.");
            }
        }
    }

    private static Date getInputDate(){
        try {
            return new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(scanner.nextLine());
        } catch (ParseException ex) {
            System.out.println("Error: Invalid date.");
            findAndReserveRoom();
        }
        return null;
    }

    private static void seeMyReservations() {
        System.out.println("Enter your email address:");
        String email = scanner.nextLine();
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
        MainMenu.mainMenu();
    }

}
