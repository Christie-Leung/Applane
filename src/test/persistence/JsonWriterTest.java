package persistence;

import model.Account;
import model.Flight;
import model.FlightSchedule;
import model.Passenger;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Passenger p = new Passenger("Mary", "Smith", "mary", "mary",
                    LocalDate.of(2000, 10, 11), "778");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyAccounts() {
        try {
            Passenger p = new Passenger("Mary", "Smith", "mary", "mary",
                    LocalDate.of(2000, 10, 11), "778");
            Account account = new Account();
            account.addPassenger("mary", p);
            JsonWriter writer = new JsonWriter("./data/testEmptyAccounts.json");
            writer.open();
            writer.write(account);
            writer.close();

            JsonReader reader = new JsonReader("./data/testEmptyAccounts.json");
            account = reader.readAccounts();
            Passenger passenger = account.retrievePassenger("mary");
            checkPassenger(p, passenger.getFirstName(), passenger.getLastName(), passenger.getEmailAddress());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterEmptyFlights() {
        try {
            Flight f = new Flight("AC", 123, LocalDateTime.of(2022, 4, 24, 18, 0),
                    LocalDateTime.of(2022, 4, 24, 0, 25), "YVR", "YEG",
                    300, 200, 80, 20);
            FlightSchedule flightSchedule = new FlightSchedule();
            flightSchedule.addFlight(f);
            JsonWriter writer = new JsonWriter("./data/testEmptyFlights.json");
            writer.open();
            writer.write(flightSchedule);
            writer.close();

            JsonReader reader = new JsonReader("./data/testEmptyFlights.json");
            flightSchedule = reader.readFlights();
            assertEquals(1, flightSchedule.getFlightsByDestination("YVR", "YEG").size());
            checkFlight(flightSchedule.getFlightsByDestination("YVR", "YEG").get(0),
                    "AC", 123, "YVR", "YEG");
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralAccounts() {
        try {

            JsonReader reader = new JsonReader("./data/testAccounts.json");
            Passenger p = new Passenger("Mary", "Smith", "mary", "mary",
                    LocalDate.of(2000, 10, 11), "778");
            Account account = reader.readAccounts();
            account.addPassenger("mary", p);
            JsonWriter writer = new JsonWriter("./data/testAccounts.json");
            writer.open();
            writer.write(account);
            writer.close();

            account = reader.readAccounts();
            Passenger passenger = account.retrievePassenger("mary");
            checkPassenger(p, passenger.getFirstName(), passenger.getLastName(), passenger.getEmailAddress());
            Passenger passenger1 = account.retrievePassenger("christie");
            checkPassenger(passenger1, "Christie", "Leung", "christie");
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralFlights() {
        try {

            JsonReader reader = new JsonReader("./data/testFlights.json");
            Flight flight = new Flight("AC", 187, LocalDateTime.of(2022, 3, 24, 10, 0),
                    LocalDateTime.of(2022, 3, 24, 20, 30), "YVR", "ICN",
                    300, 200, 80, 20);
            FlightSchedule fs = reader.readFlights();
            fs.addFlight(flight);
            JsonWriter writer = new JsonWriter("./data/testFlights.json");
            writer.open();
            writer.write(fs);
            writer.close();

            fs = reader.readFlights();
            Flight f1 = fs.getFlightsByDestination("YVR", "ICN").get(0);
            checkFlight(f1, "AC", 187, "YVR", "ICN");
            Flight f2 = fs.getFlightsByDestination("YVR", "YEG").get(0);
            checkFlight(f2, "AC", 246, "YVR", "YEG");
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}