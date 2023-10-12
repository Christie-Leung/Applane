package ui;

import model.Account;
import model.FlightSchedule;
import model.Passenger;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

// Represents a signup page
public class SignUpPage {

    final JFrame frame;
    List<JTextField> textFields;
    Account account;
    FlightSchedule flightSchedule;

    /*
    EFFECTS: creates a signup page given the frame, account and flight schedule and calls to initialize the page
     */
    public SignUpPage(JFrame frame, Account account, FlightSchedule flightSchedule) {
        this.frame = frame;
        this.account = account;
        this.flightSchedule = flightSchedule;
        initializePage();
    }

    /*
    MODIFIES: this
    EFFECTS: creates the signup fields to make a new account
     */
    void initializePage() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        JFormattedTextField dateTextField = new JFormattedTextField(format);
        dateTextField.setColumns(20);

        this.textFields = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            textFields.add(new JTextField(20));
        }
        this.textFields.add(dateTextField);

        List<JLabel> labels = new ArrayList<>();

        labels.add(new JLabel("First name*:"));
        labels.add(new JLabel("Middle:"));
        labels.add(new JLabel("Last name*:"));
        labels.add(new JLabel("Email address*:"));
        labels.add(new JLabel("Create a password*:"));
        labels.add(new JLabel("Phone Number*:"));
        labels.add(new JLabel("Date of Birth*:"));

        this.frame.add(createPanel(this.textFields, labels), BorderLayout.CENTER);
        this.frame.validate();
    }

    /*
    REQUIRES: the list of text fields and labels for creating an account
    EFFECTS: creates the panel that holds all text fields holding account information
     */
    JPanel createPanel(List<JTextField> textFields, List<JLabel> labels) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel namePanel = new JPanel();
        JPanel emailPassPanel = new JPanel();
        JPanel dobPhonePanel = new JPanel();

        for (int i = 0; i < 3; i++) {
            namePanel.add(createTextPanels(labels.get(i), textFields.get(i)));
        }
        for (int i = 3; i < 5; i++) {
            emailPassPanel.add(createTextPanels(labels.get(i), textFields.get(i)));
        }
        for (int i = 5; i < 7; i++) {
            dobPhonePanel.add(createTextPanels(labels.get(i), textFields.get(i)));
        }

        panel.add(Box.createVerticalGlue());
        panel.add(namePanel);
        panel.add(dobPhonePanel);
        panel.add(emailPassPanel);
        panel.add(signUpButtonPanel());
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /*
    REQUIRES: a label and text field
    EFFECTS: create a text panel that includes the label and the text field
     */
    JPanel createTextPanels(JLabel label, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setMaximumSize(new Dimension(this.frame.getWidth(), 100));
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    /*
    EFFECTS: creates the signup button panel
     */
    JPanel signUpButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setMaximumSize(new Dimension(this.frame.getWidth(), 100));
        JButton button = new JButton("Sign up!");
        JLabel feedbackMsg = new JLabel();
        feedbackMsg.setForeground(Color.red);

        button.addActionListener(e -> buttonAction(feedbackMsg));

        buttonPanel.add(button);
        buttonPanel.add(feedbackMsg);

        return buttonPanel;
    }

    /*
    REQUIRES: feedback msg label
    MODIFIES: this
    EFFECTS: when the user fills in all information and signs up, this checks for if all requires fields are
             filled in and creates a new passenger and add it to list of accounts,
             else, print an error message
     */
    void buttonAction(JLabel feedbackMsg) {
        for (int i = 0; i < this.textFields.size(); i++) {
            if (this.textFields.get(i).getText().isEmpty() && i != 1 && i != 6) {
                feedbackMsg.setText("You must fill in all fields with asterisks!");
                return;
            }
        }

        LocalDate dateOfBirth;
        try {
            dateOfBirth = LocalDate.parse(this.textFields.get(6).getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Passenger passenger = new Passenger(this.textFields.get(0).getText(), this.textFields.get(2).getText(),
                    this.textFields.get(3).getText(), this.textFields.get(4).getText(),
                    dateOfBirth, this.textFields.get(5).getText());
            passenger.setMiddleName(this.textFields.get(1).getText());
            this.account.addPassenger(this.textFields.get(3).getText(), passenger);
            this.frame.getContentPane().removeAll();
            this.frame.repaint();
            new AirplaneApp(this.frame, this.account, this.flightSchedule, passenger);
        } catch (DateTimeParseException dateTimeParseException) {
            feedbackMsg.setForeground(Color.red);
            feedbackMsg.setText("Invalid Date of Birth! Please put it in the format (yyyy-MM-dd)");
        }
    }

    /*
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
     */

}
