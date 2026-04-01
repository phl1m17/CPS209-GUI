import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Item {
    public enum Type {
        WOOD, SWORD, AXE, PLANK
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
                this.maxDurability = 20;
                break;
            case AXE:
                this.maxDurability = 10;
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

    public void paint(Graphics2D g, int x, int y, int size, boolean preview, boolean drop, boolean facingRight) {
        Color brown = new Color(115, 69, 17);
        switch (type) {
            case WOOD:
                int woodSize = (int) (size / 1.5);
                int woodX = x + size / 2 - woodSize / 2;
                int woodY = y + size / 2 - woodSize / 2;

                g.setColor(brown.brighter());
                g.fillRect(woodX, woodY, woodSize, woodSize);
                g.setColor(brown);
                g.setStroke(new BasicStroke(8));
                g.drawRect(woodX + 4, woodY + 4, woodSize - 8, woodSize - 8);
                g.setStroke(new BasicStroke(4));
                g.setColor(brown.darker());
                g.drawRect(woodX + 2, woodY + 2, woodSize - 4, woodSize - 4);
                g.setStroke(new BasicStroke(1));
                break;
            case SWORD:
                int cx = x + size / 2;
                int itemSize = Math.max(2, size / 16);

                int[] swordX = { cx - itemSize, cx, cx + itemSize, cx + itemSize, cx - itemSize };
                int[] swordY = { y + size / 4, y + size / 16, y + size / 4, y + size - size / 16,
                        y + size - size / 16 };
                g.setColor(brown.brighter());
                g.fillPolygon(swordX, swordY, 5);

                int handle = size / 4;
                int guardThick = Math.max(2, size / 32);
                int[] swordX2 = {
                        x + handle, x + handle + guardThick, x + size - handle - guardThick, x + size - handle,
                        x + size - handle - guardThick, x + handle + guardThick
                };
                int[] swordY2 = {
                        y + 2 * size / 3, y + 2 * size / 3 - guardThick, y + 2 * size / 3 - guardThick,
                        y + 2 * size / 3,
                        y + 2 * size / 3 + guardThick, y + 2 * size / 3 + guardThick
                };
                g.setColor(brown.darker());
                g.fillPolygon(swordX2, swordY2, 6);
                break;

            case AXE:
                AffineTransform old = g.getTransform();

                if (!facingRight) {
                    g.translate(x + size / 2, 0);
                    g.scale(-1, 1);
                    g.translate(-(x + size / 2), 0);
                }

                cx = x + size / 2 - size / 8;
                itemSize = Math.max(2, size / 16);

                int[] axeX = {
                        cx - itemSize,
                        cx + itemSize,
                        cx + itemSize,
                        cx - itemSize
                };

                int[] axeY = {
                        y + size / 4,
                        y + size / 4,
                        y + size - size / 16,
                        y + size - size / 16
                };

                g.setColor(brown.darker());
                g.fillPolygon(axeX, axeY, 4);

                int axeOut = size * 5 / 16;

                int[] axeX2 = {
                        cx + itemSize,
                        cx + itemSize + axeOut,
                        cx + itemSize + axeOut,
                        cx + itemSize
                };

                int[] axeY2 = {
                        y + size * 9 / 32,
                        y + size * 3 / 16,
                        y + size * 17 / 32,
                        y + size * 7 / 16
                };

                g.setColor(brown.brighter());
                g.fillPolygon(axeX2, axeY2, 4);

                g.setTransform(old);

                break;
            case PLANK:
                Color plankLight = new Color(210, 160, 90);
                Color plankDark = new Color(150, 100, 40);
                int plankSize = (int) (size / 1.5);
                int plankX = x + size / 2 - plankSize / 2;
                int plankY = y + size / 2 - plankSize / 2;
                g.setColor(plankLight);
                g.fillRect(plankX, plankY, plankSize, plankSize);
                g.setColor(plankDark);
                g.drawRect(plankX, plankY, plankSize, plankSize);
                g.drawLine(plankX, plankY + plankSize / 3, plankX + plankSize, plankY + plankSize / 3);
                g.drawLine(plankX, plankY + 2 * plankSize / 3, plankX + plankSize, plankY + 2 * plankSize / 3);
                g.drawLine(plankX + plankSize / 2, plankY, plankX + plankSize / 2, plankY + plankSize / 3);
                g.drawLine(plankX + plankSize / 2, plankY + 2 * plankSize / 3, plankX + plankSize / 2,
                        plankY + plankSize);
                break;
            default:
                break;
        }
        if (maxDurability > 0 && !preview) {
            int barW = (int) ((durability / (double) maxDurability) * size);
            g.setColor(Color.DARK_GRAY);
            g.fillRect(x, y + size - 4, size, 4);
            g.setColor(durability > maxDurability / 2 ? Color.GREEN : Color.ORANGE);
            g.fillRect(x, y + size - 4, barW, 4);
        }
        if (quantity > 1 && !drop) {
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(quantity), x + size - 12, y + size - 4);
        }
    }
}