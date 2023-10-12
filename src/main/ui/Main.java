package ui;

import model.Account;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        new LoginPage(new JFrame(), new Account());
    }
}
