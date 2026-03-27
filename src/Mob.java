import java.awt.Color;
import java.awt.Graphics2D;

public class Mob extends Character implements Clickable {
    private int damageCooldown = 0;
    private final int DAMAGE_INTERVAL = 60;
    private boolean removed = false;

    Panel panel;
    int worldIndex;

    public Mob(int size, int health, Color colour, Panel panel, int worldIndex) {
        super(size, health, colour);
        this.panel = panel;
        this.worldIndex = worldIndex;
        characterX = Math.random() < 0.5 ? -size : panel.SCREEN_WIDTH + size;
        characterY = panel.SCREEN_HEIGHT / 2 + size / 2;
    }

    public void paint(Graphics2D g) {
        if (!removed) {
            g.setColor(colour);
            g.fillRect((int) characterX, (int) characterY, size, size);
            paintHealth(g);
        }
    }

    public void paintHealth(Graphics2D g) {
        if (removed)
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

    public void setX(int x) {
        characterX = x;
    }

    public void moveMob(double px) {
        if (removed)
            return;

        double speed = 2;
        double dx = px - characterX;

        characterX += Math.signum(dx) * speed;
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0)
            removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }

    public boolean collidesWithAny(double px, double py, int size) {
        if (removed)
            return false;
        int padding = 5;
        return px + size > characterX + padding && px < characterX + this.size - padding &&
                py + size > characterY + padding && py < characterY + this.size - padding;
    }

    @Override
    public double getX() {
        return characterX;
    }

    @Override
    public double getY() {
        return characterY;
    }

    public boolean canDamage() {
        return damageCooldown <= 0;
    }

    public void tickCooldown() {
        if (damageCooldown > 0)
            damageCooldown--;
    }

    public void resetDamageCooldown() {
        damageCooldown = DAMAGE_INTERVAL;
    }

    @Override
    public boolean containsPoint(int x, int y) {
        return x >= characterX && x <= characterX + size &&
                y >= characterY && y <= characterY + size;
    }

    @Override
    public void onClick() {
        takeDamage(1);
    }
}