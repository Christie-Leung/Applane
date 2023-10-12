package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

// Represents a passenger with their needed information to book flights
public class Passenger implements Writable {

    private String firstName;
    private String lastName;
    private String middleName;

    private String emailAddress;
    private String password;

    private LocalDate dateOfBirth;
    private String phoneNumber;

    private Map<UUID, BookedFlight> bookedFlights;

    private UUID id;

    // REQUIRES: first and last name that isn't empty,
    //           a valid email address, a password
    //           date of birth, and phone number
    // MODIFIES: this
    // EFFECTS: constructs a passenger with given first and last name
    //          email address, password, date of birth, phone number and
    //          sets their middle name to an empty string
    //          add an empty map of booked flights and a unique UUID
    public Passenger(String firstName, String lastName, String emailAddress,
                     String password, LocalDate dateOfBirth, String phoneNumber) {
        this.firstName = firstName;
        this.middleName = "";
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.bookedFlights = new HashMap<>();
        this.id = UUID.randomUUID();
    }

    // EFFECTS: returns a string with details of the passenger
    @Override
    public String toString() {
        return "<html>Passenger " + id.toString() + ":<br>"
                + "Name: " + firstName + " " + middleName + " " + lastName + "<br>"
                + "Date Of Birth: " + dateOfBirth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "<br>"
                + "Email Address: " + emailAddress + "<br>"
                + "Phone Number: " + phoneNumber + "<br><html>";
    }

    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }


    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UUID getId() {
        return id;
    }

    // REQUIRES: id as a string
    // MODIFIES: this
    // EFFECTS: converts a string into a UUID and sets it to passenger
    public void setId(String id) {
        this.id = UUID.fromString(id);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // EFFECTS: gets all booked flights without the uuid
    public List<BookedFlight> getBookedFlights() {
        return new ArrayList<>(bookedFlights.values());
    }

    // REQUIRES: flight and an integer from 1-3 indicating economy, business, first class respectively
    // MODIFIES: flight, this
    // EFFECTS: adds a flight to the user's booked flights and subtract a seat from the corresponding flight
    //          depending on seat chosen
    public void addBookedFlight(Flight flight, int seat) {
        this.bookedFlights.put(flight.getId(), new BookedFlight(seat, flight));
        switch (seat) {
            case 1:
                flight.bookEconomy();
                break;
            case 2:
                flight.bookBusiness();
                break;
            default:
                flight.bookFirstClass();
                break;
        }
        EventLog eventlog = EventLog.getInstance();
        eventlog.logEvent(new Event("Added Flight " + flight.getAirlineCode() + flight.getFlightNumber()
                + " to Passenger " + this.getFirstName() + " " + this.getLastName()
                + " to passenger's booked flights."));
    }

    public void setBookedFlight(Map<UUID, BookedFlight> bookedFlights) {
        this.bookedFlights = bookedFlights;
    }

    /*
    EFFECTS: returns true if flight is in passenger's booked flights
     */
    public boolean hasFlight(Flight flight) {
        return this.bookedFlights.get(flight.getId()) != null;
    }

    /*
    REQUIRES: ID of flight
    MODIFIES: this
    EFFECTS: removes the given flight from the list of booked flights
             and add one to the corresponding seats on that flight
     */
    public void removeBookedFlight(UUID id) {
        BookedFlight flight = this.bookedFlights.get(id);
        switch (flight.getSeat()) {
            case 1:
                flight.getFlight().addEconomySeat();
                break;
            case 2:
                flight.getFlight().addBusinessSeat();
                break;
            default:
                flight.getFlight().addFirstClassSeat();
                break;
        }
        this.bookedFlights.remove(id);
        EventLog eventlog = EventLog.getInstance();
        eventlog.logEvent(new Event("Removed Flight " + flight.getFlight().getAirlineCode()
                + flight.getFlight().getFlightNumber()
                + " to Passenger " + this.getFirstName() + " " + this.getLastName()
                + " from passenger's booked flights."));
    }


    // EFFECTS: converts a passenger into a json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("uuid", id);
        json.put("first name", firstName);
        json.put("middle name", middleName);
        json.put("last name", lastName);
        json.put("email", emailAddress);
        json.put("password", password);
        json.put("date of birth", dateOfBirth);
        json.put("phone", phoneNumber);
        json.put("booked flights", bookedFlightsToJson());

        return json;
    }

    // EFFECTS: converts booked flights into a json array
    public JSONArray bookedFlightsToJson() {
        JSONArray json = new JSONArray();

        bookedFlights.forEach((k,v) -> json.put(v.toJson()));

        return json;
    }
}
