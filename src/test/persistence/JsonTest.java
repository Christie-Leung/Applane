package persistence;

import model.Flight;
import model.Passenger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonTest {
    protected void checkPassenger(Passenger passenger, String firstName, String lastName, String emailAddress) {
        assertEquals(firstName, passenger.getFirstName());
        assertEquals(lastName, passenger.getLastName());
        assertEquals(emailAddress, passenger.getEmailAddress());
    }

    protected void checkFlight(Flight flight, String airline, int flightNum, String startLoc, String endLoc) {
        assertEquals(airline, flight.getAirlineCode());
        assertEquals(flightNum, flight.getFlightNumber());
        assertEquals(startLoc, flight.getStartLocation());
        assertEquals(endLoc, flight.getEndLocation());
    }
}
