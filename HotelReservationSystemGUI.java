import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class HotelReservationSystemGUI extends JFrame {
    private ArrayList<Room> rooms = new ArrayList<>();
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> roomList;
    
    // The file where your hotel data will be stored permanently
    private static final String DATA_FILE = "hotel_data.ser";

    public HotelReservationSystemGUI() {
        loadData(); // Load previous bookings from the file if it exists

        // GUI Window Setup
        setTitle("CodeAlpha Enterprise Hotel System");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Header Label
        JLabel header = new JLabel("CodeAlpha Premium Reservations", SwingConstants.CENTER);
        header.setFont(new Font("Serif", Font.BOLD, 22));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        // Room List Display
        updateRoomList();
        roomList = new JList<>(listModel);
        roomList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(roomList), BorderLayout.CENTER);

        // Control Panel (Buttons)
        JPanel buttonPanel = new JPanel();
        JButton bookBtn = new JButton("Confirm Booking");
        JButton cancelBtn = new JButton("Cancel Stay");
        
        buttonPanel.add(bookBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Logic: Booking a Room
        bookBtn.addActionListener(e -> {
            int index = roomList.getSelectedIndex();
            if (index != -1) {
                Room selectedRoom = rooms.get(index);
                if (selectedRoom.isAvailable()) {
                    selectedRoom.setAvailable(false);
                    saveData(); // Persist the change to the file
                    JOptionPane.showMessageDialog(this, "Payment Simulated.\nRoom " + selectedRoom.getRoomNumber() + " is now reserved.");
                    updateRoomList();
                } else {
                    JOptionPane.showMessageDialog(this, "This room is already occupied.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a room first.");
            }
        });

        // Logic: Cancelling a Stay
        cancelBtn.addActionListener(e -> {
            int index = roomList.getSelectedIndex();
            if (index != -1) {
                rooms.get(index).setAvailable(true);
                saveData(); // Persist the change to the file
                JOptionPane.showMessageDialog(this, "Reservation cancelled successfully.");
                updateRoomList();
            }
        });
    }

    // Refresh the JList display
    private void updateRoomList() {
        listModel.clear();
        for (Room r : rooms) {
            String status = r.isAvailable() ? "[Available]" : "[BOOKED]";
            listModel.addElement(String.format("Room %d | %-20s %s", r.getRoomNumber(), r.getCategory(), status));
        }
    }

    // --- DATA PERSISTENCE (Real-World Feature) ---
    
    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(rooms);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
                rooms = (ArrayList<Room>) ois.readObject();
            } catch (Exception e) {
                initializeDefaultRooms();
            }
        } else {
            initializeDefaultRooms();
        }
    }

    private void initializeDefaultRooms() {
        rooms.add(new Room(101, "Standard Single"));
        rooms.add(new Room(102, "Standard Double"));
        rooms.add(new Room(201, "Deluxe Ocean View"));
        rooms.add(new Room(202, "Deluxe City View"));
        rooms.add(new Room(301, "Presidential Suite"));
    }

    public static void main(String[] args) {
        // Run GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new HotelReservationSystemGUI().setVisible(true));
    }
}