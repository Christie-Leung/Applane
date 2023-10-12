package ui;

import model.Account;
import model.FlightSchedule;
import model.Passenger;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// Represents an account editing page
public class EditAccountPage extends SignUpPage {

    private final Passenger passenger;

    // EFFECTS: constructs a new edit account page with given frame, account, flight schedule, passenger and
    //          calls to fill in all fields with passenger info
    public EditAccountPage(JFrame frame, Account account, FlightSchedule flightSchedule, Passenger passenger) {
        super(frame, account, flightSchedule);
        this.passenger = passenger;
        fillInFields();
    }

    /*
    MODIFIES: this
    EFFECTS: fills in all fields with passenger information
     */
    void fillInFields() {
        super.textFields.get(0).setText(this.passenger.getFirstName());
        super.textFields.get(1).setText(this.passenger.getMiddleName());
        super.textFields.get(2).setText(this.passenger.getLastName());
        super.textFields.get(3).setText(this.passenger.getEmailAddress());
        super.textFields.get(4).setText(this.passenger.getPassword());
        super.textFields.get(5).setText(this.passenger.getPhoneNumber());
        super.textFields.get(6).setText(
                this.passenger.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    /*
    EFFECTS: modifies the signup button to reflect on editing functions instead of signing up
     */
    @Override
    JPanel signUpButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setMaximumSize(new Dimension(this.frame.getWidth(), 100));
        JButton button = new JButton("Finish Editing");
        JLabel feedbackMsg = new JLabel();
        feedbackMsg.setForeground(Color.red);

        button.addActionListener(e -> buttonAction(feedbackMsg, this.passenger.getEmailAddress()));

        buttonPanel.add(button);
        buttonPanel.add(feedbackMsg);

        return buttonPanel;
    }

    /*
    REQUIRES: feedback message, user's old email
    MODIFIES: this
    EFFECTS: checks if user changed their email to reflect the change in all account and saves it
     */
    void buttonAction(JLabel feedbackMsg, String email) {
        for (int i = 0; i < this.textFields.size(); i++) {
            if (this.textFields.get(i).getText().isEmpty() && i != 1 && i != 6) {
                feedbackMsg.setText("You must fill in all fields with asterisks!");
                return;
            }
        }

        setPassengerFields(feedbackMsg);
        if (!this.textFields.get(3).getText().equals(email)) {
            this.account.deletePassenger(email);
            this.account.addPassenger(this.textFields.get(3).getText(), this.passenger);
        } else {
            this.account.replacePassenger(email, this.passenger);
        }
        this.frame.getContentPane().removeAll();
        this.frame.repaint();
        new AirplaneApp(this.frame, this.account, this.flightSchedule, this.passenger);
    }

    /*
    REQUIRES: feedback message
    MODIFIES: this
    EFFECTS: sets the passenger with user filled out fields and checks if the date of birth is in date format
     */
    void setPassengerFields(JLabel feedbackMsg) {
        this.passenger.setFirstName(this.textFields.get(0).getText());
        this.passenger.setMiddleName(this.textFields.get(1).getText());
        this.passenger.setLastName(this.textFields.get(2).getText());
        this.passenger.setEmailAddress(this.textFields.get(3).getText());
        this.passenger.setPassword(this.textFields.get(4).getText());
        this.passenger.setPhoneNumber(this.textFields.get(5).getText());

        try {
            LocalDate dateOfBirth = LocalDate.parse(this.textFields.get(6).getText(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.passenger.setDateOfBirth(dateOfBirth);
        } catch (DateTimeParseException dateTimeParseException) {
            feedbackMsg.setForeground(Color.red);
            feedbackMsg.setText("Invalid Date of Birth! Please put it in the format (yyyy-MM-dd)");
        }
    }

}
