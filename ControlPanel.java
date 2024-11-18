import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel {
    public ControlPanel(RoomCanvas roomCanvas) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        String[] roomTypes = {"Bedroom", "Kitchen", "Living Room", "Bathroom"};
        JComboBox<String> roomSelector = new JComboBox<>(roomTypes);
        JButton addButton = new JButton("Add Room");
        JButton deleteButton = new JButton("Delete Room");

        // Action listener for the "Add Room" button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedRoom = (String) roomSelector.getSelectedItem();
                String lengthStr = JOptionPane.showInputDialog("Enter Length in pixels:");
                String widthStr = JOptionPane.showInputDialog("Enter Width in pixels:");

                try {
                    int length = Integer.parseInt(lengthStr);
                    int width = Integer.parseInt(widthStr);
                    Room room = new Room(selectedRoom, length, width);
                    roomCanvas.addRoom(room);  // Add the room to the canvas
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input for length or width.");
                }
            }
        });

        // Action listener for the "Delete Room" button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roomCanvas.deleteSelectedRoom();  // Delete the selected room from the canvas
            }
        });

        add(roomSelector);
        add(addButton);
        add(deleteButton);
    }
}
