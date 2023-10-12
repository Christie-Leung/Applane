package ui;

import model.Account;
import model.Event;
import model.EventLog;
import model.FlightSchedule;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

// Represents a login page
public class LoginPage {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;

    private static final String JSON_ACCOUNTS = "./data/accounts.json";
    private static final String JSON_FLIGHTS = "./data/flights.json";

    private final JsonWriter jsonWriterAccounts;
    private final JsonReader jsonReaderAccounts;

    private final JsonWriter jsonWriterFlights;
    private final JsonReader jsonReaderFlights;

    private FlightSchedule flightSchedule;

    private Account account;

    private final JFrame frame;

    // EFFECTS: constructs a new airplane app with a json writer and reader for accounts and flights
    //          automatically loads accounts and flights from files and goes to the menu
    public LoginPage(JFrame frame, Account account) {
        this.account = account;
        this.flightSchedule = new FlightSchedule();
        this.jsonWriterFlights = new JsonWriter(JSON_FLIGHTS);
        this.jsonReaderFlights = new JsonReader(JSON_FLIGHTS);
        this.jsonWriterAccounts = new JsonWriter(JSON_ACCOUNTS);
        this.jsonReaderAccounts = new JsonReader(JSON_ACCOUNTS);
        this.frame = frame;
        loadFlights();
        saveFlights();
        loadAndSave();
        // menu();
        initializeGraphics();
    }

    /*
    EFFECTS: load and saves accounts
     */
    void loadAndSave() {
        if (this.account.accounts.isEmpty()) {
            loadAccounts();
        }
        saveAccounts();
    }

    /*
    MODIFIES: this
    EFFECTS: changes settings of the frame and initialize the page
     */
    void initializeGraphics() {
        this.frame.setLayout(new BorderLayout());
        this.frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        this.frame.setTitle("Airplane App");
        this.frame.setVisible(true);
        this.frame.setLocationRelativeTo(null);
        this.frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                saveAccounts();
                saveFlights();
                EventLog eventLog = EventLog.getInstance();
                for (Event next : eventLog) {
                    System.out.println(next.toString() + "\n");
                }
                System.exit(0);
            }
        });
        loginMenu();
    }

    /*
    MODIFIES: this
    EFFECTS: creates a login page
     */
    void loginMenu() {
        JPanel logIn = new JPanel();
        logIn.setLayout(new BoxLayout(logIn, BoxLayout.Y_AXIS));

        logIn.add(Box.createVerticalGlue());
        logIn.add(createIntroTopFields());
        logIn.add(createLoginFields());
        logIn.add(createIntroBotFields());
        logIn.add(Box.createVerticalGlue());

        this.frame.add(logIn);
        this.frame.validate();
    }

    /*
    EFFECTS: returns a panel with the app title and login label that will be at the top of the page
     */
    JPanel createIntroTopFields() {
        JPanel intro = new JPanel();
        intro.setLayout(new BoxLayout(intro, BoxLayout.Y_AXIS));

        JLabel appTitle = new JLabel();
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        appTitle.setText("APPLANE");
        appTitle.setFont(new Font("Serif", Font.BOLD, 24));

        JLabel loginText = new JLabel();
        loginText.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginText.setText("Login");
        loginText.setFont(new Font("Serif", Font.BOLD, 14));

        intro.add(Box.createVerticalGlue());
        intro.add(appTitle);
        intro.add(Box.createVerticalStrut(20));
        intro.add(loginText);
        intro.add(Box.createVerticalStrut(10));

        return intro;
    }

    /*
    EFFECTS: returns the login fields where user fills in their username (email) and password
     */
    JPanel createLoginFields() {
        JPanel loginField = new JPanel();
        loginField.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        loginField.setLayout(new BoxLayout(loginField, BoxLayout.Y_AXIS));

        JPanel userEmail = new JPanel();
        JTextField userField = new JTextField("Email Address", 22);
        userField.addFocusListener(tabbedInTextField(userField, "Email Address"));
        userEmail.add(userField);

        JPanel userPass = new JPanel();
        JTextField passField = new JTextField("Password", 22);
        passField.addFocusListener(tabbedInTextField(passField, "Password"));
        userPass.add(passField);

        JPanel buttonPanel = createLoginButton(userField, passField);

        loginField.add(userEmail);
        loginField.add(userPass);
        loginField.add(buttonPanel);

        return loginField;
    }

    /*
    REQUIRES: the page's email and password fields
    MODIFIES: this
    EFFECTS: creates the login button and sets an action listener then returns a panel containing the button
     */
    JPanel createLoginButton(JTextField userField, JTextField passField) {
        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("LOG IN");
        JLabel errorMsg = new JLabel();
        errorMsg.setForeground(Color.RED);
        loginButton.addActionListener(e -> {
            String msg = signIn(userField.getText(), passField.getText());
            if (msg.isEmpty()) {
                this.frame.getContentPane().removeAll();
                this.frame.repaint();
                new AirplaneApp(this.frame, this.account, this.flightSchedule,
                        this.account.retrievePassenger(userField.getText()));
            } else {
                errorMsg.setText(msg);
            }
        });
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(errorMsg);
        return buttonPanel;
    }

    /*
    REQUIRES: text fields and text displayed when tabbed into the field
    EFFECTS: change the field to empty when the user clicks into it and changes it back if the user
             does not type anything
     */
    FocusListener tabbedInTextField(JTextField field, String text) {
        return new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(text);
                }
            }
        };
    }

    /*
    MODIFIES: this
    EFFECTS: creates the bottom panel with a signup button
     */
    JPanel createIntroBotFields() {
        JPanel signupField = new JPanel();
        JLabel signupText = new JLabel("Don't have an account?");

        JButton signupButton = new JButton("Sign up here!");

        signupField.add(signupText);
        signupField.add(signupButton);

        signupButton.addActionListener(e -> {
            this.frame.getContentPane().removeAll();
            this.frame.repaint();
            new SignUpPage(this.frame, this.account, this.flightSchedule);
        });

        return signupField;
    }

    // EFFECTS: checks if user has already registered an account and check against their password
    String signIn(String email, String password) {
        int statusCode = account.matchPassword(email, password);

        switch (statusCode) {
            case 1:
                return "";
            case 0:
                return "Invalid password! Please try again.";
            case -1:
                return "Credentials not found!";
        }
        return "";
    }

    /*
    // EFFECTS: welcomes user to application and ask them to sign in, register, or exit
    void menu() {
        System.out.println("Welcome to the airplane application!");
        System.out.print("Do you have an account with us?\n"
                + "1) Yes\n"
                + "2) No \n"
                + "3) Exit Program and Save Files\n"
                + "Please input your choice: ");
        int option = getOption(3);
        switch (option) {
            case 1:
                signIn();
                break;
            case 2:
                register();
                break;
            case 3:
                exit();
                break;
        }
        menu();
    }

    // MODIFIES: accounts
    // EFFECTS: registers the user as a passenger with an account
    void register() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("First Name: ");
        String firstName = scanner.next();
        System.out.print("Last Name: ");
        String lastName = scanner.next();
        System.out.print("Date of Birth (yyyy-MM-dd): ");
        LocalDate dateOfBirth = LocalDate.parse(scanner.next(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.print("Phone Number: ");
        String phoneNumber = scanner.next();
        System.out.print("Email Address: ");
        String email = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();

        Passenger passenger = new Passenger(firstName, lastName, email, password, dateOfBirth, phoneNumber);
        accounts.addPassenger(email, passenger);
        System.out.println("\n~~ Successful! ~~");
        optionMenu(passenger);
    }

    // REQUIRES: user
    // EFFECTS: prints out the option menu once users have logged in, directs them to their chosen function
    void optionMenu(Passenger user) {
        System.out.println("\nWhat would you like to do today?\n"
                + "1) Book a flight\n"
                + "2) View all booked flights\n"
                + "3) Cancel a flight\n"
                + "4) Log out\n"
                + "5) Exit program and Save Files");

        switch (getOption(5)) {
            case 1:
                searchFlight(user);
                break;
            case 2:
                viewMyFlights(user);
                break;
            case 3:
                cancelFlight(user);
                break;
            case 4:
                menu();
                break;
            case 5:
                exit();
        }
        menu();
    }

    // EFFECTS: saves accounts and flights before exiting
    void exit() {
        saveAccounts();
        saveFlights();
        System.exit(0);
    }

    // REQUIRES: user
    // EFFECTS: searches through flight schedule depending on where the user wants to travel from and to
    //          allow user to view more information on desired flight and direct them to book the flight
    void searchFlight(Passenger user) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Starting destination (airport code): ");
        String startDestination = scanner.next().toUpperCase();
        System.out.print("End destination (airport code): ");
        String endDestination = scanner.next().toUpperCase();
        List<Flight> availableFlights = flightSchedule.getFlightsByDestination(startDestination, endDestination);
        if (!availableFlights.isEmpty()) {
            System.out.println("Here are all available flights: ");
            for (int i = 1; i <= availableFlights.size(); i++) {
                System.out.println(i + ") " + availableFlights.get(i - 1).publicInfo() + "\n");
            }
            System.out.print("\nWhich flight would you like to view more information on?"
                    + "\nPlease input a number: ");
            int option = getOption(availableFlights.size());
            bookFlight(user, availableFlights.get(option - 1));

        } else {
            System.out.println("Sorry! There are currently no scheduled flights from "
                    + startDestination + " to " + endDestination);
        }
        optionMenu(user);
    }

    // REQUIRES: user, flight
    // MODIFIES: user
    // EFFECTS: allows registered user to book their desired flight with seat options (economy, business, first class)
    void bookFlight(Passenger user, Flight flight) {
        System.out.println("\nYour selected flight information: \n\n" + flight.toString());
        System.out.println("\n Would you like to continue with booking? "
                + "\n1) Yes"
                + "\n2) No");
        int option = getOption(2);
        if (option == 1) {
            System.out.println("\nWhat seat would you like?\n"
                    + "1) Economy [$" + flight.getEconomyPrice() + "]\n"
                    + "2) Business[$" + flight.getBusinessPrice() + "]\n"
                    + "3) First Class[$" + flight.getFirstClassPrice() + "]");
            int seat = getOption(3);
            user.addBookedFlight(flight, seat);

            System.out.println("Successfully booked Flight " + flight.getAirlineCode()
                    + flight.getFlightNumber() + "!");
        }
    }

    // REQUIRES: user
    // EFFECTS: displays all user's booked flights
    void viewMyFlights(Passenger user) {
        List<BookedFlight> bookedFlights = user.getBookedFlights();
        if (bookedFlights.isEmpty()) {
            System.out.println("You have no booked flights!");
        } else {
            for (BookedFlight bookedFlight : bookedFlights) {
                System.out.println(bookedFlight.getFlight().publicInfo());
            }
        }
        optionMenu(user);
    }

    // REQUIRES: user
    // MODIFIES: user
    // EFFECTS: cancels a registered user's flight and remove the flight from their booked flight
    void cancelFlight(Passenger user) {
        List<BookedFlight> bookedFlights = user.getBookedFlights();
        if (bookedFlights.isEmpty()) {
            System.out.println("You have no booked flights!");
        } else {
            System.out.println("Which flight would you like to cancel?");
            for (int i = 1; i <= bookedFlights.size(); i++) {
                System.out.println(i + ") " + bookedFlights.get(i - 1).getFlight().publicInfo() + "\n");
            }
            System.out.print("Please input a number: ");
            int choice = getOption(bookedFlights.size());
            Flight flight = bookedFlights.get(choice - 1).getFlight();
            user.removeBookedFlight(flight.getId());
            System.out.println("\nSuccessfully removed Flight " + flight.getAirlineCode()
                    + flight.getFlightNumber() + "!");
        }
        optionMenu(user);
    }

    // REQUIRES: max options >= 2
    // EFFECTS: checks if user input is within the range and produces the final option choice
    int getOption(int max) {
        Scanner scanner = new Scanner(System.in);
        int option = checkInt(scanner.next());
        if (1 <= option && option <= max) {
            return option;
        } else {
            System.out.printf("Please input a number between 1 - %s:", max);
            return getOption(max);
        }
    }

    // REQUIRES: string with the input
    // EFFECTS: checks if the string is a number.
    int checkInt(String stringToCheck) {
        int num;
        try {
            num = Integer.parseInt(stringToCheck);
        } catch (NumberFormatException e) {
            System.out.println("Please input a number.");
            Scanner scanner = new Scanner(System.in);
            return checkInt(scanner.next());
        }
        return num;
    }

     */

    // EFFECTS: saves accounts to file
    void saveAccounts() {
        try {
            jsonWriterAccounts.open();
            jsonWriterAccounts.write(account);
            jsonWriterAccounts.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_ACCOUNTS);
        }
    }

    // MODIFIES: accounts
    // EFFECTS: loads accounts from file
    void loadAccounts() {
        try {
            account = jsonReaderAccounts.readAccounts();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_ACCOUNTS);
        }
    }

    // MODIFIES: flightSchedule
    // EFFECTS: loads flights from file
    private void loadFlights() {
        try {
            flightSchedule = jsonReaderFlights.readFlights();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_FLIGHTS);
        }
    }

    // EFFECTS: saves flights to file
    private void saveFlights() {
        try {
            jsonWriterFlights.open();
            jsonWriterFlights.write(flightSchedule);
            jsonWriterFlights.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_FLIGHTS);
        }
    }
}
