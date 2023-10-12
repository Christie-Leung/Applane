package model;

import org.json.JSONObject;
import persistence.Writable;

// Booked flights are flights booked by passengers that holds their booked seat type and flight
public class BookedFlight implements Writable {

    private final int seat;
    private final Flight flight;

    // REQUIRES: seat type in int (1-economy, 2-business, 3-first-class)
    //           and flight
    // EFFECTS: Constructs a booked flight with given seat and flight
    public BookedFlight(int seat, Flight flight) {
        this.flight = flight;
        this.seat = seat;
    }

    public int getSeat() {
        return seat;
    }

    public Flight getFlight() {
        return flight;
    }

    // EFFECTS: converts a booked flight into a json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("uuid", flight.getId());
        json.put("seat", seat);
        json.put("flight", flight.toJson());

        return json;
    }

}
