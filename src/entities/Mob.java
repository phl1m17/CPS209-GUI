import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Mob extends Character implements Clickable {
    Panel panel;
    int worldIndex;

    public Mob(int size, int health, Color color, Panel panel) {
        super(size, health, color);
        this.panel = panel;
        this.worldIndex = panel.worldIndex;
    }

    public void setX(int x) {
        characterX = x;
    }

    public void paintHealth(Graphics2D g) {
        if (isRemoved())
            return;
        int barWidth = size;
        int barHeight = 6;
        int x = (int) characterX;
        int y = (int) characterY - barHeight - 4;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, barWidth, barHeight);

        int fillWidth = (int) ((health / (double) maxHealth) * barWidth);
        g.setColor(Color.RED);
        g.fillRect(x, y, fillWidth, barHeight);

        g.setColor(Color.WHITE);
        g.drawRect(x, y, barWidth, barHeight);
    }

    @Override
    public boolean containsPoint(int x, int y) {
        return x >= characterX && x <= characterX + size &&
                y >= characterY && y <= characterY + size * 2;
    }

    @Override
    public void onClick() {
        takeDamage(1);
    }

    public abstract void update(double playerX);
}