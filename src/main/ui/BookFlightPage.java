package ui;

import model.Account;
import model.Flight;
import model.FlightSchedule;
import model.Passenger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Represents a flight booking page given border layout format
public class BookFlightPage extends BorderLayoutPage {

    JFrame frame;
    Account account;
    Passenger passenger;
    JPanel panel;
    FlightSchedule flightSchedule;

    // EFFECTS: Constructs a flight booking page with given frame, account, flight schedule, passenger and creates
    //          a new JPanel with a border layout then calls to initialize the page
    public BookFlightPage(JFrame frame, Account account, FlightSchedule flightSchedule, Passenger passenger) {
        this.frame = frame;
        this.account = account;
        this.passenger = passenger;
        this.panel = new JPanel(new BorderLayout());
        this.flightSchedule = flightSchedule;
        initializePage();
    }

    /*
    MODIFIES: this
    EFFECTS: fills the page using the border layout functions with the needed information
     */
    void initializePage() {
        createTopPanel(this.panel);

        Dimension dim = new Dimension(700, this.frame.getHeight() * 5 / 6);

        JScrollPane sidePanel = new JScrollPane();
        sidePanel.setLayout(new ScrollPaneLayout());
        sidePanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sidePanel.setPreferredSize(new Dimension(690, this.frame.getHeight() * 5 / 6 - 20));
        sidePanel.setWheelScrollingEnabled(true);
        availableFlights(sidePanel);

        createLeftSidePanel(this.panel, dim, new JLabel("All Available Flights"), sidePanel);

        JButton homePage = new JButton("Back to Home Page");
        homePage.addActionListener(e -> {
            this.frame.getContentPane().removeAll();
            this.frame.repaint();
            new AirplaneApp(this.frame, this.account, this.flightSchedule, this.passenger);
        });
        createRightSidePanel(this.panel, Collections.singletonList(homePage));

        this.frame.add(this.panel);
        this.frame.validate();
    }

    /*
    REQUIRES: scroll pane container
    EFFECTS: fills the container with all available flights given that the user hasn't already booked the same flight
             and the flight isn't full
     */
    void availableFlights(JScrollPane container) {
        List<Flight> flightList = this.flightSchedule.getFlightSchedule();
        JPanel panel = new JPanel();
        if (flightList.isEmpty()) {
            panel.setLayout(new GridBagLayout());
            panel.add(new JLabel("There are no available flights!"));
            return;
        }
        for (Flight flight : flightList) {
            if (!flight.isFull() && !passenger.hasFlight(flight)) {
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                JButton button = new JButton(flight.publicInfo());
                button.setPreferredSize(new Dimension(panel.getWidth(), 100));
                panel.add(button);
                button.addActionListener(e -> bookFlight(flight));
            }
        }
        container.setViewportView(panel);
    }

    /*
    REQUIRES: flight to be booked
    EFFECTS: creates a popup menu to confirm booking
     */
    void bookFlight(Flight flight) {
        JFrame popup = new JFrame();
        popup.setVisible(true);
        popup.setLocationRelativeTo(null);
        popup.setSize(new Dimension(500, 200));
        JPanel container = new JPanel();
        JLabel confirmBooking = new JLabel("<html>Would you like to continue booking this flight?<br>"
                + "From: " + flight.getStartLocation() + "<br>"
                + "To: " + flight.getEndLocation() + "<br><html>");
        JPanel buttons = new JPanel();
        JButton yes = new JButton("Yes");
        JButton no = new JButton("No");
        buttons.add(yes);
        buttons.add(no);
        no.addActionListener(e -> popup.dispose());
        container.add(confirmBooking);
        container.add(buttons);
        popup.add(container);
        yes.addActionListener(e -> chooseSeat(popup, flight));
    }

    /*
    REQUIRES: popup frame and flight to be booked
    EFFECTS: asks the user which type of seat they would like to book on the flight and creates the panel for it
     */
    void chooseSeat(JFrame popup, Flight flight) {
        popup.getContentPane().removeAll();
        JPanel container = new JPanel();
        JLabel seats = new JLabel("<html>There are<br>"
                + flight.getAvailableEconomySeats() + " economy seats<br>"
                + flight.getAvailableBusinessSeats() + " business seats<br>"
                + flight.getAvailableFirstClassSeats() + " first class seats<br>"
                + "Which would you like? <html>");

        JPanel checkboxPanel = new JPanel();
        List<JCheckBox> checkBoxes = getCheckBoxes(flight);
        for (JCheckBox checkBox : checkBoxes) {
            checkboxPanel.add(checkBox);
        }

        JPanel confirmButton = new JPanel();
        JButton confirm = new JButton("Confirm & Save");
        JLabel errorMsg = new JLabel();
        confirmButton.add(confirm);
        confirmButton.add(errorMsg);
        container.add(seats);
        container.add(checkboxPanel);
        container.add(confirmButton);
        confirm.addActionListener(e -> saveBooking(popup, flight, errorMsg, checkBoxes));
        popup.add(container);
        popup.validate();
    }

    /*
    REQUIRES: flight to be booked
    EFFECTS: Creates a list of checkboxes depending on what is available on the given flight
     */
    List<JCheckBox> getCheckBoxes(Flight flight) {
        List<JCheckBox> checkboxes = new ArrayList<>();
        if (flight.getAvailableEconomySeats() != 0) {
            JCheckBox economy = new JCheckBox("Economy");
            checkboxes.add(economy);
        }
        if (flight.getAvailableBusinessSeats() != 0) {
            JCheckBox business = new JCheckBox("Business");
            checkboxes.add(business);
        }
        if (flight.getAvailableFirstClassSeats() != 0) {
            JCheckBox firstClass = new JCheckBox("First Class");
            checkboxes.add(firstClass);
        }
        return checkboxes;
    }

    /*
    REQUIRES: popup frame, flight to be booked, error message and a list of checkboxes that hold seat types
    MODIFIES: this
    EFFECTS: checks if user only selected one checkbox and books flight if one is selected
     */
    void saveBooking(JFrame popup, Flight flight, JLabel errorMsg, List<JCheckBox> checkBoxes) {
        boolean oneSelected = false;
        String seat = "";
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected() && !oneSelected) {
                oneSelected = true;
                seat = checkBox.getText();
            } else if (checkBox.isSelected()) {
                oneSelected = false;
                break;
            }
        }
        if (oneSelected) {
            this.passenger.addBookedFlight(flight, convertSeatStringToInt(seat));
            popup.getContentPane().removeAll();
            popup.add(new JLabel("Successfully Booked!"));
            popup.setSize(new Dimension(200, 100));
            popup.validate();
            this.frame.getContentPane().removeAll();
            new AirplaneApp(this.frame, this.account, this.flightSchedule, this.passenger);
        } else {
            errorMsg.setText("Please select only one option!");
        }
    }

    /*
    REQUIRES: seat type of either economy, business or first class
    EFFECTS: returns the number that represents each seat type
     */
    int convertSeatStringToInt(String seatType) {
        switch (seatType) {
            case "Economy":
                return 1;
            case "Business":
                return 2;
            default:
                return 3;
        }
    }

}
