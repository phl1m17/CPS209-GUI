import java.awt.Color;
import java.awt.Graphics2D;

public class PowerUp {
    public enum Type {
        HEALTH, STRENGTH
    }

    private Type type;
    private int size;
    private int x;
    private double y;
    private double baseY;
    private double angle = 0;
    private int amplitude = 10;
    private double speed = 0.05;
    int worldIndex;
    Panel panel;

    public PowerUp(Panel panel, int num, int size) {
        type = num == 0 ? Type.HEALTH : Type.STRENGTH;
        this.size = size / 2;
        this.panel = panel;

        x = (int) (Math.random() * (panel.SCREEN_WIDTH - size * 4)) + size * 2;
        worldIndex = (int) (Math.random() * 3);

        baseY = panel.SCREEN_HEIGHT / 2 + size / 2.0;
        y = baseY;
    }

    public void animatePowerUp() {
        angle += speed;
        y = baseY + Math.sin(angle) * amplitude;
    }

    public Type getType() {
        return type;
    }

    public boolean collides(double playerX, double playerY, int playerSize) {
        if (panel.worldIndex + 1 != worldIndex)
            return false;
        return playerX + playerSize > x && playerX < x + size &&
                playerY + playerSize > y && playerY < y + size;
    }

    public int[] getPos() {
        return new int[] { x, worldIndex };
    }

    public void paintComponent(Graphics2D g) {
        if (panel.worldIndex + 1 != worldIndex)
            return;

        switch (type) {
            case HEALTH:
                g.setColor(new Color(201, 31, 22));
                g.fillOval(x, (int) y, size, size);
                g.setColor(Color.WHITE);
                g.drawOval(x, (int) y, size, size);
                break;
            case STRENGTH:
                g.setColor(new Color(204, 101, 10));
                g.fillOval(x, (int) y, size, size);
                g.setColor(Color.DARK_GRAY);
                g.drawOval(x, (int) y, size, size);
                break;
        }

    }
}