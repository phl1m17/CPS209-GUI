import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Item {
    public enum Type {
        WOOD, SWORD, AXE
    }

    private Type type;
    private int quantity;
    private int durability;
    private final int maxDurability;

    public Item(Type type, int quantity) {
        this.type = type;
        this.quantity = quantity;

        switch (type) {
            case SWORD:
                this.maxDurability = 10;
                break;
            case AXE:
                this.maxDurability = 5;
                break;
            default:
                this.maxDurability = -1;
        }
        this.durability = maxDurability;
    }

    public Type getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getDurability() {
        return durability;
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public void addQuantity(int amount) {
        quantity += amount;
        if (quantity < 0)
            quantity = 0;
    }

    public boolean useDurability() {
        if (maxDurability == -1)
            return false;
        durability--;
        if (durability <= 0) {
            durability = maxDurability;
            return true;
        }
        return false;
    }

    public void resetDurability() {
        durability = maxDurability;
    }

    public void paint(Graphics2D g, int x, int y, int size, boolean preview) {
        Color lightBrown = new Color(171, 120, 62);
        Color brown = new Color(115, 69, 17);
        Color darkBrown = new Color(82, 51, 16);
        switch (type) {
            case WOOD:
                int woodSize = (int) (size / 1.5);
                int woodX = x + size / 2 - woodSize / 2;
                int woodY = y + size / 2 - woodSize / 2;

                g.setColor(lightBrown);
                g.fillRect(woodX, woodY, woodSize, woodSize);
                g.setColor(brown);
                g.setStroke(new BasicStroke(8));
                g.drawRect(woodX + 4, woodY + 4, woodSize - 8, woodSize - 8);
                g.setStroke(new BasicStroke(4));
                g.setColor(darkBrown);
                g.drawRect(woodX + 2, woodY + 2, woodSize - 4, woodSize - 4);
                g.setStroke(new BasicStroke(1));
                break;

            case SWORD:
                int cx = x + size / 2;
                int itemSize = size / 16;

                int[] swordX = { cx - itemSize, cx, cx + itemSize, cx + itemSize, cx - itemSize };
                int[] swordY = { y + 16, y + 4, y + 16, y + size - 4, y + size - 4 };
                g.setColor(lightBrown);
                g.fillPolygon(swordX, swordY, 5);

                int handle = 16;
                int[] swordX2 = {
                        x + handle, x + handle + 4, x + size - handle - 4, x + size - handle, x + size - handle - 4,
                        x + handle + 4
                };
                int[] swordY2 = {
                        y + 2 * size / 3, y + 2 * size / 3 - 2, y + 2 * size / 3 - 2, y + 2 * size / 3,
                        y + 2 * size / 3 + 2, y + 2 * size / 3 + 2
                };
                g.setColor(darkBrown);
                g.fillPolygon(swordX2, swordY2, 6);
                break;

            case AXE:
                cx = x + size / 2 - 8;
                itemSize = size / 16;

                int[] axeX = { cx - itemSize, cx + itemSize, cx + itemSize, cx - itemSize };
                int[] axeY = { y + 16, y + 16, y + size - 4, y + size - 4 };
                g.setColor(darkBrown);
                g.fillPolygon(axeX, axeY, 4);

                int[] axeX2 = { cx + itemSize, cx + itemSize + 20, cx + itemSize + 20, cx + itemSize };
                int[] axeY2 = { y + 18, y + 12, y + 34, y + 28 };
                g.setColor(lightBrown);
                g.fillPolygon(axeX2, axeY2, 4);
                break;
        }
        if (maxDurability > 0 && !preview) {
            int barW = (int) ((durability / (double) maxDurability) * size);
            g.setColor(Color.DARK_GRAY);
            g.fillRect(x, y + size - 4, size, 4);
            g.setColor(durability > maxDurability / 2 ? Color.GREEN : Color.ORANGE);
            g.fillRect(x, y + size - 4, barW, 4);
        }
        if (quantity > 1) {
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(quantity), x + size - 12, y + size - 4);
        }
    }
}