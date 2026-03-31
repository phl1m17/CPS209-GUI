import java.awt.Color;
import java.awt.Graphics2D;

public class PowerUp extends FloatingEntity {
    public enum Type {
        HEALTH, STRENGTH
    }

    private Type type;
    private int powerUpSize;
    int worldIndex;
    Panel panel;

    public PowerUp(Panel panel, int num, int size, int worldIndex) {
        super(
                (int) (Math.random() * (panel.SCREEN_WIDTH - size * 4)) + size * 2,
                panel.SCREEN_HEIGHT / 2 + size / 2.0,
                10);
        this.type = num == 0 ? Type.HEALTH : Type.STRENGTH;
        this.powerUpSize = size / 2;
        this.panel = panel;
        this.worldIndex = worldIndex;
    }

    public Type getType() {
        return type;
    }

    @Override
    protected int size() {
        return powerUpSize;
    }

    @Override
    public boolean collidesWithPlayer(double px, double py, int playerSize) {
        if (panel.worldIndex + 1 != worldIndex)
            return false;
        return super.collidesWithPlayer(px, py, playerSize);
    }

    @Override
    public void paint(Graphics2D g) {
        if (panel.worldIndex + 1 != worldIndex)
            return;
        switch (type) {
            case HEALTH:
                g.setColor(new Color(201, 31, 22));
                g.fillOval((int) x, (int) y, powerUpSize, powerUpSize);
                g.setColor(Color.WHITE);
                g.drawOval((int) x, (int) y, powerUpSize, powerUpSize);
                break;
            case STRENGTH:
                g.setColor(new Color(204, 101, 10));
                g.fillOval((int) x, (int) y, powerUpSize, powerUpSize);
                g.setColor(Color.DARK_GRAY);
                g.drawOval((int) x, (int) y, powerUpSize, powerUpSize);
                break;
        }
    }
}