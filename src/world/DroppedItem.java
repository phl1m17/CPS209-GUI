import java.awt.Graphics2D;

public class DroppedItem extends FloatingEntity {
    private Item.Type type;
    private int quantity;
    static final int ITEM_SIZE = 32;

    public DroppedItem(Item.Type type, int quantity, double x, double y) {
        super(x, y, 8);
        this.type = type;
        this.quantity = quantity;
    }

    public Item.Type getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    protected int size() {
        return ITEM_SIZE;
    }

    @Override
    public void paint(Graphics2D g) {
        if (collected)
            return;
        new Item(type, quantity).paint(g, (int) x, (int) y, ITEM_SIZE, false, true, false);
    }
}