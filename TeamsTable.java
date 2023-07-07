package ruchita;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TeamsTable extends JFrame {
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;

    private JTextField teamIdField, teamNameField, teamLogoField, countryField, cityField, stadiumField;
    private JTable table;

    public TeamsTable() {
        super("Soccer League Teams");

        // Create GUI components
        JLabel teamIdLabel = new JLabel("Team ID:");
        JLabel teamNameLabel = new JLabel("Team Name:");
        JLabel teamLogoLabel = new JLabel("Team Logo:");
        JLabel countryLabel = new JLabel("Country:");
        JLabel cityLabel = new JLabel("City:");
        JLabel stadiumLabel = new JLabel("Stadium:");

        teamIdField = new JTextField(10);
        teamNameField = new JTextField(20);
        teamLogoField = new JTextField(20);
        countryField = new JTextField(20);
        cityField = new JTextField(20);
        stadiumField = new JTextField(20);

        JButton addButton = new JButton("Add");
        JButton modifyButton = new JButton("Modify");
        JButton deleteButton = new JButton("Delete");
        JButton displayButton = new JButton("Display");

        table = new JTable(new DefaultTableModel(new Object[]{"Team ID", "Team Name", "Team Logo", "Country", "City", "Stadium"}, 0));

        // Add action listeners to buttons
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTeam();
            }
        });

        modifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifyTeam();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteTeam();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayTeams();
            }
        });

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(displayButton);

        // Create panel for labels and fields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(teamIdLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(teamIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(teamNameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(teamNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(teamLogoLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(teamLogoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(countryLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(countryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(cityLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(cityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(stadiumLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(stadiumField, gbc);

        // Set frame layout
        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(table), BorderLayout.SOUTH);

        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

        // Establish database connection
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "ruchita", "ruchita12345");
            stmt = conn.createStatement();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Add MouseListener to table
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String teamId = (String) table.getValueAt(row, 0);
                    String teamName = (String) table.getValueAt(row, 1);
                    String teamLogo = (String) table.getValueAt(row, 2);
                    String country = (String) table.getValueAt(row, 3);
                    String city = (String) table.getValueAt(row, 4);
                    String stadium = (String) table.getValueAt(row, 5);

                    setTeamId(teamId);
                    setTeamName(teamName);
                    setTeamLogo(teamLogo);
                    setCountry(country);
                    setCity(city);
                    setStadium(stadium);
                }
            }
        });
    }

    private String getTeamId() {
        return teamIdField.getText();
    }

    private String getTeamName() {
        return teamNameField.getText();
    }

    private String getTeamLogo() {
        return teamLogoField.getText();
    }

    private String getCountry() {
        return countryField.getText();
    }

    private String getCity() {
        return cityField.getText();
    }

    private String getStadium() {
        return stadiumField.getText();
    }

    private void setTeamId(String teamId) {
        teamIdField.setText(teamId);
    }

    private void setTeamName(String teamName) {
        teamNameField.setText(teamName);
    }

    private void setTeamLogo(String teamLogo) {
        teamLogoField.setText(teamLogo);
    }

    private void setCountry(String country) {
        countryField.setText(country);
    }

    private void setCity(String city) {
        cityField.setText(city);
    }

    private void setStadium(String stadium) {
        stadiumField.setText(stadium);
    }

    private void addTeam() {
        try {
            String teamId = getTeamId();
            String teamName = getTeamName();
            String teamLogo = getTeamLogo();
            String country = getCountry();
            String city = getCity();
            String stadium = getStadium();

            String query = "INSERT INTO teams (team_id, TEAMNAME, TEAMLOGO, COUNTRY, CITY, STADIUM) VALUES ('" +
                    teamId + "', '" + teamName + "', '" + teamLogo + "', '" + country + "', '" + city + "', '" +
                    stadium + "')";

            stmt.executeUpdate(query);

            JOptionPane.showMessageDialog(this, "Team added successfully!");

            // Clear data fields after addition
            setTeamId("");
            setTeamName("");
            setTeamLogo("");
            setCountry("");
            setCity("");
            setStadium("");

            displayTeams();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void modifyTeam() {
        try {
            String teamId = getTeamId();
            String teamName = getTeamName();
            String teamLogo = getTeamLogo();
            String country = getCountry();
            String city = getCity();
            String stadium = getStadium();

            String query = "UPDATE teams SET TEAMNAME = '" + teamName + "', TEAMLOGO = '" + teamLogo + "', " +
                    "COUNTRY = '" + country + "', CITY = '" + city + "', STADIUM = '" + stadium + "' WHERE " +
                    "team_id = '" + teamId + "'";

            stmt.executeUpdate(query);

            JOptionPane.showMessageDialog(this, "Team modified successfully!");

            // Clear data fields after modification
            setTeamId("");
            setTeamName("");
            setTeamLogo("");
            setCountry("");
            setCity("");
            setStadium("");

            displayTeams();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteTeam() {
        try {
            String teamId = getTeamId();

            String query = "DELETE FROM teams WHERE team_id = '" + teamId + "'";

            stmt.executeUpdate(query);

            JOptionPane.showMessageDialog(this, "Team deleted successfully!");

            // Clear data fields after deletion
            setTeamId("");
            setTeamName("");
            setTeamLogo("");
            setCountry("");
            setCity("");
            setStadium("");

            displayTeams();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void displayTeams() {
        try {
            String query = "SELECT * FROM teams";

            rs = stmt.executeQuery(query);

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear existing data

            while (rs.next()) {
                String teamId = rs.getString("team_id");
                String teamName = rs.getString("TEAMNAME");
                String teamLogo = rs.getString("TEAMLOGO");
                String country = rs.getString("COUNTRY");
                String city = rs.getString("CITY");
                String stadium = rs.getString("STADIUM");

                model.addRow(new Object[]{teamId, teamName, teamLogo, country, city, stadium});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TeamsTable();
            }
        });
    }
}

