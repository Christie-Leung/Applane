package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;

// Represents a border layout template that can be used by other pages
public class BorderLayoutPage {

    /*
    REQUIRES: panel to be added on with border layout
    EFFECTS: creates the top banner that holds the app title in given font and colour
     */
    JPanel createTopPanel(JPanel panel) {
        JPanel topPanel = new JPanel();
        JLabel appTitle = new JLabel("APPLANE");
        appTitle.setFont(new Font("Serif", Font.BOLD, 24));
        topPanel.add(appTitle);
        topPanel.setBackground(new Color(0xC1C1EF));

        panel.add(topPanel, BorderLayout.PAGE_START);
        return panel;
    }

    /*
    REQUIRES: root panel with border layout, frameHeight, title as a label, and an info panel that holds all needed info
    EFFECTS: creates a left panel with all given information
     */
    JPanel createLeftSidePanel(JPanel rootPanel, int frameHeight, JLabel label, JPanel infoPanel) {
        JPanel panelContainer = new JPanel();

        panelContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelContainer.setPreferredSize(new Dimension(700, frameHeight * 5 / 6));

        panelContainer.add(label);
        panelContainer.add(infoPanel);
        rootPanel.add(panelContainer, BorderLayout.LINE_START);
        return rootPanel;
    }

    /*
    REQUIRES: root panel with border layout, dimension, title as a label, and an info panel that holds all needed info
              as a scroll pane
    EFFECTS: create a left side panel with a scroll pane holding the needed information
     */
    JPanel createLeftSidePanel(JPanel rootPanel, Dimension dimension, JLabel label,
                               JScrollPane infoPanel) {
        JPanel panelContainer = new JPanel();

        panelContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelContainer.setPreferredSize(dimension);

        panelContainer.add(label);
        panelContainer.add(infoPanel);
        rootPanel.add(panelContainer, BorderLayout.LINE_START);
        return rootPanel;
    }

    /*
    REQUIRES: panel to be added on with border layout and a list of buttons to add on the panel
    EFFECTS: creates a right panel with given buttons
     */
    JPanel createRightSidePanel(JPanel panel, List<JButton> buttons) {
        JPanel sidePanel = new JPanel();
        sidePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GroupLayout layout = new GroupLayout(sidePanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        GroupLayout.ParallelGroup horizontalGroup = layout.createParallelGroup(LEADING);
        GroupLayout.SequentialGroup verticalGroup = layout.createSequentialGroup();
        for (JButton button : buttons) {
            horizontalGroup.addComponent(button);
            verticalGroup.addGroup(layout.createParallelGroup(BASELINE).addComponent(button));
        }

        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(horizontalGroup));

        layout.setVerticalGroup(verticalGroup);
        sidePanel.setLayout(layout);

        panel.add(sidePanel);
        return panel;
    }

    /*
    REQUIRES: non-null string for a label title and a string containing information that is needed to be shown
    EFFECTS: create a professional looking info panel with hard set fonts and text sizes
     */
    JPanel createInfoPanel(String labelTitle, String labelInfo) {
        JLabel title = new JLabel(labelTitle);
        title.setFont(new Font("Georgia", Font.BOLD, 24));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(labelInfo);
        label.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        container.add(title);
        container.add(Box.createVerticalStrut(5));
        container.add(label);
        return container;
    }
}
