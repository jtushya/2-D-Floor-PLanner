import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class Room implements Serializable {
    private String type;
    private int x, y; // Top-left corner coordinates
    private int width, height; // Dimensions in pixels
    private static final int GRID_SIZE = 20; // Grid size for snapping
    private static final int WALL_THICKNESS = 3; // Wall thickness
    private boolean[] hasDoor; // [top, right, bottom, left] for walls

    public Room(String type, int width, int height) {
        this.type = type;
        this.width = snapToGrid(width);
        this.height = snapToGrid(height);
        this.x = 0;
        this.y = 0;
        this.hasDoor = new boolean[]{false, false, false, false}; // No doors initially
    }

    // Snap a value to the nearest grid point
    private int snapToGrid(int value) {
        return (value + GRID_SIZE / 2) / GRID_SIZE * GRID_SIZE;
    }

    // Get color based on room type
    public Color getColor() {
        switch (type) {
            case "Bathroom":
                return Color.BLUE;
            case "Kitchen":
                return Color.RED;
            case "Bedroom":
                return Color.GREEN;
            case "Living Room":
                return Color.PINK;
            default:
                return Color.GRAY; // Default color if type is undefined
        }
    }

    // Draw the room on the canvas
    public void draw(Graphics2D g) {
        // Draw the main room area
        g.setColor(getColor());
        g.fillRect(x + WALL_THICKNESS, y + WALL_THICKNESS, 
                   width - 2 * WALL_THICKNESS, height - 2 * WALL_THICKNESS);

        // Draw the walls
        g.setColor(Color.BLACK);

        // Top wall
        if (!hasDoor[0]) {
            g.fillRect(x, y, width, WALL_THICKNESS);
        }
        // Right wall
        if (!hasDoor[1]) {
            g.fillRect(x + width - WALL_THICKNESS, y, WALL_THICKNESS, height);
        }
        // Bottom wall
        if (!hasDoor[2]) {
            g.fillRect(x, y + height - WALL_THICKNESS, width, WALL_THICKNESS);
        }
        // Left wall
        if (!hasDoor[3]) {
            g.fillRect(x, y, WALL_THICKNESS, height);
        }

        // Draw the room type text
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        String label = type + " (" + ((width - 2 * WALL_THICKNESS) / GRID_SIZE) + "x" 
                                 + ((height - 2 * WALL_THICKNESS) / GRID_SIZE) + ")";
        Rectangle2D textBounds = g.getFontMetrics().getStringBounds(label, g);
        int textX = x + (width - (int) textBounds.getWidth()) / 2;
        int textY = y + (height + (int) textBounds.getHeight()) / 2;
        g.drawString(label, textX, textY);
    }

    // Add a door by removing part of the wall
    public void addDoor(String wall) {
        switch (wall.toLowerCase()) {
            case "top":
                hasDoor[0] = true;
                break;
            case "right":
                hasDoor[1] = true;
                break;
            case "bottom":
                hasDoor[2] = true;
                break;
            case "left":
                hasDoor[3] = true;
                break;
            default:
                throw new IllegalArgumentException("Invalid wall: Choose from top, right, bottom, left");
        }
    }

    // Place the room relative to another room
    public boolean placeRelativeTo(Room other, String direction, String alignment) {
        switch (direction.toLowerCase()) {
            case "north":
                this.y = other.y - this.height;
                alignHorizontally(other, alignment);
                break;
            case "south":
                this.y = other.y + other.height;
                alignHorizontally(other, alignment);
                break;
            case "east":
                this.x = other.x + other.width;
                alignVertically(other, alignment);
                break;
            case "west":
                this.x = other.x - this.width;
                alignVertically(other, alignment);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: Choose from north, south, east, west");
        }

        // Check for overlap
        if (this.overlaps(other)) {
            return false; // Indicate failure due to overlap
        }

        return true;
    }

    // Horizontal alignment: left, center, right
    private void alignHorizontally(Room other, String alignment) {
        switch (alignment.toLowerCase()) {
            case "left":
                this.x = other.x;
                break;
            case "center":
                this.x = other.x + (other.width - this.width) / 2;
                break;
            case "right":
                this.x = other.x + other.width - this.width;
                break;
            default:
                throw new IllegalArgumentException("Invalid alignment: Choose from left, center, right");
        }
    }

    // Vertical alignment: top, center, bottom
    private void alignVertically(Room other, String alignment) {
        switch (alignment.toLowerCase()) {
            case "top":
                this.y = other.y;
                break;
            case "center":
                this.y = other.y + (other.height - this.height) / 2;
                break;
            case "bottom":
                this.y = other.y + other.height - this.height;
                break;
            default:
                throw new IllegalArgumentException("Invalid alignment: Choose from top, center, bottom");
        }
    }

    // Check if this room overlaps with another room
    public boolean overlaps(Room other) {
        return this.x < other.x + other.width &&
               this.x + this.width > other.x &&
               this.y < other.y + other.height &&
               this.y + this.height > other.y;
    }

    // Save the room details in a custom file format
    public String saveRoom() {
        return String.format("%s,%d,%d,%d,%d", type, x, y, width, height);
    }

    // Load room details from a custom file format
    public static Room loadRoom(String data) {
        String[] parts = data.split(",");
        String type = parts[0];
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        int width = Integer.parseInt(parts[3]);
        int height = Integer.parseInt(parts[4]);
        Room room = new Room(type, width, height);
        room.setPosition(x, y);
        return room;
    }

    // Set position with snapping
    public void setPosition(int x, int y) {
        this.x = snapToGrid(x);
        this.y = snapToGrid(y);
    }

    // Resize the room with snapping
    public void resize(int newWidth, int newHeight) {
        this.width = snapToGrid(newWidth);
        this.height = snapToGrid(newHeight);
    }

    // Getters and setters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getType() {
        return type;
    }
}
