import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Tree implements Clickable {
    private int treeX;
    private int treeY;
    private int size;
    private int health;
    private int side;
    private boolean removed = false;

    Panel panel;

    public Tree(Panel panel, int size, int health, int side) {
        this.panel = panel;
        this.size = size;
        this.health = health;
        this.side = side;

        treeX = side == 0
                ? (int) (Math.random() * 5 + 2) * panel.SIZE
                : (int) (Math.random() * 5 + 9) * panel.SIZE;
        treeY = panel.SCREEN_HEIGHT / 2 - (size) / 2 - size;
    }

    public int getSide() {
        return side;
    }

    public void paint(Graphics2D g) {
        Color brown = new Color(115, 69, 17);
        Color green = new Color(30, 110, 10);
        if (!removed) {
            g.setColor(brown);
            g.fillRect(treeX, treeY, size, size * 3);
            g.setColor(brown.darker());
            g.setStroke(new BasicStroke(2));
            g.drawRect(treeX, treeY, size, size * 3);
            g.setColor(green);
            g.fillRect(treeX - size, treeY, size * 3, size);
            g.fillRect(treeX, treeY - size, size, size);
            g.setColor(green.darker());
            g.drawRect(treeX - size, treeY, size * 3, size);
            g.drawRect(treeX, treeY - size, size, size);
            g.setColor(green);
            g.drawLine(treeX, treeY, treeX + size, treeY);
            g.setStroke(new BasicStroke(1));
        }
    }

    public boolean collidesWithAny(double px, double py, int pSize) {
        int padding = 8;
        return px + pSize - padding > treeX && px + padding < treeX + size &&
                py + pSize > treeY && py + padding < treeY + size * 3 && !removed;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            removed = true;
            Item selected = panel.player.getSelectedItem();
            int woodDrop = (selected != null && selected.getType() == Item.Type.AXE) ? 4 : 2;
            panel.worlds[panel.worldIndex + 1].dropItem(Item.Type.WOOD, woodDrop, treeX + DroppedItem.ITEM_SIZE / 2,
                    panel.SCREEN_HEIGHT / 2 + DroppedItem.ITEM_SIZE + 15);
        }
    }

    @Override
    public double getX() {
        return treeX;
    }

    @Override
    public double getY() {
        return treeY;
    }

    @Override
    public boolean containsPoint(int x, int y) {
        return x >= treeX && x <= treeX + size &&
                y >= treeY && y <= treeY + size * 3;
    }

    @Override
    public void onClick() {
        takeDamage(1);
    }
}
