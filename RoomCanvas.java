import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class RoomCanvas extends JPanel {
    private static final int GRID_SIZE = 20; // Grid size in pixels
    private ArrayList<Room> rooms; // List of rooms on the canvas
    private Room selectedRoom; // Currently selected room
    private Point dragStart; // Initial drag point for moving or resizing

    public RoomCanvas() {
        rooms = new ArrayList<>();
        selectedRoom = null;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                selectedRoom = getRoomAt(point);
                dragStart = point;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragStart = null;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedRoom != null && dragStart != null) {
                    Point currentPoint = e.getPoint();
                    int dx = currentPoint.x - dragStart.x;
                    int dy = currentPoint.y - dragStart.y;

                    if (SwingUtilities.isRightMouseButton(e)) {
                        // Resizing
                        int newWidth = selectedRoom.getWidth() + dx;
                        int newHeight = selectedRoom.getHeight() + dy;
                        if (!doesOverlap(selectedRoom, selectedRoom.getX(), selectedRoom.getY(), newWidth, newHeight)) {
                            selectedRoom.resize(newWidth, newHeight);
                        }
                    } else {
                        // Moving
                        int newX = selectedRoom.getX() + dx;
                        int newY = selectedRoom.getY() + dy;
                        if (!doesOverlap(selectedRoom, newX, newY, selectedRoom.getWidth(), selectedRoom.getHeight())) {
                            selectedRoom.setPosition(newX, newY);
                        }
                    }

                    dragStart = currentPoint;
                    repaint();
                }
            }
        });
    }

    // Add a room to the canvas
    public void addRoom(Room room) {
        if (!doesOverlap(room, room.getX(), room.getY(), room.getWidth(), room.getHeight())) {
            rooms.add(room);
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Cannot add room: It overlaps with another room.");
        }
    }

    // Delete the currently selected room
    public void deleteSelectedRoom() {
        if (selectedRoom != null) {
            rooms.remove(selectedRoom);
            selectedRoom = null;
            repaint();
        }
    }

    // Check if a room overlaps with other rooms
    private boolean doesOverlap(Room target, int x, int y, int width, int height) {
        Rectangle targetBounds = new Rectangle(x, y, width, height);
        for (Room room : rooms) {
            if (room != target) {
                Rectangle roomBounds = new Rectangle(room.getX(), room.getY(), room.getWidth(), room.getHeight());
                if (targetBounds.intersects(roomBounds)) {
                    return true; // Overlap detected
                }
            }
        }
        return false;
    }

    // Find a room at a given point
    private Room getRoomAt(Point point) {
        for (Room room : rooms) {
            if (point.x >= room.getX() && point.x <= room.getX() + room.getWidth() &&
                point.y >= room.getY() && point.y <= room.getY() + room.getHeight()) {
                return room;
            }
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw grid
        drawGrid(g2);

        // Draw all rooms
        for (Room room : rooms) {
            room.draw(g2);
        }

        // Highlight the selected room
        if (selectedRoom != null) {
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(selectedRoom.getX() - 1, selectedRoom.getY() - 1,
                        selectedRoom.getWidth() + 2, selectedRoom.getHeight() + 2);
        }
    }

    // Draw a grid on the canvas
    private void drawGrid(Graphics2D g) {
        g.setColor(Color.LIGHT_GRAY);
        for (int x = 0; x < getWidth(); x += GRID_SIZE) {
            g.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += GRID_SIZE) {
            g.drawLine(0, y, getWidth(), y);
        }
    }
}
