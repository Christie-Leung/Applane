package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FlightTest {

    private Flight flight;

    @BeforeEach
    void runBefore() {
        flight = new Flight("AC", 123, LocalDateTime.of(2022, 4, 24, 18, 0),
                LocalDateTime.of(2022, 4, 24, 0, 25), "YWR", "YEG",
                300, 200, 80, 20);
    }

    @Test
    void testConstructor() {
        assertEquals("AC", flight.getAirlineCode());
        assertEquals(123, flight.getFlightNumber());
        assertEquals(LocalDateTime.of(2022, 4, 24, 18, 0), flight.getDepartureTime());
        assertEquals(LocalDateTime.of(2022, 4, 24, 0, 25), flight.getArrivalTime());
        assertEquals("YWR", flight.getStartLocation());
        assertEquals("YEG", flight.getEndLocation());
        assertEquals(300, flight.getMaxSeats());
        assertEquals(200, flight.getAvailableEconomySeats());
        assertEquals(80, flight.getAvailableBusinessSeats());
        assertEquals(20, flight.getAvailableFirstClassSeats());
        assertEquals(0, flight.getEconomyPrice());
        assertEquals(0, flight.getBusinessPrice());
        assertEquals(0, flight.getFirstClassPrice());
        assertEquals(-1, flight.getGateNumber());
        assertEquals("TBD", flight.getAirplaneModel());
    }

    @Test
    void testSetters() {
        flight.setAirlineCode("JET");
        assertEquals("JET", flight.getAirlineCode());
        flight.setFlightNumber(321);
        assertEquals(321, flight.getFlightNumber());
        flight.setDepartureTime(LocalDateTime.of(2022, 5,11, 12,0));
        assertEquals(LocalDateTime.of(2022, 5,11, 12,0), flight.getDepartureTime());
        flight.setArrivalTime(LocalDateTime.of(2022, 5,11, 15,0));
        assertEquals(LocalDateTime.of(2022, 5,11, 15,0), flight.getArrivalTime());
        flight.setStartLocation("YVR");
        assertEquals("YVR", flight.getStartLocation());
        flight.setEndLocation("MON");
        assertEquals("MON", flight.getEndLocation());
        flight.setMaxSeats(1000);
        assertEquals(1000, flight.getMaxSeats());
        flight.setAvailableEconomySeats(700);
        assertEquals(700, flight.getAvailableEconomySeats());
        flight.setAvailableBusinessSeats(250);
        assertEquals(250, flight.getAvailableBusinessSeats());
        flight.setAvailableFirstClassSeats(50);
        assertEquals(50, flight.getAvailableFirstClassSeats());
        flight.setEconomyPrice(500);
        assertEquals(500, flight.getEconomyPrice());
        flight.setBusinessPrice(700);
        assertEquals(700, flight.getBusinessPrice());
        flight.setFirstClassPrice(1000);
        assertEquals(1000, flight.getFirstClassPrice());
        flight.setGateNumber(54);
        assertEquals(54, flight.getGateNumber());
        assertFalse(flight.isFull());

        flight.setAvailableEconomySeats(0);
        flight.setAvailableBusinessSeats(0);
        flight.setAvailableFirstClassSeats(0);
        assertTrue(flight.isFull());

        UUID id = UUID.randomUUID();
        flight.setId(id.toString());
        assertEquals(id, flight.getId());
    }


    @Test
    void testBookEconomy() {
        flight.bookEconomy();
        assertEquals(199, flight.getAvailableEconomySeats());

        flight.bookEconomy();
        assertEquals(198, flight.getAvailableEconomySeats());

        flight.addEconomySeat();
        assertEquals(199, flight.getAvailableEconomySeats());

        assertEquals(80, flight.getAvailableBusinessSeats());
        assertEquals(20, flight.getAvailableFirstClassSeats());
    }

    @Test
    void testBookBusiness() {
        flight.bookBusiness();
        assertEquals(79, flight.getAvailableBusinessSeats());

        flight.bookBusiness();
        assertEquals(78, flight.getAvailableBusinessSeats());

        flight.addBusinessSeat();
        assertEquals(79, flight.getAvailableBusinessSeats());

        assertEquals(200, flight.getAvailableEconomySeats());
        assertEquals(20, flight.getAvailableFirstClassSeats());
    }

    @Test
    void testBookFirstClass() {
        flight.bookFirstClass();
        assertEquals(19, flight.getAvailableFirstClassSeats());

        flight.bookFirstClass();
        assertEquals(18, flight.getAvailableFirstClassSeats());

        flight.addFirstClassSeat();
        assertEquals(19, flight.getAvailableFirstClassSeats());

        assertEquals(200, flight.getAvailableEconomySeats());
        assertEquals(80, flight.getAvailableBusinessSeats());
    }

    @Test
    void testToString() {
        assertEquals("<html>Flight " + flight.getAirlineCode() + flight.getFlightNumber() + " (" 
                + flight.getId() + ")<br>"
                + "Departure: " + flight.getStartLocation()
                + " [" + flight.getDepartureTime().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")) + "]<br>"
                + "Arrival: " + flight.getEndLocation()
                + " [" + flight.getArrivalTime().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")) + "]<br>"
                + "Airplane Model: " + flight.getAirplaneModel() + "<br>"
                + "Maximum Seats: " + flight.getMaxSeats() + "<br>"
                + "Available Economy Seats: " + flight.getAvailableEconomySeats() + "<br>"
                + "Available Business Seats: " + flight.getAvailableBusinessSeats() + "<br>"
                + "Available First Class Seats: " + flight.getAvailableFirstClassSeats() + "<html>",
                flight.toString());

        flight.setAirplaneModel("Boeing");

        assertEquals("<html>Flight " + flight.getAirlineCode() + flight.getFlightNumber() + " (" 
                + flight.getId() + ")<br>"
                + "Departure: " + flight.getStartLocation()
                + " [" + flight.getDepartureTime().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")) + "]<br>"
                + "Arrival: " + flight.getEndLocation()
                + " [" + flight.getArrivalTime().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")) + "]<br>"
                + "Airplane Model: " + flight.getAirplaneModel()+ "<br>"
                + "Maximum Seats: " + flight.getMaxSeats() + "<br>"
                + "Available Economy Seats: " + flight.getAvailableEconomySeats() + "<br>"
                + "Available Business Seats: " + flight.getAvailableBusinessSeats() + "<br>"
                + "Available First Class Seats: " + flight.getAvailableFirstClassSeats() + "<html>",
                flight.toString());
    }

    @Test
    void testPublicInfo() {
        assertEquals("<html>Flight " + flight.getAirlineCode() + flight.getFlightNumber() + " ("
                + flight.getId() + ")<br>"
                + "Departure: " + flight.getStartLocation()
                + " [" + flight.getDepartureTime().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")) + "]<br>"
                + "Arrival: " + flight.getEndLocation()
                + " [" + flight.getArrivalTime().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")) + "]<br>"
                + "Remaining Seats: " + (flight.getAvailableEconomySeats() + flight.getAvailableBusinessSeats()
                        + flight.getAvailableFirstClassSeats() + "<html>"), flight.publicInfo());
    }

    @Test
    void testToJson() {
        JSONObject json = flight.toJson();

        assertEquals("AC", json.getString("airline"));
        assertEquals(123, json.getInt("flight number"));
        assertEquals(-1, json.getInt("gate number"));
        assertEquals("TBD", json.getString("airplane model"));
        assertEquals(300, json.getInt("max seats"));
    }


}