package model;

import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

// Represents a flight that holds all information regarding one flight
public class Flight implements Writable {

    private UUID id;
    private String airlineCode;
    private int flightNumber;
    private int gateNumber;
    private String airplaneModel;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private String startLocation;
    private String endLocation;

    private int maxSeats;
    private int availableEconomySeats;
    private int availableBusinessSeats;
    private int availableFirstClassSeats;


    private double economyPrice;
    private double businessPrice;
    private double firstClassPrice;


    // REQUIRES: Airline code, flight number, departure and arrival dates and time as a LocalDateTime
    //           start and end location as airport codes, max number of seats on flight
    //           available economy, business, first class seats
    // MODIFIES: this
    // EFFECTS: Constructs a flight with a random unique UUID, given airline code, flight number
    //          departure and arrival times, start and end location, number of seats,
    //          available economy, business, and first class seats and set gate number to -1
    //          and airplane model to "TBD"
    public Flight(String airline, int flightNumber, LocalDateTime departureTime, LocalDateTime arrivalTime,
                  String startLocation, String endLocation, int maxSeats, int availableEconomySeats,
                  int availableBusinessSeats, int availableFirstClassSeats) {
        this.id = UUID.randomUUID();
        this.airlineCode = airline;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.maxSeats = maxSeats;
        this.availableEconomySeats = availableEconomySeats;
        this.availableBusinessSeats = availableBusinessSeats;
        this.availableFirstClassSeats = availableFirstClassSeats;
        this.gateNumber = -1;
        this.airplaneModel = "TBD";
        this.flightNumber = flightNumber;
    }

    // EFFECTS: returns a string with advanced details of the flight
    @Override
    public String toString() {
        return "<html>Flight " + airlineCode + flightNumber + " (" + id + ")<br>"
                + "Departure: " + startLocation
                + " [" + departureTime.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")) + "]<br>"
                + "Arrival: " + endLocation
                + " [" + arrivalTime.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")) + "]<br>"
                + "Airplane Model: " + airplaneModel + "<br>"
                + "Maximum Seats: " + maxSeats + "<br>"
                + "Available Economy Seats: " + availableEconomySeats + "<br>"
                + "Available Business Seats: " + availableBusinessSeats + "<br>"
                + "Available First Class Seats: " + availableFirstClassSeats + "<html>";
    }

    // EFFECTS: returns a string with basic details of the flight
    public String publicInfo() {
        return "<html>Flight " + airlineCode + flightNumber + " (" + id + ")<br>"
                + "Departure: " + startLocation
                + " [" + departureTime.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")) + "]<br>"
                + "Arrival: " + endLocation
                + " [" + arrivalTime.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")) + "]<br>"
                + "Remaining Seats: " + (availableFirstClassSeats + availableBusinessSeats + availableEconomySeats)
                + "<html>";
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

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }


    public int getGateNumber() {
        return gateNumber;
    }

    public void setGateNumber(int gateNumber) {
        this.gateNumber = gateNumber;
    }

    public String getAirplaneModel() {
        return airplaneModel;
    }


    public void setAirplaneModel(String airplaneModel) {
        this.airplaneModel = airplaneModel;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }


    public String getStartLocation() {
        return startLocation;
    }


    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }


    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }


    public int getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(int maxSeats) {
        this.maxSeats = maxSeats;
    }


    public int getAvailableEconomySeats() {
        return availableEconomySeats;
    }

    public void setAvailableEconomySeats(int availableEconomySeats) {
        this.availableEconomySeats = availableEconomySeats;
    }


    public int getAvailableBusinessSeats() {
        return availableBusinessSeats;
    }

    public void setAvailableBusinessSeats(int availableBusinessSeats) {
        this.availableBusinessSeats = availableBusinessSeats;
    }


    public int getAvailableFirstClassSeats() {
        return availableFirstClassSeats;
    }

    public void setAvailableFirstClassSeats(int availableFirstClassSeats) {
        this.availableFirstClassSeats = availableFirstClassSeats;
    }


    public double getEconomyPrice() {
        return economyPrice;
    }

    public void setEconomyPrice(double economyPrice) {
        this.economyPrice = economyPrice;
    }


    public double getBusinessPrice() {
        return businessPrice;
    }


    public void setBusinessPrice(double businessPrice) {
        this.businessPrice = businessPrice;
    }

    public double getFirstClassPrice() {
        return firstClassPrice;
    }

    public void setFirstClassPrice(double firstClassPrice) {
        this.firstClassPrice = firstClassPrice;
    }


    // MODIFIES: this
    // EFFECTS: subtracts one from the available economy seat
    public void bookEconomy() {
        this.availableEconomySeats -= 1;
    }

    // MODIFIES: this
    // EFFECTS: subtracts one from the available business seat
    public void bookBusiness() {
        this.availableBusinessSeats -= 1;
    }

    // MODIFIES: this
    // EFFECTS: subtracts one from the available first class seat
    public void bookFirstClass() {
        this.availableFirstClassSeats -= 1;
    }

    // MODIFIES: this
    // EFFECTS: adds one seat to the available economy seat
    public void addEconomySeat() {
        this.availableEconomySeats += 1;
    }

    // MODIFIES: this
    // EFFECTS: adds one seat to the available business seat
    public void addBusinessSeat() {
        this.availableBusinessSeats += 1;
    }

    // MODIFIES: this
    // EFFECTS: adds one seat to the available first class seat
    public void addFirstClassSeat() {
        this.availableFirstClassSeats += 1;
    }

    // EFFECTS: returns true if flight is full
    public Boolean isFull() {
        return (this.availableBusinessSeats + this.availableEconomySeats + this.availableFirstClassSeats)
                == 0;
    }

    // EFFECTS: converts a flight into a json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("uuid", id);
        json.put("airline", airlineCode);
        json.put("flight number", flightNumber);
        json.put("gate number", gateNumber);
        json.put("airplane model", airplaneModel);
        json.put("departure", departureTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        json.put("arrival", arrivalTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        json.put("start location", startLocation);
        json.put("destination", endLocation);
        json.put("max seats", maxSeats);
        json.put("remaining economy seats", availableEconomySeats);
        json.put("remaining business seats", availableBusinessSeats);
        json.put("remaining first class seats", availableFirstClassSeats);
        json.put("economy price", economyPrice);
        json.put("business price", businessPrice);
        json.put("first class price", firstClassPrice);

        return json;
    }

}
