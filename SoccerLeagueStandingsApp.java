package ruchita;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class SoccerLeagueStandingsApp extends JFrame implements ActionListener {
    private JTextField idField, teamIdField, matchesPlayedField, winsField, drawsField, lossesField, goalsForField, goalsAgainstField, pointsField;
    private JButton addButton, deleteButton, modifyButton, displayButton;
    private JTextArea resultArea;

    private Connection connection;
    private PreparedStatement preparedStatement;

    private int selectedStandingsId; // New variable to store the selected standings ID

    public SoccerLeagueStandingsApp() {
        setTitle("Soccer League Standings");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Standings ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        idField = new JTextField();
        idField.setPreferredSize(new Dimension(200, 25)); // Set preferred size for the JTextField
        inputPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Team ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        teamIdField = new JTextField();
        teamIdField.setPreferredSize(new Dimension(200, 25)); // Set preferred size for the JTextField
        inputPanel.add(teamIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Matches Played:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        matchesPlayedField = new JTextField();
        matchesPlayedField.setPreferredSize(new Dimension(200, 25)); // Set preferred size for the JTextField
        inputPanel.add(matchesPlayedField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Wins:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        winsField = new JTextField();
        winsField.setPreferredSize(new Dimension(200, 25)); // Set preferred size for the JTextField
        inputPanel.add(winsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Draws:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        drawsField = new JTextField();
        drawsField.setPreferredSize(new Dimension(200, 25)); // Set preferred size for the JTextField
        inputPanel.add(drawsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(new JLabel("Losses:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        lossesField = new JTextField();
        lossesField.setPreferredSize(new Dimension(200, 25)); // Set preferred size for the JTextField
        inputPanel.add(lossesField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        inputPanel.add(new JLabel("Goals For:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        goalsForField = new JTextField();
       goalsForField.setPreferredSize(new Dimension(200, 25)); // Set preferred size for the JTextField
        inputPanel.add(goalsForField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        inputPanel.add(new JLabel("Goals Against:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        goalsAgainstField = new JTextField();
        goalsAgainstField.setPreferredSize(new Dimension(200, 25)); // Set preferred size for the JTextField
        inputPanel.add(goalsAgainstField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        inputPanel.add(new JLabel("Points:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        pointsField = new JTextField();
        pointsField.setPreferredSize(new Dimension(200, 25)); // Set preferred size for the JTextField
        inputPanel.add(pointsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        inputPanel.add(addButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this);
        inputPanel.add(deleteButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        modifyButton = new JButton("Modify");
        modifyButton.addActionListener(this);
        inputPanel.add(modifyButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 10;
        displayButton = new JButton("Display");
        displayButton.addActionListener(this);
        inputPanel.add(displayButton, gbc);

        add(inputPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = resultArea.viewToModel(e.getPoint());
                int startIndex = resultArea.getText().substring(0, row).lastIndexOf("\n") + 1;
                int endIndex = resultArea.getText().indexOf("\n", row);
                String selectedRow = resultArea.getText().substring(startIndex, endIndex).trim();
                String[] rowData = selectedRow.split("\t");
                selectedStandingsId = Integer.parseInt(rowData[0]);
                idField.setText(rowData[0]);
                teamIdField.setText(rowData[1]);
                matchesPlayedField.setText(rowData[2]);
                winsField.setText(rowData[3]);
                drawsField.setText(rowData[4]);
                lossesField.setText(rowData[5]);
                goalsForField.setText(rowData[6]);
                goalsAgainstField.setText(rowData[7]);
                pointsField.setText(rowData[8]);
            }
        });
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(780, 480)); // Set preferred size for the scroll pane
        add(scrollPane, BorderLayout.CENTER);

        connectToDatabase();
    }

    private void connectToDatabase() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "ruchita", "ruchita12345");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addStandings() {
        int standingsId = Integer.parseInt(idField.getText());
        int teamId = Integer.parseInt(teamIdField.getText());
        int matchesPlayed = Integer.parseInt(matchesPlayedField.getText());
        int wins = Integer.parseInt(winsField.getText());
        int draws = Integer.parseInt(drawsField.getText());
        int losses = Integer.parseInt(lossesField.getText());
        int goalsFor= Integer.parseInt(goalsForField.getText());
        int goalsAgainst = Integer.parseInt(goalsAgainstField.getText());
        int points = Integer.parseInt(pointsField.getText());

        String query = "INSERT INTO STANDINGS (STANDINGSID, TEAMID, MATCHESPLAYED, WINS, DRAWS, LOSSES, GOALSFOR, GOALSAGAINST, POINTS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, standingsId);
            preparedStatement.setInt(2, teamId);
            preparedStatement.setInt(3, matchesPlayed);
            preparedStatement.setInt(4, wins);
            preparedStatement.setInt(5, draws);
            preparedStatement.setInt(6, losses);
            preparedStatement.setInt(7, goalsFor);
            preparedStatement.setInt(8, goalsAgainst);
            preparedStatement.setInt(9, points);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                clearFields();
                displayStandings();
                JOptionPane.showMessageDialog(this, "Standings added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteStandings() {
        int standingsId = Integer.parseInt(idField.getText());

        String query = "DELETE FROM STANDINGS WHERE STANDINGSID = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, standingsId);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                clearFields();
                displayStandings();
                JOptionPane.showMessageDialog(this, "Standings deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifyStandings() {
        int standingsId = Integer.parseInt(idField.getText());
        int teamId = Integer.parseInt(teamIdField.getText());
        int matchesPlayed = Integer.parseInt(matchesPlayedField.getText());
        int wins = Integer.parseInt(winsField.getText());
        int draws = Integer.parseInt(drawsField.getText());
        int losses = Integer.parseInt(lossesField.getText());
        int goalsFor = Integer.parseInt(goalsForField.getText());
        int goalsAgainst = Integer.parseInt(goalsAgainstField.getText());
        int points = Integer.parseInt(pointsField.getText());

        String query = "UPDATE STANDINGS SET TEAMID = ?, MATCHESPLAYED = ?, WINS = ?, DRAWS = ?, LOSSES = ?, GOALSFOR = ?, GOALSAGAINST = ?, POINTS = ? WHERE STANDINGSID = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, teamId);
            preparedStatement.setInt(2, matchesPlayed);
            preparedStatement.setInt(3, wins);
            preparedStatement.setInt(4, draws);
            preparedStatement.setInt(5, losses);
            preparedStatement.setInt(6, goalsFor);
            preparedStatement.setInt(7, goalsAgainst);
            preparedStatement.setInt(8, points);
            preparedStatement.setInt(9, standingsId);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                clearFields();
                displayStandings();
                JOptionPane.showMessageDialog(this, "Standings modified successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayStandings() {
        String query = "SELECT * FROM STANDINGS";

        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            StringBuilder sb = new StringBuilder();
            while (resultSet.next()) {
                int standingsId = resultSet.getInt("STANDINGSID");
                int teamId = resultSet.getInt("TEAMID");
                int matchesPlayed = resultSet.getInt("MATCHESPLAYED");
                int wins = resultSet.getInt("WINS");
                int draws = resultSet.getInt("DRAWS");
                int losses = resultSet.getInt("LOSSES");
                int goalsFor = resultSet.getInt("GOALSFOR");
                int goalsAgainst = resultSet.getInt("GOALSAGAINST");
                int points = resultSet.getInt("POINTS");

                sb.append(standingsId).append("\t")
                        .append(teamId).append("\t")
                        .append(matchesPlayed).append("\t")
                        .append(wins).append("\t")
                        .append(draws).append("\t")
                        .append(losses).append("\t")
                        .append(goalsFor).append("\t")
                        .append(goalsAgainst).append("\t")
                        .append(points).append("\n");
            }

            resultArea.setText(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        idField.setText("");
        teamIdField.setText("");
        matchesPlayedField.setText("");
        winsField.setText("");
        drawsField.setText("");
        lossesField.setText("");
        goalsForField.setText("");
        goalsAgainstField.setText("");
        pointsField.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addStandings();
        } else if (e.getSource() == deleteButton) {
            deleteStandings();
        } else if (e.getSource() == modifyButton) {
            modifyStandings();
        } else if (e.getSource() == displayButton) {
            displayStandings();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SoccerLeagueStandingsApp app = new SoccerLeagueStandingsApp();
            app.setVisible(true);
        });
    }
}
