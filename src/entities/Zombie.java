import java.awt.Color;
import java.awt.Graphics2D;

public class Zombie extends Mob {
    private int damageCooldown = 0;
    private static final int DAMAGE_INTERVAL = 60;
    private static final double SPEED = 2;

    public Zombie(int size, int health, Color colour, Panel panel) {
        super(size, health, colour, panel);
        characterX = Math.random() < 0.5 ? -size : panel.SCREEN_WIDTH + size;
        characterY = panel.SCREEN_HEIGHT / 2 + size / 2;
    }

    @Override
    public void update(double playerX) {
        if (isRemoved())
            return;

        double dx = playerX - characterX;
        characterX += Math.signum(dx) * SPEED;

        if (damageCooldown > 0)
            damageCooldown--;
    }

    public boolean canDamage() {
        return damageCooldown <= 0;
    }

    public void resetDamageCooldown() {
        damageCooldown = DAMAGE_INTERVAL;
    }

    @Override
    public void paint(Graphics2D g) {
        if (isRemoved())
            return;
        g.setColor(colour);
        g.fillRect((int) characterX, (int) characterY, size, size);
        paintHealth(g);
    }
}