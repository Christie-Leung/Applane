package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.HashMap;

// Stores a map of all accounts which holds the passenger's email as the key and the passenger as its value.
public class Account implements Writable {
    public final HashMap<String, Passenger> accounts = new HashMap<>();

    // REQUIRES: email and password as string
    // EFFECTS: checks if given email is registered and
    //          whether the password matches the saved password for the passenger
    public int matchPassword(String email, String password) {
        Passenger passenger;
        String passengerPassword;
        try {
            passenger = accounts.get(email);
            passengerPassword = passenger.getPassword();

        } catch (NullPointerException nullPointerException) {
            return -1;
        }
        return password.equals(passengerPassword) ? 1 : 0;
    }

    // REQUIRES: email of passenger that already exists in accounts\
    //           assume that emails are unique
    // EFFECTS: get the passenger with the corresponding email address
    public Passenger retrievePassenger(String email) {
        return accounts.get(email);
    }

    /*
    REQUIRES: email that is not empty and passenger that is not null
    MODIFIES: this
    EFFECTS: adds the passenger to the list of accounts with the key being their email address
     */
    public void addPassenger(String email, Passenger passenger) {
        accounts.put(email, passenger);
        EventLog eventlog = EventLog.getInstance();
        eventlog.logEvent(new Event("Added passenger " + passenger.getFirstName() + " "
                + passenger.getLastName() + " to accounts database."));
    }

    /*
    REQUIRES: email that is not empty and passenger that is not null
    MODIFIES: this
    EFFECTS: adds the passenger to the list of accounts with the key being their email address
     */
    public void loadPassenger(String email, Passenger passenger) {
        accounts.put(email, passenger);
        EventLog eventlog = EventLog.getInstance();
        eventlog.logEvent(new Event("Loaded passenger " + passenger.getFirstName() + " "
                + passenger.getLastName() + " from file."));
    }

    /*
    REQUIRES: email of passenger that is currently in the accounts hashmap
    MODIFIES: this
    EFFECTS: deletes the given passenger from accounts
     */
    public void deletePassenger(String email) {
        Passenger passenger = accounts.remove(email);
        EventLog eventlog = EventLog.getInstance();
        eventlog.logEvent(new Event("Removed passenger " + passenger.getFirstName() + " "
                + passenger.getLastName() + " from accounts database."));
    }

    /*
    REQUIRES: email that is currently in the accounts hashmap and the new passenger that hasn't
              changes their email
    MODIFIES: this
    EFFECTS: replaces the old passenger with the new passenger
     */
    public void replacePassenger(String email, Passenger passenger) {
        accounts.replace(email, passenger);
    }

    /*
    EFFECTS: converts all accounts into a json object
     */
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accounts", accountToJson());

        return jsonObject;
    }

    /*
    EFFECTS: converts all passengers that have an account into a json array
     */
    public JSONArray accountToJson() {
        JSONArray json = new JSONArray();

        accounts.forEach((k, v) -> json.put(v.toJson()));

        return json;
    }

}
