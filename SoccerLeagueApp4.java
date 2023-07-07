package ruchita;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SoccerLeagueApp4 extends JFrame implements ActionListener {
    private JTextField matchIdField, team1Field, team2Field, wonTeamField;
    private JTable displayTable;
    private JButton addButton, displayButton, deleteButton, modifyButton;
    private Connection connection;
    private DefaultTableModel tableModel;

    public SoccerLeagueApp4() {
        setTitle("Soccer League App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create labels and text fields for data entry
        JLabel matchIdLabel = new JLabel("Match ID:");
        matchIdField = new JTextField(10);

        JLabel team1Label = new JLabel("Team 1 ID:");
        team1Field = new JTextField(10);

        JLabel team2Label = new JLabel("Team 2 ID:");
        team2Field = new JTextField(10);

        JLabel wonTeamLabel = new JLabel("Won Team ID:");
        wonTeamField = new JTextField(10);

        // Create buttons for adding, displaying, deleting, and modifying data
        addButton = new JButton("Add Match");
        addButton.addActionListener(this);

        displayButton = new JButton("Display Matches");
        displayButton.addActionListener(this);

        deleteButton = new JButton("Delete Match");
        deleteButton.addActionListener(this);

        modifyButton = new JButton("Modify Match");
        modifyButton.addActionListener(this);

        // Create table model and table for displaying data
        tableModel = new DefaultTableModel();
        displayTable = new JTable(tableModel);
        displayTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow selection of only one row
        displayTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = displayTable.getSelectedRow();
            if (selectedRow != -1) {
                matchIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                team1Field.setText(tableModel.getValueAt(selectedRow, 1).toString());
                team2Field.setText(tableModel.getValueAt(selectedRow, 2).toString());
                wonTeamField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            }
        });
        JScrollPane scrollPane = new JScrollPane(displayTable);

        // Create main content pane and add components
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(matchIdLabel);
        inputPanel.add(matchIdField);
        inputPanel.add(team1Label);
        inputPanel.add(team1Field);
        inputPanel.add(team2Label);
        inputPanel.add(team2Field);
        inputPanel.add(wonTeamLabel);
        inputPanel.add(wonTeamField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(modifyButton);

        contentPane.add(inputPanel, BorderLayout.NORTH);
        contentPane.add(buttonPanel, BorderLayout.CENTER);
        contentPane.add(scrollPane, BorderLayout.SOUTH);

        pack();
        setVisible(true);

        // Establish database connection
        try {
            Class.forName("oracle.jdbc.OracleDriver").newInstance();
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "ruchita", "ruchita12345");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addMatch();
        } else if (e.getSource() == displayButton) {
            displayMatches();
        } else if (e.getSource() == deleteButton) {
            deleteMatch();
        } else if (e.getSource() == modifyButton) {
            modifyMatch();
        }
    }

    private void addMatch() {
        try {
            String matchId = matchIdField.getText();
            String team1Id = team1Field.getText();
            String team2Id = team2Field.getText();
            String wonTeamId = wonTeamField.getText();

            String sql = "INSERT INTO Match(MATCH_ID, TEAM1_ID, TEAM2_ID, WON_TEAM_ID) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, matchId);
            statement.setString(2, team1Id);
            statement.setString(3, team2Id);
            statement.setString(4, wonTeamId);
            statement.executeUpdate();

            // Clear the input fields
            matchIdField.setText("");
            team1Field.setText("");
            team2Field.setText("");
            wonTeamField.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void displayMatches() {
        try {
            String sql = "SELECT * FROM Match";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            tableModel.setRowCount(0); // Clear existing data

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Set column names
            String[] columns = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columns[i - 1] = metaData.getColumnName(i);
            }
            tableModel.setColumnIdentifiers(columns);

            // Add rows to the table
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            resultSet.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteMatch() {
        try {
            String matchId = matchIdField.getText();

            String sql = "DELETE FROM Match WHERE MATCH_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, matchId);
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                matchIdField.setText("");
                team1Field.setText("");
                team2Field.setText("");
                wonTeamField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "No match found with Match ID=" + matchId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void modifyMatch() {
        try {
            String matchId = matchIdField.getText();
            String team1Id = team1Field.getText();
            String team2Id = team2Field.getText();
            String wonTeamId = wonTeamField.getText();

            String sql = "UPDATE Match SET TEAM1_ID = ?, TEAM2_ID = ?, WON_TEAM_ID = ? WHERE MATCH_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, team1Id);
            statement.setString(2, team2Id);
            statement.setString(3, wonTeamId);
            statement.setString(4, matchId);
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                matchIdField.setText("");
                team1Field.setText("");
                team2Field.setText("");
                wonTeamField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "No match found with Match ID=" + matchId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SoccerLeagueApp4());
    }
}
