package ruchita;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

public class PlayerManagementApp extends JFrame {
    private JTextField playerIdField, firstNameField, lastNameField, dobField, nationalityField, teamIdField;
    private JButton addButton, displayButton, deleteButton, modifyButton;
    private JTable table;
    private DefaultTableModel model;

    public PlayerManagementApp() {
        setTitle("Player Management App");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        playerIdField = new JTextField(10);
        firstNameField = new JTextField(10);
        lastNameField = new JTextField(10);
        dobField = new JTextField(10);
        nationalityField = new JTextField(10);
        teamIdField = new JTextField(10);

        addButton = new JButton("Add");
        displayButton = new JButton("Display");
        deleteButton = new JButton("Delete");
        modifyButton = new JButton("Modify");

        // Create table model
        model = new DefaultTableModel();
        model.addColumn("Player ID");
        model.addColumn("First Name");
        model.addColumn("Last Name");
        model.addColumn("Date of Birth");
        model.addColumn("Nationality");
        model.addColumn("Team ID");

        table = new JTable(model);

        // Add table click listener
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    playerIdField.setText(table.getValueAt(selectedRow, 0).toString());
                    firstNameField.setText(table.getValueAt(selectedRow, 1).toString());
                    lastNameField.setText(table.getValueAt(selectedRow, 2).toString());
                    dobField.setText(table.getValueAt(selectedRow, 3).toString());
                    nationalityField.setText(table.getValueAt(selectedRow, 4).toString());
                    teamIdField.setText(table.getValueAt(selectedRow, 5).toString());
                }
            }
        });

        setLayout(new FlowLayout());

        add(new JLabel("Player ID: "));
        add(playerIdField);
        add(new JLabel("First Name: "));
        add(firstNameField);
        add(new JLabel("Last Name: "));
        add(lastNameField);
        add(new JLabel("Date of Birth: "));
        add(dobField);
        add(new JLabel("Nationality: "));
        add(nationalityField);
        add(new JLabel("Team ID: "));
        add(teamIdField);
        add(addButton);
        add(displayButton);
        add(deleteButton);
        add(modifyButton);
        add(new JScrollPane(table));

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPlayer();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayPlayers();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePlayer();
            }
        });

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyPlayer();
            }
        });
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "ruchita";
        String password = "ruchita12345";
        return DriverManager.getConnection(url, username, password);
    }

    private void addPlayer() {
        try (Connection conn = getConnection()) {
            String query = "INSERT INTO player (playerid, firstname, lastname, dateofbirth, nationality, teamid) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, playerIdField.getText());
            statement.setString(2, firstNameField.getText());
            statement.setString(3, lastNameField.getText());
            statement.setString(4, dobField.getText().trim().toString()); // Modified line
            statement.setString(5, nationalityField.getText());
            statement.setString(6, teamIdField.getText());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Player added successfully!");
                clearFields();
                displayPlayers();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding player: " + e.getMessage());
        }
    }

    private void displayPlayers() {
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM player";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            model.setRowCount(0); 

            while (resultSet.next()) {
                String playerId = resultSet.getString("playerid");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String dob = resultSet.getString("dateofbirth"); // Modified line
                String nationality = resultSet.getString("nationality");
                String teamId = resultSet.getString("teamid");

                model.addRow(new Object[]{playerId, firstName, lastName, dob, nationality, teamId});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error displaying players: " + e.getMessage());
        }
    }

    private void deletePlayer() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String playerId = table.getValueAt(selectedRow, 0).toString();
            try (Connection conn = getConnection()) {
                String query = "DELETE FROM player WHERE playerid = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, playerId);

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(null, "Player deleted successfully!");
                    clearFields();
                    displayPlayers();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error deleting player: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No player selected!");
        }
    }

    private void modifyPlayer() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String playerId = table.getValueAt(selectedRow, 0).toString();
            try (Connection conn = getConnection()) {
                String query = "UPDATE player SET firstname = ?, lastname = ?, dateofbirth = ?, nationality = ?, teamid = ? WHERE playerid = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, firstNameField.getText());
                statement.setString(2, lastNameField.getText());
                statement.setString(3, dobField.getText().trim().toString()); // Modified line
                statement.setString(4, nationalityField.getText());
                statement.setString(5, teamIdField.getText());
                statement.setString(6, playerId);

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(null, "Player modified successfully!");
                    clearFields();
                    displayPlayers();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error modifying player: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No player selected!");
        }
    }

    private void clearFields() {
        playerIdField.setText("");
       


        firstNameField.setText("");
        lastNameField.setText("");
        dobField.setText("");
        nationalityField.setText("");
        teamIdField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PlayerManagementApp().setVisible(true);
            }
        });
    }
}
