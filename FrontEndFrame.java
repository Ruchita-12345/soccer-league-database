package ruchita;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrontEndFrame extends JFrame {
    private FrontEndFrame() {
        setTitle("Soccer League Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        // Create menu items
        JMenuItem teamsMenuItem = new JMenuItem("Teams");
        JMenuItem standingsMenuItem = new JMenuItem("Standings");
        JMenuItem matchMenuItem = new JMenuItem("Match");
        JMenuItem playerMenuItem = new JMenuItem("Player");

        // Add menu items to the file menu
        fileMenu.add(teamsMenuItem);
        fileMenu.add(standingsMenuItem);
        fileMenu.add(matchMenuItem);
        fileMenu.add(playerMenuItem);

        // Add file menu to the menu bar
        menuBar.add(fileMenu);

        // Add menu bar to the frame
        setJMenuBar(menuBar);

        // Action listener for menu items
        ActionListener menuActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JMenuItem menuItem = (JMenuItem) e.getSource();
                String itemName = menuItem.getText();

                switch (itemName) {
                    case "Teams":
                        TeamsTable teamsTable = new TeamsTable();
                        teamsTable.setVisible(true);
                        break;
                    case "Standings":
                        SoccerLeagueStandingsApp standingsApp = new SoccerLeagueStandingsApp();
                        standingsApp.setVisible(true);
                        break;
                    case "Match":
                        SoccerLeagueApp4 leagueApp = new SoccerLeagueApp4();
                        leagueApp.setVisible(true);
                        break;
                    case "Player":
                    	PlayerManagementApp playerApp = new PlayerManagementApp();
                        playerApp.setVisible(true);
                        break;
                    default:
                        break;
                }
            }
        };

        // Add action listener to menu items
        teamsMenuItem.addActionListener(menuActionListener);
        standingsMenuItem.addActionListener(menuActionListener);
        matchMenuItem.addActionListener(menuActionListener);
        playerMenuItem.addActionListener(menuActionListener);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FrontEndFrame();
            }
        });
    }
}
