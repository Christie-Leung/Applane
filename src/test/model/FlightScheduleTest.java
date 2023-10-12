package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FlightScheduleTest {

    private FlightSchedule flightSchedule;
    private Flight f1;
    private Flight f2;
    private Flight f3;


    @BeforeEach
    void runBefore() {
        flightSchedule = new FlightSchedule();
        f1 = new Flight("AC", 123, LocalDateTime.of(2022, 4, 24, 18, 0),
                LocalDateTime.of(2022, 4, 24, 0, 25), "YWR", "YEG",
                300, 200, 80, 20);
        f2 = new Flight("AC", 123, LocalDateTime.of(2022, 3, 24, 10, 0),
                LocalDateTime.of(2022, 3, 24, 14, 30), "YWR", "YYZ",
                300, 200, 80, 20);
        f3 = new Flight("AC", 245, LocalDateTime.of(2022, 5, 24, 10, 0),
                LocalDateTime.of(2022, 3, 24, 14, 30), "YWR", "YYZ",
                300, 200, 80, 20);
        flightSchedule.addFlight(f1);
        flightSchedule.addFlight(f2);
        flightSchedule.addFlight(f3);
    }

    @Test
    void getFlightsByDestination() {
        assertEquals(3, flightSchedule.getFlightSchedule().size());
        assertEquals(1, flightSchedule.getFlightsByDestination("YWR", "YEG").size());
        assertEquals(0, flightSchedule.getFlightsByDestination("YWR", "MRW").size());
        assertEquals(2, flightSchedule.getFlightsByDestination("YWR", "YYZ").size());
        assertEquals(0, flightSchedule.getFlightsByDestination("YEG", "YYZ").size());
    }

    @Test
    void addFlight() {
        Flight f4 = new Flight("AC", 123, LocalDateTime.of(2022, 3, 22, 15, 0),
                LocalDateTime.of(2022, 3, 22, 17, 30), "YWR", "LAX",
                300, 200, 80, 20);
        flightSchedule.addFlight(f4);
        assertEquals(1, flightSchedule.getFlightsByDestination("YWR", "LAX").size());
    }

    @Test
    void testToJson() {
        JSONObject json = flightSchedule.toJson();
        assertEquals(3, json.getJSONArray("flights").length());
    }
}