import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Inventory {
    Panel panel;
    int size;
    Item item;
    int slotY;
    int slotX;

    public Inventory(Panel panel, int size) {
        this.panel = panel;
        this.size = size;
        slotY = panel.SCREEN_HEIGHT - 30 - size;
    }

    public Inventory(Panel panel, int size, Item item) {
        this.panel = panel;
        this.size = size;
        this.item = item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public boolean isEmpty() {
        return item == null || item.getQuantity() <= 0;
    }

    public void paint(Graphics2D g, int slotIndex, boolean selected) {
        int slotX = (int) (16 + size * slotIndex * 1.25);

        g.setColor(new Color(117, 117, 117));
        g.fillRect(slotX, slotY, size, size);
        g.setColor(selected ? Color.YELLOW : new Color(173, 173, 173));
        g.setStroke(new BasicStroke(selected ? 3 : 2));
        g.drawRect(slotX, slotY, size, size);
        g.setStroke(new BasicStroke(1));

        if (!isEmpty()) {
            item.paint(g, slotX, slotY, size, false);
        }
    }
}