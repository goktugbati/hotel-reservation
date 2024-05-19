package api;

import model.customer.Customer;
import model.room.IRoom;
import model.reservation.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;

public class HotelResource {
    private static final HotelResource instance = new HotelResource();

    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    public static HotelResource getInstance() {
        return instance;
    }

    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName) {
        customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber){
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail,
                                 IRoom room,
                                 Date checkInDate,
                                 Date checkOutDate){
        Customer customer = customerService.getCustomer(customerEmail);
        if (customer == null){
            throw new IllegalArgumentException("Invalid email format: " + customerEmail);
        }
        return reservationService.reserveARoom(customer, room, checkInDate, checkOutDate);
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut){
        return reservationService.findRooms(checkIn, checkOut);
    }

    public Collection<Reservation> getCustomerReservations(String customerEmail){
        return reservationService.getCustomerReservation(customerEmail);
    }

}
