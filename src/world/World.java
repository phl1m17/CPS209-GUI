import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class World {
    Panel panel;
    int size;
    ArrayList<Tree> trees = new ArrayList<>();
    ArrayList<DroppedItem> droppedItems = new ArrayList<>();
    ArrayList<Block> blocks = new ArrayList<>();

    public World(Panel panel, int size) {
        this.panel = panel;
        this.size = size;
        spawnTrees();
    }

    public void spawnTrees() {
        boolean hasLeft = trees.stream().anyMatch(t -> t.getSide() == 0);
        boolean hasRight = trees.stream().anyMatch(t -> t.getSide() == 1);

        if (!hasLeft) {
            int attempts = 0;
            while (attempts < 100) {
                Tree candidate = new Tree(panel, panel.SIZE, 4, 0);
                if (!panel.player.tooCloseToPlayer(candidate.getX())
                        && !treeOverlapsSomething(candidate)) {
                    trees.add(candidate);
                    break;
                }
                attempts++;
            }
        }

        if (!hasRight) {
            int attempts = 0;
            while (attempts < 100) {
                Tree candidate = new Tree(panel, panel.SIZE, 4, 1);
                if (!panel.player.tooCloseToPlayer(candidate.getX())
                        && !treeOverlapsSomething(candidate)) {
                    trees.add(candidate);
                    break;
                }
                attempts++;
            }
        }
    }

    private boolean treeOverlapsSomething(Tree candidate) {
        int treeX = (int) candidate.getX();
        int treeY = (int) candidate.getY();

        // trunk
        int trunkW = size;
        int trunkH = size * 3;

        // leaves
        int leafTopX = treeX;
        int leafTopY = treeY - size;
        int leafTopW = size;
        int leafTopH = size;

        int leafSideX = treeX - size;
        int leafSideY = treeY;
        int leafSideW = size * 3;
        int leafSideH = size;

        for (Block b : blocks) {

            int bx = (int) b.getX();
            int by = (int) b.getY();
            int bs = b.getSize();

            if (rectOverlap(treeX, treeY, trunkW, trunkH, bx, by, bs, bs))
                return true;

            if (rectOverlap(leafTopX, leafTopY, leafTopW, leafTopH, bx, by, bs, bs))
                return true;

            if (rectOverlap(leafSideX, leafSideY, leafSideW, leafSideH, bx, by, bs, bs))
                return true;
        }

        return false;
    }

    private boolean rectOverlap(
            int x1, int y1, int w1, int h1,
            int x2, int y2, int w2, int h2) {

        return x1 < x2 + w2 &&
                x1 + w1 > x2 &&
                y1 < y2 + h2 &&
                y1 + h1 > y2;
    }

    public void dropItem(Item.Type type, int quantity, double x, double y) {
        droppedItems.add(new DroppedItem(type, quantity, x, y));
    }

    public void update() {
        trees.removeIf(tree -> tree.isRemoved());
        droppedItems.removeIf(drop -> drop.isCollected());
        blocks.removeIf(block -> block.isBroken());
        for (DroppedItem drop : droppedItems) {
            drop.animate();
        }
    }

    public void paint(Graphics2D g) {
        g.setColor(panel.timeManager.getSkyColor());
        g.fillRect(0, 0, panel.SCREEN_WIDTH, panel.SCREEN_HEIGHT);

        int groundY = panel.SCREEN_HEIGHT / 2 + (3 * size / 2);
        Color grass = new Color(13, 84, 23);
        g.setStroke(new BasicStroke(2));
        g.setColor(grass);
        g.fillRect(0, groundY, panel.SCREEN_WIDTH, size / 2);

        g.setColor(grass.darker());
        g.drawLine(0, groundY, panel.SCREEN_WIDTH, groundY);

        g.setColor(new Color(66, 37, 1));
        g.fillRect(0, groundY + size / 2, panel.SCREEN_WIDTH, 3 * size);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, groundY + 5 * size / 2, panel.SCREEN_WIDTH,
                size / 2);

        for (Tree tree : trees) {
            tree.paint(g);
        }

        for (DroppedItem drop : droppedItems) {
            drop.paint(g);
        }
        for (Block block : blocks) {
            block.paint(g);
        }

        g.setColor(panel.timeManager.isDay() ? Color.YELLOW : Color.WHITE);
        g.fillOval(10, 10, 16, 16);
        g.setStroke(new BasicStroke(1));

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 3; j++) {
                g.setColor(new Color(0, 0, 0, 30));
                g.drawRect(i * size, groundY + j * size, size, size);
            }
        }

        Item heldItem = panel.player.getSelectedItem();
        boolean holdingWood = heldItem != null
                && (heldItem.getType() == Item.Type.WOOD || heldItem.getType() == Item.Type.PLANK);
        if (holdingWood && panel.mouseY < groundY) {
            int gridX = (panel.mouseX / size) * size;
            int gridY = (panel.mouseY / size) * size;
            double dist = Math.sqrt(Math.pow(panel.mouseX - panel.player.getX(), 2) +
                    Math.pow(panel.mouseY - panel.player.getY(), 2));
            boolean inReach = dist <= panel.SIZE * 5;
            boolean valid = inReach && canPlace(gridX, gridY);
            if (valid) {
                g.setColor(new Color(255, 255, 255, 60));
                g.fillRect(gridX, gridY, size, size);
                g.setColor(new Color(255, 255, 255, 150));
                g.drawRect(gridX, gridY, size, size);
            }
        }
    }

    public boolean canPlace(int gridX, int gridY) {
        for (Block b : blocks) {
            if (b.getX() == gridX && b.getY() == gridY)
                return false;
        }
        int groundY = panel.SCREEN_HEIGHT / 2 + (3 * size / 2);

        if (gridY + size == groundY)
            return true;

        for (Block b : blocks) {
            if (b.getX() == gridX && b.getY() == gridY + size)
                return true;
            if (b.getX() == gridX && b.getY() == gridY - size)
                return true;
            if (b.getY() == gridY && b.getX() == gridX + size)
                return true;
            if (b.getY() == gridY && b.getX() == gridX - size)
                return true;
        }
        return false;
    }

    public void placeBlock(Block.Type type, int gridX, int gridY, Panel panel) {
        blocks.add(new Block(type, gridX, gridY, size, panel));
    }
}