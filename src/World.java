import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class World {
    Panel panel;
    int size;
    ArrayList<Tree> trees = new ArrayList<>();
    private final int MAX_TREES = 2;

    public World(Panel panel, int size) {
        this.panel = panel;
        this.size = size;
        spawnTrees();
    }

    public void update() {
        trees.removeIf(tree -> tree.isRemoved());
    }

    public void spawnTrees() {
        int attempts = 0;
        while (trees.size() < MAX_TREES && attempts < 100) {
            Tree candidate = new Tree(panel, panel.SIZE, 2);

            boolean overlapsTree = false;
            for (Tree existing : trees) {
                if (candidate.overlaps(existing)) {
                    overlapsTree = true;
                    break;
                }
            }

            boolean tooClose = panel.player.tooCloseToPlayer(candidate.getX());

            if (!overlapsTree && !tooClose) {
                trees.add(candidate);
            }

            attempts++;
        }
    }

    public void paint(Graphics g) {
        g.setColor(panel.timeManager.getSkyColor());
        g.fillRect(0, 0, panel.SCREEN_WIDTH, panel.SCREEN_HEIGHT);

        int groundY = panel.SCREEN_HEIGHT / 2 + (3 * size / 2);

        g.setColor(new Color(13, 84, 23));
        g.fillRect(0, groundY, panel.SCREEN_WIDTH, panel.SCREEN_HEIGHT / 15);
        g.setColor(new Color(66, 37, 1));
        g.fillRect(0, groundY + panel.SCREEN_HEIGHT / 15, panel.SCREEN_WIDTH, panel.SCREEN_HEIGHT / 5);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, groundY + panel.SCREEN_HEIGHT / 15 + panel.SCREEN_HEIGHT / 5, panel.SCREEN_WIDTH,
                panel.SCREEN_HEIGHT);

        for (Tree tree : trees) {
            tree.paint(g);
        }
        g.setColor(panel.timeManager.isDay() ? Color.YELLOW : Color.WHITE);
        g.fillOval(10, 10, 16, 16);
    }
}