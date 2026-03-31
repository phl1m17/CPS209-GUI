import java.awt.Color;
import java.awt.Graphics2D;

public class Tree implements Clickable {
    private int treeX;
    private int treeY;
    private int size;
    private int health;
    private boolean removed = false;

    Panel panel;

    public Tree(Panel panel, int size, int health) {
        this.panel = panel;
        this.size = size;
        this.health = health;

        int leftOrRight = (int) (Math.random() * 2);
        treeX = leftOrRight == 0
                ? (int) (Math.random() * (panel.SCREEN_WIDTH / 2 - size * 2) + size * 2)
                : (int) (panel.SCREEN_WIDTH / 2 + size + Math.random() * (panel.SCREEN_WIDTH / 2 - size * 3)
                        - size * 2);
        treeY = panel.SCREEN_HEIGHT / 2 - (size) / 2 - size;
    }

    public void paint(Graphics2D g) {
        if (!removed) {
            g.setColor(new Color(115, 69, 17));
            g.fillRect(treeX, treeY, size, size * 3);
            g.setColor(new Color(30, 110, 10));
            g.fillRect(treeX, treeY, size, size);
            g.fillRect(treeX, treeY - size, size, size);
            g.fillRect(treeX - size, treeY, size, size);
            g.fillRect(treeX + size, treeY, size, size);
        }
    }

    public boolean collidesWithAny(double px, double py, int pSize) {
        return px + pSize > treeX && px < treeX + size &&
                py + pSize > treeY && py < treeY + size * 3 && !removed;
    }

    public boolean isRemoved() {
        return removed;
    }

    public boolean overlaps(Tree other) {
        return treeX - size * 2 < other.treeX + other.size * 3 &&
                treeX + size * 3 > other.treeX - other.size * 2;
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
