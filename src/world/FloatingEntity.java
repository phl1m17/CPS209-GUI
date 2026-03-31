import java.awt.Graphics2D;

public abstract class FloatingEntity {
    protected double x, y, baseY;
    protected double angle = 0;
    protected final double speed = 0.05;
    protected final int amplitude;
    protected boolean collected = false;

    public FloatingEntity(double x, double y, int amplitude) {
        this.x = x;
        this.baseY = y;
        this.y = y;
        this.amplitude = amplitude;
    }

    public void animate() {
        angle += speed;
        y = baseY + Math.sin(angle) * amplitude;
    }

    public boolean collidesWithPlayer(double px, double py, int playerSize) {
        int playerW = 36;
        int playerH = playerSize * 2;
        return px + 14 + playerW > x && px + 14 < x + size() &&
                py + playerH > y && py < y + size();
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    protected abstract int size();

    public abstract void paint(Graphics2D g);
}