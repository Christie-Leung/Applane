package ui;

import model.Account;
import model.BookedFlight;
import model.FlightSchedule;
import model.Passenger;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

// Represents the booked flight page
public class BookedFlightPage extends BorderLayoutPage {
    JFrame frame;
    JPanel panel;
    BookedFlight flight;
    Passenger passenger;
    Account account;
    FlightSchedule flightSchedule;

    // EFFECTS: Constructs a new booked flight page with given frame, account, flight schedule, passenger
    //          booked flight, creates a new JPanel with a border layout and calls to initialize the page
    public BookedFlightPage(JFrame frame, Account account, FlightSchedule flightSchedule,
                            Passenger passenger, BookedFlight flight) {
        this.frame = frame;
        this.panel = new JPanel(new BorderLayout());
        this.passenger = passenger;
        this.flight = flight;
        this.account = account;
        this.flightSchedule = flightSchedule;
        initializePage();
    }

    /*
    MODIFIES: this
    EFFECTS: calls to fill the page with needed information and adds it to the frame
     */
    void initializePage() {
        bookedFlightAppPage();
        this.frame.add(panel);
        this.frame.validate();
    }

    /*
    MODIFIES: this
    EFFECTS: creates panels and add it to the page given the border layout formats
     */
    void bookedFlightAppPage() {
        this.panel = createTopPanel(this.panel);

        JPanel flightInfo = createInfoPanel("Your Flight", "<html>" + this.flight.getFlight().toString()
                + "<br>" + "Seat: " + this.flight.getSeat() + "<html>");


        this.panel = createLeftSidePanel(this.panel, this.frame.getHeight(), new JLabel(), flightInfo);

        JButton homePage = new JButton("Back to Home Page");
        JButton cancelFlight = new JButton("Cancel Flight");
        homePage.addActionListener(e -> {
            this.frame.getContentPane().removeAll();
            this.frame.repaint();
            new AirplaneApp(this.frame, this.account, this.flightSchedule, this.passenger);
        });
        cancelFlight.addActionListener(e -> cancelFlightAction());

        this.panel = createRightSidePanel(this.panel, Arrays.asList(homePage, cancelFlight));
    }

    /*
    EFFECTS: Creates the cancel flight popup menu and confirmation to cancel the flight
     */
    void cancelFlightAction() {
        JFrame popup = new JFrame();
        popup.setSize(new Dimension(300, 110));
        popup.setVisible(true);
        popup.setLocationRelativeTo(null);
        JPanel container = new JPanel();
        JPanel buttons = new JPanel();
        JLabel label = new JLabel("Are you sure you want to cancel this flight?");
        JButton yes = new JButton("Yes & Save");
        JButton no = new JButton("No");
        buttons.add(yes);
        buttons.add(no);
        no.addActionListener(e -> popup.dispose());
        container.add(label);
        container.add(buttons);
        popup.add(container);
        yes.addActionListener(e -> removeFlightAction(popup, container));
    }

    /*
    MODIFIES: this
    EFFECTS: removes the flight from passenger's booked flight list and
             shows a confirmation message that the flight is removed
     */
    void removeFlightAction(JFrame popup, JPanel container) {
        this.passenger.removeBookedFlight(this.flight.getFlight().getId());

        container.removeAll();
        container.add(new JLabel("Successfully removed and saved!"));
        popup.getContentPane().removeAll();
        popup.repaint();
        popup.add(container);
        popup.validate();

        this.frame.getContentPane().removeAll();
        this.frame.repaint();
        new AirplaneApp(this.frame, this.account, this.flightSchedule, this.passenger);
    }


}
