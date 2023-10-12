package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

// reads json files
// structure taken from JsonSerializationDemo
public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads accounts from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Account readAccounts() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAccounts(jsonObject);
    }

    // EFFECTS: reads flights from file and returns it;
    // throws IOException if an error occurs reading data from file
    public FlightSchedule readFlights() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseFlightSchedule(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }


    // EFFECTS: parses accounts from JSON object and returns it
    private Account parseAccounts(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("accounts");
        Account account = new Account();

        for (Object json : jsonArray) {
            JSONObject nextAccount = (JSONObject) json;
            parsePassenger(account, nextAccount);
        }
        return account;
    }

    // REQUIRES: json object of passenger's account
    // EFFECTS: reads the passenger's info from the json file and makes a new passenger,
    //          then turn it into an account and returns it
    private void parsePassenger(Account account, JSONObject jsonObject) {
        String firstName = jsonObject.getString("first name");
        String middleName = jsonObject.getString("middle name");
        String lastName = jsonObject.getString("last name");
        String emailAddress = jsonObject.getString("email");
        String password = jsonObject.getString("password");
        LocalDate dateOfBirth = LocalDate.parse(
                jsonObject.getString("date of birth"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String phoneNumber = jsonObject.getString("phone");
        String id = jsonObject.getString("uuid");

        Map<UUID, BookedFlight> bookedFlight = parseBookedFlights(jsonObject.getJSONArray("booked flights"));

        Passenger passenger = new Passenger(firstName, lastName, emailAddress, password, dateOfBirth, phoneNumber);
        passenger.setMiddleName(middleName);
        passenger.setId(id);
        passenger.setBookedFlight(bookedFlight);

        account.loadPassenger(emailAddress, passenger);
    }

    // REQUIRES: json array of booked flights
    // EFFECTS: reads booked flights from the json array and turns it into a map
    //          with its uuid and seat
    // Note: there will only be one flight within one booked flight.
    private Map<UUID, BookedFlight> parseBookedFlights(JSONArray jsonArray) {
        Map<UUID, BookedFlight> bookedFlights = new HashMap<>();

        for (Object json : jsonArray) {
            JSONObject nextFlight = (JSONObject) json;
            UUID id = UUID.fromString(nextFlight.getString("uuid"));
            Flight flight = parseFlight(nextFlight.getJSONObject("flight"));
            int seat = nextFlight.getInt("seat");
            BookedFlight bookedFlight = new BookedFlight(seat, flight);
            bookedFlights.put(id, bookedFlight);
        }
        return bookedFlights;
    }


    // REQUIRES: json object of flight
    // EFFECTS: reads the flight info from the json object and constructs a new flight
    //          with the data and returns it.
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private Flight parseFlight(JSONObject jsonObject) {
        String id = jsonObject.getString("uuid");
        String airline = jsonObject.getString("airline");
        int flightNum = jsonObject.getInt("flight number");
        int gateNumber = jsonObject.getInt("gate number");
        String planeModel = jsonObject.getString("airplane model");
        LocalDateTime departure = LocalDateTime.parse(
                jsonObject.getString("departure"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        );
        LocalDateTime arrival = LocalDateTime.parse(
                jsonObject.getString("arrival"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        );
        String startLocation = jsonObject.getString("start location");
        String destination = jsonObject.getString("destination");
        int maxSeats = jsonObject.getInt("max seats");
        int economySeats = jsonObject.getInt("remaining economy seats");
        int businessSeats = jsonObject.getInt("remaining business seats");
        int firstClassSeats = jsonObject.getInt("remaining first class seats");
        double economyPrice = jsonObject.getDouble("economy price");
        double businessPrice = jsonObject.getDouble("business price");
        double firstClassPrice = jsonObject.getDouble("first class price");

        Flight flight = new Flight(airline, flightNum, departure, arrival, startLocation, destination,
                maxSeats, economySeats, businessSeats, firstClassSeats);
        flight.setId(id);
        flight.setGateNumber(gateNumber);
        flight.setAirplaneModel(planeModel);
        flight.setEconomyPrice(economyPrice);
        flight.setBusinessPrice(businessPrice);
        flight.setFirstClassPrice(firstClassPrice);

        return flight;
    }

    // REQUIRES: json object of flight schedule
    // EFFECTS: parses flight schedule from JSON object and returns it
    private FlightSchedule parseFlightSchedule(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("flights");
        FlightSchedule flightSchedule = new FlightSchedule();

        for (Object json : jsonArray) {
            JSONObject flight = (JSONObject) json;
            flightSchedule.addFlight(parseFlight(flight));
        }
        return flightSchedule;
    }

}
