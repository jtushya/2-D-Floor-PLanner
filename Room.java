import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Room {
    private String type;
    private int x, y; // Top-left corner coordinates
    private int width, height; // Dimensions in pixels
    private static final int GRID_SIZE = 20; // Grid size for snapping

    public Room(String type, int width, int height) {
        this.type = type;
        this.width = snapToGrid(width);
        this.height = snapToGrid(height);
        this.x = 0;
        this.y = 0;
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
        g.setColor(getColor());
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK); // Border color
        g.drawRect(x, y, width, height);

        // Draw the room type text
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        String label = type + " (" + (width / GRID_SIZE) + "x" + (height / GRID_SIZE) + ")";
        Rectangle2D textBounds = g.getFontMetrics().getStringBounds(label, g);
        int textX = x + (width - (int) textBounds.getWidth()) / 2;
        int textY = y + (height + (int) textBounds.getHeight()) / 2;
        g.drawString(label, textX, textY);
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
