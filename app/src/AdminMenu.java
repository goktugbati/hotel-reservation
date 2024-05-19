import api.AdminResource;
import model.customer.Customer;
import model.room.IRoom;
import model.room.Room;
import model.room.RoomType;

import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class AdminMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AdminResource adminResource = AdminResource.getInstance();

    public static void adminMenu() {
        String line;
        do {
            printMenu();
            line = scanner.nextLine();
            handleUserInput(line);
        } while (!line.equals("5"));
    }

    private static void printMenu() {
        System.out.print("\nAdmin Menu\n" +
                "--------------------------------------------\n" +
                "1. See all Customers\n" +
                "2. See all Rooms\n" +
                "3. See all Reservations\n" +
                "4. Add a Room\n" +
                "5. Back to Main Menu\n" +
                "--------------------------------------------\n" +
                "Please select a number for the menu option:\n");
    }

    private static void handleUserInput(String line) {
        try {
            switch (line) {
                case "1":
                    seeAllCustomers();
                    break;
                case "2":
                    seeAllRooms();
                    break;
                case "3":
                    seeAllReservations();
                    break;
                case "4":
                    addARoom();
                    break;
                case "5":
                    System.out.println("Returning to Main Menu...");
                    MainMenu.mainMenu();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void seeAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();
        if (customers.isEmpty()){
            System.out.println("No customer found.");
            return;
        }
        customers.forEach(System.out::println);
    }

    private static void seeAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();
        if (rooms.isEmpty()){
            System.out.println("No rooms found.");
            return;
        }
        rooms.forEach(room ->System.out.println(room.toString()));
    }

    private static void seeAllReservations() {
        adminResource.displayAllReservations();
    }

    private static void addARoom() {
        try {
            String roomNumber = getRoomNumber();
            double price = getRoomPrice();
            RoomType roomType = getRoomType();

            Room room = new Room(roomNumber, price, roomType);
            adminResource.addRoom(Collections.singletonList(room));
            System.out.println("Room added successfully!");

            handleAddAnotherRoom();
        } catch (Exception e) {
            System.out.println("Error adding room: " + e.getMessage());
        }
    }

    private static String getRoomNumber() {
        String roomNumber;
        do {
            System.out.println("Enter room number:");
            roomNumber = scanner.nextLine();
            if (roomNumber.isEmpty()) {
                System.out.println("Room number cannot be empty. Please enter a valid room number.");
            } else {
                try {
                    Integer.parseInt(roomNumber);
                } catch (NumberFormatException e) {
                    System.out.println("Room number must be an integer. Please enter a valid room number.");
                    roomNumber = "";
                }
            }
        } while (roomNumber.isEmpty());
        return roomNumber;
    }

    private static double getRoomPrice() {
        double price = 0;
        boolean validPrice = false;
        do {
            try {
                System.out.println("Enter room price:");
                price = Double.parseDouble(scanner.nextLine());
                if (price <= 0) {
                    System.out.println("Price must be a positive number. Please enter a valid price.");
                } else {
                    validPrice = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter a valid number.");
            }
        } while (!validPrice);
        return price;
    }

    private static RoomType getRoomType() {
        int roomTypeInput = 0;
        boolean validRoomType = false;
        do {
            try {
                System.out.println("Enter room type: 1 for single bed, 2 for double bed");
                roomTypeInput = Integer.parseInt(scanner.nextLine());
                if (roomTypeInput == 1 || roomTypeInput == 2) {
                    validRoomType = true;
                } else {
                    System.out.println("Invalid input. Please enter 1 for single bed or 2 for double bed.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter a valid number.");
            }
        } while (!validRoomType);
        return roomTypeInput == 1 ? RoomType.SINGLE : RoomType.DOUBLE;
    }

    private static void handleAddAnotherRoom() {
        System.out.println("Would you like to add another room? Y/N");
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("Y")) {
            addARoom();
        } else if (answer.equalsIgnoreCase("N")) {
            System.out.println("Returning to Admin Menu...");
            adminMenu();
        } else {
            System.out.println("Please enter Y (yes) or N (no).");
            handleAddAnotherRoom();
        }
    }

}
