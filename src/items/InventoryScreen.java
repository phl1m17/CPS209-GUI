import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class InventoryScreen {
    private Panel panel;
    private int size = 56;
    private int padding = 10;

    private int screenX;
    private int screenY;
    private int screenW;
    private int screenH;

    private int craftX;
    private int craftSwordY;
    private int craftAxeY;
    private int craftPlanksY;

    private int draggedSlot = -1; // index of slot being dragged (-1 = none)
    private int dragX, dragY; // current mouse position while dragging

    private final Item swordPreview = new Item(Item.Type.SWORD, 1);
    private final Item axePreview = new Item(Item.Type.AXE, 1);
    private final Item plankPreview = new Item(Item.Type.PLANK, 1);

    public InventoryScreen(Panel panel) {
        this.panel = panel;

        screenW = 420;
        screenH = 300;
        screenX = panel.SCREEN_WIDTH / 2 - screenW / 2;
        screenY = panel.SCREEN_HEIGHT / 2 - screenH / 2;

        craftX = screenX + screenW - size - padding * 3;
        craftSwordY = screenY + padding * 3 + 20;
        craftAxeY = craftSwordY + size + padding;
        craftPlanksY = craftAxeY + size + padding;
    }

    public void paint(Graphics2D g) {
        g.setColor(new Color(80, 80, 80));
        g.fillRect(screenX, screenY, screenW, screenH);
        g.setColor(new Color(173, 173, 173));
        g.setStroke(new BasicStroke(3));
        g.drawRect(screenX, screenY, screenW, screenH);
        g.setStroke(new BasicStroke(1));

        g.setColor(Color.WHITE);
        g.drawString("INVENTORY", screenX + padding, screenY + padding + 12);

        int columns = 3;
        for (int i = 0; i < panel.player.inventory.size(); i++) {
            Inventory slot = panel.player.inventory.get(i);
            int col = i % columns;
            int row = i / columns;
            int slotX = screenX + padding + col * (size + padding);
            int slotY = screenY + padding * 3 + 20 + row * (size + padding);

            g.setColor(new Color(50, 50, 50));
            g.fillRect(slotX, slotY, size, size);
            g.setColor(new Color(173, 173, 173));
            g.setStroke(new BasicStroke(2));
            g.drawRect(slotX, slotY, size, size);
            g.setStroke(new BasicStroke(1));

            if (!slot.isEmpty()) {
                slot.getItem().paint(g, slotX, slotY, size, false, false, true);
            }
        }

        g.setColor(new Color(100, 100, 100));
        g.fillRect(screenX + screenW - size - padding * 12, screenY + padding,
                2, screenH - padding * 2);

        g.setColor(Color.WHITE);
        g.drawString("CRAFT", craftX, screenY + padding + 12);

        drawCraftSlot(g, craftX, craftSwordY, Item.Type.SWORD, "6 wood");
        drawCraftSlot(g, craftX, craftAxeY, Item.Type.AXE, "4 wood");
        drawCraftSlot(g, craftX, craftPlanksY, Item.Type.PLANK, "1 wood");

        g.setColor(Color.WHITE);
        g.drawString("Days Survived: " + panel.dayCount, screenX + padding,
                screenY + screenH - padding - 5);

        if (draggedSlot != -1 && !panel.player.inventory.get(draggedSlot).isEmpty()) {
            panel.player.inventory.get(draggedSlot).getItem()
                    .paint(g, dragX - size / 2, dragY - size / 2, size, false, false, true);
        }
    }

    private void drawCraftSlot(Graphics2D g, int x, int y, Item.Type result, String recipe) {
        g.setColor(new Color(50, 50, 50));
        g.fillRect(x, y, size, size);

        boolean canCraft = panel.player.canCraft(result);
        g.setColor(canCraft ? Color.GREEN : Color.DARK_GRAY);
        g.setStroke(new BasicStroke(1));
        g.drawRect(x, y, size, size);

        Item preview = result == Item.Type.SWORD ? swordPreview : result == Item.Type.AXE ? axePreview : plankPreview;
        preview.paint(g, x, y, size, true, false, true);

        g.setColor(canCraft ? Color.WHITE : Color.GRAY);
        g.drawString(recipe, x - 60, y + size / 2 + 5);
    }

    private int getSlotAt(int mouseX, int mouseY) {
        int columns = 3;
        for (int i = 0; i < panel.player.inventory.size(); i++) {
            int col = i % columns;
            int row = i / columns;
            int slotX = screenX + padding + col * (size + padding);
            int slotY = screenY + padding * 3 + 20 + row * (size + padding);
            if (mouseX >= slotX && mouseX <= slotX + size &&
                    mouseY >= slotY && mouseY <= slotY + size)
                return i;
        }
        return -1;
    }

    public void handlePress(int mouseX, int mouseY) {
        if (!panel.player.keyH.inventoryPressed)
            return;
        int slot = getSlotAt(mouseX, mouseY);
        if (slot != -1 && !panel.player.inventory.get(slot).isEmpty()) {
            draggedSlot = slot;
            dragX = mouseX;
            dragY = mouseY;
        }
    }

    public void handleDrag(int mouseX, int mouseY) {
        if (draggedSlot != -1) {
            dragX = mouseX;
            dragY = mouseY;
        }
    }

    public void handleRelease(int mouseX, int mouseY) {
        if (!panel.player.keyH.inventoryPressed || draggedSlot == -1) {
            draggedSlot = -1;
            return;
        }
        int targetSlot = getSlotAt(mouseX, mouseY);
        if (targetSlot != -1 && targetSlot != draggedSlot) {
            Inventory a = panel.player.inventory.get(draggedSlot);
            Inventory b = panel.player.inventory.get(targetSlot);
            panel.player.inventory.set(draggedSlot, b);
            panel.player.inventory.set(targetSlot, a);
        }
        draggedSlot = -1;
    }

    public void handleClick(int mouseX, int mouseY) {
        if (!panel.player.keyH.inventoryPressed)
            return;

        if (mouseX >= craftX && mouseX <= craftX + size) {
            if (mouseY >= craftSwordY && mouseY <= craftSwordY + size) {
                panel.player.craft(Item.Type.SWORD);
            }
            if (mouseY >= craftAxeY && mouseY <= craftAxeY + size) {
                panel.player.craft(Item.Type.AXE);
            }
            if (mouseY >= craftPlanksY && mouseY <= craftPlanksY + size) {
                panel.player.craft(Item.Type.PLANK);
            }
        }
    }
}