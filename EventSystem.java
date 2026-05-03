import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class EventSystem {

    // ---------- DATABASE CONNECTION ----------
    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/event_management?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String pass = "SanaM@01"; // change if needed

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, pass);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "DB Error: " + e.getMessage());
            return null;
        }
    }

    // ---------- DASHBOARD ----------
    static class Dashboard extends JFrame {
        Dashboard() {
            setTitle("Event Management System");
            setSize(400, 200);
            setLayout(new FlowLayout());

            JButton addEvent = new JButton("Add Event");
            JButton registerUser = new JButton("Register User");
            JButton viewEvents = new JButton("View Events");

            add(addEvent);
            add(registerUser);
            add(viewEvents);

            addEvent.addActionListener(e -> new AddEvent());
            registerUser.addActionListener(e -> new RegisterUser());
            viewEvents.addActionListener(e -> new ViewEvents());

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }
    }

    // ---------- ADD EVENT ----------
    static class AddEvent extends JFrame {
        JTextField nameField, dateField, timeField, venueField, descField;

        AddEvent() {
            setTitle("Add Event");
            setSize(350, 300);
            setLayout(new GridLayout(6, 2));

            nameField = new JTextField();
            dateField = new JTextField();
            timeField = new JTextField();
            venueField = new JTextField();
            descField = new JTextField();

            JButton submit = new JButton("Add");

            add(new JLabel("Event Name:"));
            add(nameField);
            add(new JLabel("Date:"));
            add(dateField);
            add(new JLabel("Time:"));
            add(timeField);
            add(new JLabel("Venue:"));
            add(venueField);
            add(new JLabel("Description:"));
            add(descField);
            add(submit);

            submit.addActionListener(e -> addEvent());

            setVisible(true);
        }

        void addEvent() {
            try {
                Connection con = getConnection();

                String query = "INSERT INTO events(event_name, event_date, event_time, venue, description, user_id) VALUES (?, ?, ?, ?, ?, ?)";

                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, nameField.getText());
                pst.setString(2, dateField.getText());
                pst.setString(3, timeField.getText());
                pst.setString(4, venueField.getText());
                pst.setString(5, descField.getText());
                pst.setInt(6, 1);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Event Added!");

                con.close();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    // ---------- REGISTER USER ----------
    static class RegisterUser extends JFrame {
        JTextField nameField, emailField, passwordField;
        JComboBox<String> eventBox;

        RegisterUser() {
            setTitle("Register User");
            setSize(300, 300);
            setLayout(new GridLayout(5, 2));

            nameField = new JTextField();
            emailField = new JTextField();
            passwordField = new JTextField();

            // Dropdown
            eventBox = new JComboBox<>();
            eventBox.addItem("Event 1");
            eventBox.addItem("Event 2");
            eventBox.addItem("Event 3");

            JButton submit = new JButton("Register");

            add(new JLabel("Name:"));
            add(nameField);
            add(new JLabel("Email:"));
            add(emailField);
            add(new JLabel("Password:"));
            add(passwordField);
            add(new JLabel("Select Event:"));
            add(eventBox);
            add(submit);

            submit.addActionListener(e -> registerUser());

            setVisible(true);
        }

        void registerUser() {
            try {
                Connection con = getConnection();

                String selectedEvent = (String) eventBox.getSelectedItem();

                // Insert user
                String query = "INSERT INTO users(name, email, password) VALUES (?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                pst.setString(1, nameField.getText());
                pst.setString(2, emailField.getText());
                pst.setString(3, passwordField.getText());

                pst.executeUpdate();

                // Get user ID
                ResultSet rs = pst.getGeneratedKeys();
                int userId = 0;

                if (rs.next()) {
                    userId = rs.getInt(1);
                }

                // Insert into registrations
                String regQuery = "INSERT INTO registrations(user_id, event_name) VALUES (?, ?)";
                PreparedStatement pst2 = con.prepareStatement(regQuery);

                pst2.setInt(1, userId);
                pst2.setString(2, selectedEvent);

                pst2.executeUpdate();

                JOptionPane.showMessageDialog(this, "User Registered for " + selectedEvent);

                con.close();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    // ---------- VIEW EVENTS ----------
    static class ViewEvents extends JFrame {
        JTable table;

        ViewEvents() {
            setTitle("View Events");
            setSize(500, 300);

            table = new JTable();
            JScrollPane scroll = new JScrollPane(table);
            add(scroll);

            loadEvents();

            setVisible(true);
        }

        void loadEvents() {
            try {
                Connection con = getConnection();

                String query = "SELECT * FROM events";
                PreparedStatement pst = con.prepareStatement(query);
                ResultSet rs = pst.executeQuery();

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("ID");
                model.addColumn("Name");
                model.addColumn("Date");
                model.addColumn("Time");
                model.addColumn("Venue");

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("event_id"),
                            rs.getString("event_name"),
                            rs.getString("event_date"),
                            rs.getString("event_time"),
                            rs.getString("venue")
                    });
                }

                table.setModel(model);

                con.close();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    // ---------- MAIN ----------
    public static void main(String[] args) {
        new Dashboard();
    }
}