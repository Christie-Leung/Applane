package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PassengerTest {

    private Passenger passenger;
    private Flight f1;
    private Flight f2;

    @BeforeEach
    void runBefore() {
        passenger = new Passenger("C", "L", "c", "play", LocalDate.of(2003, 1, 1), "000");
        f1 = new Flight("AC", 123, LocalDateTime.of(2022, 4, 24, 18, 0),
                LocalDateTime.of(2022, 4, 24, 0, 25), "YWR", "YEG",
                300, 200, 80, 20);
        f2 = new Flight("AC", 123, LocalDateTime.of(2022, 3, 24, 10, 0),
                LocalDateTime.of(2022, 3, 24, 14, 30), "YWR", "YYZ",
                300, 200, 80, 20);
    }

    @Test
    void testSetters() {
        passenger.setFirstName("P");
        assertEquals("P", passenger.getFirstName());
        passenger.setMiddleName("L");
        assertEquals("L", passenger.getMiddleName());
        passenger.setLastName("C");
        assertEquals("C", passenger.getLastName());
        passenger.setEmailAddress("p");
        assertEquals("p", passenger.getEmailAddress());
        passenger.setPassword("*");
        assertEquals("*", passenger.getPassword());
        passenger.setDateOfBirth(LocalDate.of(2005, 1, 2));
        assertEquals(LocalDate.of(2005, 1, 2), passenger.getDateOfBirth());
        passenger.setPhoneNumber("101");
        assertEquals("101", passenger.getPhoneNumber());

        String id = UUID.randomUUID().toString();
        passenger.setId(id);
        assertEquals(UUID.fromString(id), passenger.getId());

        Map<UUID, BookedFlight> bookedFlightMap = new HashMap<>();

        bookedFlightMap.put(f1.getId(), new BookedFlight(1, f1));
        bookedFlightMap.put(f2.getId(), new BookedFlight(2, f2));

        passenger.setBookedFlight(bookedFlightMap);
        assertTrue(passenger.hasFlight(f1));
        assertEquals(2, passenger.getBookedFlights().size());
    }

    @Test
    void testConstructor() {
        assertEquals("C", passenger.getFirstName());
        assertEquals("", passenger.getMiddleName());
        assertEquals("L", passenger.getLastName());
        assertEquals("c", passenger.getEmailAddress());
        assertEquals("play", passenger.getPassword());
        assertEquals(LocalDate.of(2003, 1, 1), passenger.getDateOfBirth());
        assertEquals("000", passenger.getPhoneNumber());
        assertEquals(0, passenger.getBookedFlights().size());
    }

    @Test
    void testAddBookedFlight() {
        passenger.addBookedFlight(f1, 1);
        assertEquals(1, passenger.getBookedFlights().size());
        assertEquals(199, f1.getAvailableEconomySeats());

        passenger.addBookedFlight(f2, 2);
        assertEquals(2, passenger.getBookedFlights().size());
        assertEquals(79, f2.getAvailableBusinessSeats());

        passenger.addBookedFlight(f2, 3);
        assertEquals(2, passenger.getBookedFlights().size());
        assertEquals(19, f2.getAvailableFirstClassSeats());
    }

    @Test
    void testRemoveBookedFlight() {
        passenger.addBookedFlight(f1, 1);
        assertEquals(1, passenger.getBookedFlights().size());
        assertEquals(199, f1.getAvailableEconomySeats());

        passenger.removeBookedFlight(f1.getId());
        assertEquals(0, passenger.getBookedFlights().size());
        assertEquals(200, f1.getAvailableEconomySeats());

        passenger.addBookedFlight(f2, 2);
        assertEquals(1, passenger.getBookedFlights().size());
        assertEquals(79, f2.getAvailableBusinessSeats());

        passenger.removeBookedFlight(f2.getId());
        assertEquals(0, passenger.getBookedFlights().size());
        assertEquals(80, f2.getAvailableBusinessSeats());
        assertFalse(passenger.hasFlight(f2));

        passenger.addBookedFlight(f1, 3);
        assertEquals(1, passenger.getBookedFlights().size());
        assertEquals(19, f1.getAvailableFirstClassSeats());

        passenger.removeBookedFlight(f1.getId());
        assertEquals(0, passenger.getBookedFlights().size());
        assertEquals(20, f1.getAvailableFirstClassSeats());
    }

    @Test
    void testToString() {
        assertEquals("<html>Passenger " + passenger.getId().toString() + ":<br>"
                + "Name: " + passenger.getFirstName() + " " + passenger.getMiddleName() + " "
                + passenger.getLastName() + "<br>"
                + "Date Of Birth: " + passenger.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "<br>"
                + "Email Address: " + passenger.getEmailAddress() + "<br>"
                + "Phone Number: " + passenger.getPhoneNumber() + "<br><html>", passenger.toString());
    }

    @Test
    void testToJson() {
        assertEquals("C", passenger.toJson().getString("first name"));
        assertEquals("L", passenger.toJson().getString("last name"));
        assertEquals("c", passenger.toJson().getString("email"));
        assertEquals("play", passenger.toJson().getString("password"));
        assertEquals("000", passenger.toJson().getString("phone"));
    }

    @Test
    void testBookedFlightsToJson() {
        assertEquals(0, passenger.toJson().getJSONArray("booked flights").length());

        passenger.addBookedFlight(f1, 1);
        assertEquals(1, passenger.toJson().getJSONArray("booked flights").length());
    }
}