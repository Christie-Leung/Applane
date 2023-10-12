package ui;

import model.Account;
import model.BookedFlight;
import model.FlightSchedule;
import model.Passenger;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

// Represents the main airplane app screen that takes a border layout format
public class AirplaneApp extends BorderLayoutPage {
    JFrame frame;
    JPanel panel;
    Passenger passenger;
    Account account;

    private final FlightSchedule flightSchedule;

    // EFFECTS: Constructs a new airplane page with given frame, account, flight schedule and passenger
    //          makes a new JPanel and calls to initialize page.
    public AirplaneApp(JFrame frame, Account account, FlightSchedule flightSchedule, Passenger passenger) {
        this.flightSchedule = flightSchedule;
        this.frame = frame;
        this.panel = new JPanel();
        this.account = account;
        this.passenger = passenger;
        initializePage();
    }

    /*
    MODIFIES: this
    EFFECTS: initializes the page
     */
    void initializePage() {
        this.panel.setLayout(new BorderLayout());
        this.panel.setMinimumSize(new Dimension(frame.getWidth(), frame.getHeight()));
        airplaneAppMenu();
        this.frame.add(this.panel);
        this.frame.validate();
    }

    /*
    MODIFIES: this
    EFFECTS: creates the different panels that take up different borders of the screen
     */
    void airplaneAppMenu() {
        this.panel = createTopPanel(this.panel);

        JLabel bookedFlights = new JLabel("Booked Flights");
        bookedFlights.setFont(new Font("Georgia", Font.BOLD, 14));

        Dimension dim = new Dimension(700, this.frame.getHeight() * 5 / 6);

        JScrollPane sidePanel = new JScrollPane();
        sidePanel.setLayout(new ScrollPaneLayout());
        sidePanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sidePanel.setPreferredSize(new Dimension(690, this.frame.getHeight() * 5 / 6 - 20));
        sidePanel.setWheelScrollingEnabled(true);
        addBookedFlights(sidePanel);

        this.panel = createLeftSidePanel(this.panel, dim, bookedFlights, sidePanel);



        this.panel = createRightSidePanel(this.panel, createButtons());
    }

    /*
    MODIFIES: this
    EFFECTS: create the buttons that would be on the side of the page
     */
    List<JButton> createButtons() {
        JButton bookFlight = new JButton("Book new Flight");
        JButton accountInfo = new JButton("Account Information");
        JButton signOut = new JButton("Sign Out & Return to Login");
        bookFlight.addActionListener(e -> {
            this.frame.getContentPane().removeAll();
            new BookFlightPage(this.frame, this.account, this.flightSchedule, this.passenger);
        });
        accountInfo.addActionListener(e -> {
            this.frame.getContentPane().removeAll();
            new AccountInfoPage(this.frame, this.account, this.flightSchedule, this.passenger);
        });
        signOut.addActionListener(e -> {
            this.frame.getContentPane().removeAll();
            new LoginPage(this.frame, this.account);
        });
        return Arrays.asList(bookFlight, accountInfo, signOut);
    }

/*
    void createTopPanel() {
        JPanel topPanel = new JPanel();
        JLabel appTitle = new JLabel("APPLANE");
        appTitle.setFont(new Font("Serif", Font.BOLD, 24));
        topPanel.add(appTitle);
        topPanel.setBackground(new Color(0xC1C1EF));

        this.panel.add(topPanel, BorderLayout.PAGE_START);
    }

    void createLeftSidePanel() {
        JPanel panelContainer = new JPanel();

        panelContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelContainer.setPreferredSize(new Dimension(700, this.frame.getHeight() * 5 / 6));

        JLabel bookedFlights = new JLabel("Booked Flights");
        bookedFlights.setFont(new Font("Georgia", Font.BOLD, 14));
        JScrollPane sidePanel = new JScrollPane();
        sidePanel.setLayout(new ScrollPaneLayout());
        sidePanel.setPreferredSize(new Dimension(panelContainer.getPreferredSize().width - 10,
                panelContainer.getPreferredSize().height - 20));
        sidePanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sidePanel.setWheelScrollingEnabled(true);
        addBookedFlights(sidePanel);

        panelContainer.add(bookedFlights);
        panelContainer.add(sidePanel);
        this.panel.add(panelContainer, BorderLayout.LINE_START);
    }

    void createRightSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GroupLayout layout = new GroupLayout(sidePanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JButton bookFlight = new JButton("Book new Flight");
        JButton accountInfo = new JButton("Account Information");

        BookedFlightPage.rightPanelWithButtons(sidePanel, layout, bookFlight, accountInfo, this.panel);
    }

 */

    /*
    REQUIRES: empty scrollPane
    EFFECTS: fills the scroll pane with different booked flights
     */
    void addBookedFlights(JScrollPane scrollPane) {
        List<BookedFlight> flightList = this.passenger.getBookedFlights();
        JPanel panel = new JPanel();
        if (flightList.isEmpty()) {
            panel.setLayout(new GridBagLayout());
            panel.add(new JLabel("You have no booked flights!"));
            return;
        }
        for (BookedFlight bookedFlight : flightList) {
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JButton button = new JButton(bookedFlight.getFlight().publicInfo());
            button.setPreferredSize(new Dimension(panel.getWidth(), 100));
            panel.add(button);
            button.addActionListener(e -> goToFlightPage(bookedFlight));
        }
        scrollPane.setViewportView(panel);
    }

    /*
    REQUIRES: chosen booked flight
    EFFECTS: directs the user to the flight page containing the chosen booked flight information
     */
    void goToFlightPage(BookedFlight flight) {
        this.frame.getContentPane().removeAll();
        this.frame.repaint();
        new BookedFlightPage(this.frame, this.account, this.flightSchedule, this.passenger, flight);
    }



}
