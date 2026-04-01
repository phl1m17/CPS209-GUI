import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Block implements Clickable {
    public enum Type {
        WOOD, PLANK
    }

    private Type type;
    private int x, y, size;
    private int health;
    private int maxHealth;
    private boolean broken = false;
    private Panel panel;

    public Block(Type type, int x, int y, int size, Panel panel) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.size = size;
        this.panel = panel;
        this.maxHealth = type == Type.WOOD ? 2 : 1;
        this.health = maxHealth;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public boolean containsPoint(int mx, int my) {
        return mx >= x && mx < x + size && my >= y && my < y + size;
    }

    @Override
    public void onClick() {
        health--;
        if (health <= 0) {
            broken = true;
            Item.Type dropType = type == Type.WOOD ? Item.Type.WOOD : Item.Type.PLANK;
            panel.player.addItem(dropType, 1);
        }
    }

    public boolean isBroken() {
        return broken;
    }

    public int getSize() {
        return size;
    }

    public Type getType() {
        return type;
    }

    public boolean collidesWithAny(double px, double py, int pW, int pH) {
        int padding = 8;
        return px + pW - padding > x && px + padding < x + size &&
                py + pH > y && py + padding < y + size;
    }

    public void paint(Graphics2D g) {
        if (type == Type.WOOD) {
            Color brown = new Color(115, 69, 17);
            g.setColor(brown);
            g.fillRect(x, y, size, size);
            g.setStroke(new BasicStroke(2));
            g.setColor(brown.darker());
            g.drawRect(x, y, size, size);
            g.setStroke(new BasicStroke(1));
        } else if (type == Type.PLANK) {
            Color plankLight = new Color(210, 160, 90);
            Color plankDark = new Color(150, 100, 40);
            g.setColor(plankLight);
            g.fillRect(x, y, size, size);
            g.setColor(plankDark);
            g.drawRect(x, y, size, size);
            g.drawLine(x, y + size / 3, x + size, y + size / 3);
            g.drawLine(x, y + 2 * size / 3, x + size, y + 2 * size / 3);
            g.drawLine(x + size / 2, y, x + size / 2, y + size / 3);
            g.drawLine(x + size / 2, y + 2 * size / 3, x + size / 2, y + size);
        }
    }
}