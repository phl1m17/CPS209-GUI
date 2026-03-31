import java.awt.Color;
import java.awt.Graphics2D;

public class Zombie extends Mob {
    private int damageCooldown = 0;
    private static final int DAMAGE_INTERVAL = 60;
    private static final double SPEED = 2;
    private boolean walkFrame = false;
    private int walkTimer = 0;

    public Zombie(int size, int health, Color colour, Panel panel) {
        super(size, health, colour, panel);
        characterX = Math.random() < 0.5 ? -size : panel.SCREEN_WIDTH + size;
        characterY = panel.SCREEN_HEIGHT / 2 - size / 2;
    }

    public boolean canDamage() {
        return damageCooldown <= 0;
    }

    public void resetDamageCooldown() {
        damageCooldown = DAMAGE_INTERVAL;
    }

    @Override
    public void update(double playerX) {
        if (isRemoved())
            return;

        double dx = playerX - characterX;
        characterX += Math.signum(dx) * SPEED;

        if (damageCooldown > 0)
            damageCooldown--;

        walkTimer++;
        if (walkTimer >= 16) {
            walkFrame = !walkFrame;
            walkTimer = 0;
        }
    }

    public boolean collidesWithAny(double px, double py, int size) {
        if (isRemoved())
            return false;

        int padding = 16;

        int hitboxHeight = this.size * 2 - padding * 2;
        int hitboxTop = (int) characterY + this.size * 2 - hitboxHeight;

        return px + size > characterX + padding &&
                px < characterX + this.size - padding &&
                py + size * 2 > hitboxTop &&
                py < hitboxTop + hitboxHeight;
    }

    @Override
    public void paint(Graphics2D g) {
        if (isRemoved())
            return;

        boolean movingLeft = panel.player.getX() < characterX;
        boolean movingRight = panel.player.getX() > characterX;

        paintBody(g,
                movingLeft, movingRight, walkFrame,
                new Color(80, 140, 80),
                new Color(60, 100, 60));

        paintHealth(g, this);
        g.setColor(Color.YELLOW);

        // int padding = 16;
        // int hitboxHeight = size * 2 - padding * 2;
        // int hitboxTop = (int) characterY + size * 2 - hitboxHeight;
        // g.setColor(Color.YELLOW);
        // g.drawRect(
        // (int) characterX + padding,
        // hitboxTop,
        // size - padding * 2,
        // hitboxHeight);
    }
}