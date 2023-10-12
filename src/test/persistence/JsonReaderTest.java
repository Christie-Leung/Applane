package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testAccountReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Account account = reader.readAccounts();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testFlightReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            FlightSchedule fs = reader.readFlights();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyAccount() {
        JsonReader reader = new JsonReader("./data/testEmptyAccounts.json");
        try {
            Account account = reader.readAccounts();
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderEmptyFlights() {
        JsonReader reader = new JsonReader("./data/testEmptyFlights.json");
        try {
            FlightSchedule flightSchedule = reader.readFlights();
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralAccounts() {
        JsonReader reader = new JsonReader("./data/testAccounts.json");
        try {
            Account account = reader.readAccounts();
            Passenger passenger = account.retrievePassenger("christie");
            checkPassenger(passenger, "Christie", "Leung", "christie");
            List<BookedFlight> bookedFlights = passenger.getBookedFlights();
            assertEquals(1, bookedFlights.size());
            assertEquals(246, bookedFlights.get(0).getFlight().getFlightNumber());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralFlights() {
        JsonReader reader = new JsonReader("./data/testFlights.json");
        try {
            FlightSchedule flightSchedule = reader.readFlights();
            assertEquals(1, flightSchedule.getFlightsByDestination("YVR", "YEG").size());
            assertEquals(1, flightSchedule.getFlightsByDestination("YVR", "LAX").size());
            assertEquals(1, flightSchedule.getFlightsByDestination("YVR", "YYZ").size());
            Flight flight = flightSchedule.getFlightsByDestination("YVR", "LAX").get(0);
            checkFlight(flight, "AC", 123, "YVR", "LAX");
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
