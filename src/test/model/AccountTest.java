package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;
    private Passenger p1;
    private Passenger p2;
    private Passenger p3;

    @BeforeEach
    void runBefore() {
        p1 = new Passenger("C", "L", "c", "play", LocalDate.of(2003, 1, 1), "000");
        p2 = new Passenger("S", "S", "s", "awesome", LocalDate.of(2001, 12, 1), "250");
        p3 = new Passenger("M", "M", "m", "me", LocalDate.of(2008, 8, 16), "143");
        account = new Account();
        account.addPassenger("c", p1);
        account.addPassenger("s", p2);
        account.addPassenger("m", p3);
    }

    @Test
    void matchPassword() {
        assertEquals(1, account.matchPassword("c", "play"));
        assertEquals(-1, account.matchPassword("erie", "long"));
        assertEquals(0, account.matchPassword("s", "me"));
        assertEquals(1, account.matchPassword("m", "me"));
    }

    @Test
    void deleteAndReplacePassenger() {
        account.deletePassenger("c");
        assertEquals(2, account.accounts.size());

        p1.setEmailAddress("s");
        account.replacePassenger("s", p1);
        assertEquals(2, account.accounts.size());
        assertEquals("s", account.retrievePassenger("s").getEmailAddress());
    }

    @Test
    void retrievePassenger() {
        assertEquals(p1, account.retrievePassenger("c"));
        assertEquals(p2, account.retrievePassenger("s"));
        assertEquals(p3, account.retrievePassenger("m"));
    }

    @Test
    void addPassenger() {
        Passenger p4 = new Passenger("P", "P", "p", "round", LocalDate.of(1994, 2, 25), "192");
        account.addPassenger(p4.getEmailAddress(), p4);
        assertEquals(p4, account.retrievePassenger(p4.getEmailAddress()));
    }

    @Test
    void testToJson() {
        JSONObject json = account.toJson();
        assertEquals(3, json.getJSONArray("accounts").length());

    }
}