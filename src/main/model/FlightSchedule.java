package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents a Flight schedule that holds all scheduled flights
public class FlightSchedule implements Writable {

    List<Flight> flightSchedule = new ArrayList<>();

    // REQUIRES: start and end airport codes that aren't empty
    // EFFECTS: finds all flights with given start and end airport codes and adds them to a list to be returned
    public List<Flight> getFlightsByDestination(String startDestination, String endDestination) {
        List<Flight> flights = new ArrayList<>();
        for (Flight flight : flightSchedule) {
            if (flight.getStartLocation().equalsIgnoreCase(startDestination)
                    && flight.getEndLocation().equalsIgnoreCase(endDestination)) {
                flights.add(flight);
            }
        }
        return flights;
    }

    // REQUIRES: flight
    // MODIFIES: this
    // EFFECTS: adds a flight to the flight schedule
    public void addFlight(Flight flight) {
        this.flightSchedule.add(flight);
    }


    // EFFECTS: converts flight schedule into a json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("flights", flightToJson());


        return json;
    }

    // EFFECTS: converts flights into a json array
    public JSONArray flightToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Flight f : flightSchedule) {
            jsonArray.put(f.toJson());
        }

        return jsonArray;
    }

    public List<Flight> getFlightSchedule() {
        return this.flightSchedule;
    }

}
