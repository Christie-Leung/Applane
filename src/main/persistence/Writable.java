package persistence;

import org.json.JSONObject;

// Copied from JsonSerializationDemo
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}