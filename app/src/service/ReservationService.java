package service;

import model.customer.Customer;
import model.room.IRoom;
import model.reservation.Reservation;

import java.util.*;

public class ReservationService {
    private static final ReservationService instance = new ReservationService();
    private final Map<String, List<Reservation>> reservations = new HashMap<>();
    private final Map<String, IRoom> rooms = new HashMap<>();

    public static ReservationService getInstance() {
        return instance;
    }

    public void addRoom(IRoom room){
        rooms.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId){
        return rooms.get(roomId);
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        List<IRoom> availableRooms = new ArrayList<>(rooms.values());

        for (List<Reservation> reservationsList : reservations.values()) {
            for (Reservation reservation : reservationsList) {
                if ((checkInDate.before(reservation.getCheckOutDate()) && checkOutDate.after(reservation.getCheckInDate()))) {
                    availableRooms.remove(reservation.getRoom());
                }
            }
        }

        return availableRooms;
    }

    public Reservation reserveARoom(Customer customer,
                                    IRoom room,
                                    Date checkInDate,
                                    Date checkOutDate){
        Collection<IRoom> availableRooms = findRooms(checkInDate, checkOutDate);
        if (!availableRooms.contains(room)) {
            throw new IllegalArgumentException("Room is not available for the selected dates.");
        }

        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        List<Reservation> customerReservations = getCustomerReservation(customer.getEmail());
        if (customerReservations == null){
            customerReservations = new ArrayList<>();
        }
        customerReservations.add(reservation);
        reservations.put(customer.getEmail(), customerReservations);

        return reservation;
    }

    public List<Reservation> getCustomerReservation(String customerEmail){
        return reservations.get(customerEmail);
    }

    public void printAllReservation(){
        for (List<Reservation> reservations: reservations.values()){
            for (Reservation reservation: reservations){
                System.out.println(reservation);
            }
        }
    }

    public Collection<IRoom> getRooms(){
        return rooms.values();
    }
}
