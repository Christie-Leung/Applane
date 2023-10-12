package ui;

import model.Account;
import model.FlightSchedule;
import model.Passenger;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


// Represents the account info page that takes up a border layout format
public class AccountInfoPage extends BorderLayoutPage {

    private final JFrame frame;
    private JPanel panel;
    private final Passenger passenger;
    Account account;
    FlightSchedule flightschedule;

    // EFFECTS: Constructs a new account info page with the given frame, account, flight schedule and passenger
    //          makes a new panel with a border layout
    public AccountInfoPage(JFrame frame, Account account, FlightSchedule flightschedule, Passenger passenger) {
        this.frame = frame;
        this.panel = new JPanel(new BorderLayout());
        this.passenger = passenger;
        this.account = account;
        this.flightschedule = flightschedule;
        initializePage();
    }

    /*
    MODIFIES: this
    EFFECTS: initializes the account info page with different panels taken from border layout functions
     */
    void initializePage() {
        this.panel = createTopPanel(this.panel);

        JPanel container = createInfoPanel("Account Overview", this.passenger.toString());
        this.panel = createLeftSidePanel(this.panel, this.frame.getHeight(), new JLabel(), container);

        JButton homePage = new JButton("Back to Home Page");
        JButton editAccount = new JButton("Edit Account Information");
        homePage.addActionListener(e -> {
            this.frame.getContentPane().removeAll();
            this.frame.repaint();
            new AirplaneApp(this.frame, this.account, this.flightschedule, this.passenger);
        });
        editAccount.addActionListener(e -> {
            this.frame.getContentPane().removeAll();
            this.frame.repaint();
            new EditAccountPage(this.frame, this.account, this.flightschedule, this.passenger);
        });
        JButton delAccount = new JButton("Delete Account");
        delAccount.addActionListener(e -> deleteAccountPopup());

        this.panel = createRightSidePanel(this.panel, Arrays.asList(homePage, editAccount, delAccount));

        this.frame.add(panel);
        this.frame.validate();
    }

    /*
    EFFECTS: creates a popup frame to confirm deletion an account
     */
    void deleteAccountPopup() {
        JFrame popup = new JFrame();
        popup.setSize(new Dimension(300, 110));
        popup.setVisible(true);
        popup.setLocationRelativeTo(null);
        JPanel container = new JPanel();
        JPanel buttons = new JPanel();
        JLabel label = new JLabel("Are you sure you want to delete your account?");
        JButton yes = new JButton("Yes");
        JButton no = new JButton("No");
        buttons.add(yes);
        yes.addActionListener(e -> deleteAccount(popup, container));
        buttons.add(no);
        no.addActionListener(e -> popup.dispose());
        container.add(label);
        container.add(buttons);
        popup.add(container);
    }

    /*
    REQUIRES: popup frame and the container within the frame
    EFFECTS: confirms that the account has been deleted and deletes the account
     */
    void deleteAccount(JFrame popup, JPanel container) {
        this.account.deletePassenger(this.passenger.getEmailAddress());
        container.removeAll();
        container.add(new JLabel("Successfully removed and saved!"));
        popup.getContentPane().removeAll();
        popup.repaint();
        popup.add(container);
        popup.validate();
        this.frame.getContentPane().removeAll();
        new LoginPage(this.frame, this.account);
    }




}
