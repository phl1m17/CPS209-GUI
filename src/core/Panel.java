import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Panel extends JPanel implements Runnable {
    protected final int SIZE = 64;
    final int SCREEN_WIDTH = SIZE * 16;
    final int SCREEN_HEIGHT = SIZE * 9;

    private Thread gameThread;

    Player player;
    TimeManager timeManager;
    InventoryScreen inventoryScreen;
    MainScreen mainScreen;
    GameOverScreen gameOverScreen;
    PowerUp powerUp;

    private int spawnTimer = 0;
    private final int SPAWN_INTERVAL = 30;
    ArrayList<Mob> mobs = new ArrayList<>();

    private int maxMobs;
    protected int worldIndex = 0;
    protected World[] worlds = new World[3];
    int dayCount = 0;
    boolean wasDay = true;

    public Panel() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setFocusable(true);
        setLayout(null);

        player = new Player(SIZE, 20, new Color(222, 175, 95), this);
        timeManager = new TimeManager();
        inventoryScreen = new InventoryScreen(this);
        mainScreen = new MainScreen(this);
        gameOverScreen = new GameOverScreen(this);

        restart();

        // mobs.add(new Zombie(SIZE, 1, Color.RED, this));

        addKeyListener(player.keyH);
    }

    private void addGameMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (player.keyH.pausePressed || mainScreen.active || gameOverScreen.active)
                    return;
                if (player.keyH.inventoryPressed) {
                    inventoryScreen.handleClick(e.getX(), e.getY());
                    return;
                }

                int mx = e.getX(), my = e.getY();
                double reach = SIZE * 5;

                if (tryHitMob(mx, my, reach))
                    return;
                if (tryHitTree(mx, my, reach))
                    return;
                trySelectSlot(mx, my);
            }
        });
    }

    private boolean tryHitMob(int mx, int my, double reach) {
        for (Mob mob : new ArrayList<>(mobs)) {
            if (mob.worldIndex != worldIndex || mob.isRemoved())
                continue;
            if (!mob.containsPoint(mx, my))
                continue;
            if (distanceTo(mob) > reach)
                return true;

            Item selected = player.getSelectedItem();
            int damage = calcDamage(selected, Item.Type.SWORD);
            mob.takeDamage(damage);
            return true;
        }
        return false;
    }

    private boolean tryHitTree(int mx, int my, double reach) {
        for (Tree tree : worlds[worldIndex + 1].trees) {
            if (tree.isRemoved() || !tree.containsPoint(mx, my))
                continue;
            if (mobAt(mx, my))
                return true;

            if (distanceTo(tree) <= reach) {
                Item selected = player.getSelectedItem();
                int damage = calcDamage(selected, Item.Type.AXE);
                tree.takeDamage(damage);
            }
            return true;
        }
        return false;
    }

    private void trySelectSlot(int mx, int my) {
        for (int i = 0; i < player.inventory.size(); i++) {
            int slotX = (int) (16 + SIZE * i * 1.25);
            int slotY = SCREEN_HEIGHT - 30 - SIZE;
            if (mx >= slotX && mx <= slotX + SIZE && my >= slotY && my <= slotY + SIZE) {
                player.setSelectedSlot(i);
                return;
            }
        }
    }

    private int calcDamage(Item selected, Item.Type toolType) {
        int damage = 1;
        if (selected != null && selected.getType() == toolType) {
            damage = 2;
            if (selected.useDurability())
                player.breakOne(toolType);
        }
        return damage * player.damageMultiplier;
    }

    private double distanceTo(Clickable target) {
        double dx = target.getX() - player.getX();
        double dy = target.getY() - player.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private boolean mobAt(int mx, int my) {
        for (Mob mob : mobs) {
            if (!mob.isRemoved() && mob.containsPoint(mx, my))
                return true;
        }
        return false;
    }

    public void restart() {
        player.playerRestart();

        mobs.clear();
        spawnTimer = 0;
        maxMobs = 2;
        dayCount = 0;
        wasDay = true;

        timeManager = new TimeManager();

        for (MouseListener ml : getMouseListeners()) {
            removeMouseListener(ml);
        }

        worlds[0] = new World(this, SIZE);
        worlds[1] = new World(this, SIZE);
        worlds[2] = new World(this, SIZE);
        worldIndex = 0;
        addGameMouseListener();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        if (player.keyH.pausePressed || mainScreen.active || gameOverScreen.active)
            return;

        player.playerMovement();
        player.playerUpdate();
        timeManager.update();

        boolean isDay = timeManager.isDay();
        if (isDay && !wasDay) {
            dayCount++;
            for (World world : worlds) {
                world.spawnTrees();
            }
            if (dayCount % 3 == 0)
                maxMobs++;

            if (dayCount % 4 == 0 && powerUp == null) {
                spawnPowerUp();
            }
        }

        if (powerUp != null) {
            powerUp.animate();

            if (powerUp != null && powerUp.worldIndex == worldIndex + 1) {
                if (powerUp.collidesWithPlayer((int) player.getX(), (int) player.getY(), SIZE)) {
                    player.setPowerUp(powerUp.getType());
                    powerUp = null;
                }
            }
        }
        wasDay = isDay;

        for (World w : worlds) {
            w.update();
        }

        for (DroppedItem drop : worlds[worldIndex + 1].droppedItems) {
            if (!drop.isCollected() && drop.collidesWithPlayer(player.getX(), player.getY(), SIZE)) {
                player.addItem(drop.getType(), drop.getQuantity());
                drop.collect();
            }
        }

        mobs.removeIf(mob -> mob.isRemoved());

        for (Mob mob : mobs) {
            if (mob.worldIndex == worldIndex) {
                mob.update(player.getX());
            }
        }

        if (player.health <= 0) {
            gameOverScreen.show();
            return;
        }

        if (timeManager.getDayProgress() < 0.6 || !timeManager.isDay()) {
            spawnTimer++;
            if (spawnTimer >= SPAWN_INTERVAL && mobs.size() < maxMobs) {
                int attempts = 0;
                Mob candidate = null;
                while (attempts < 100) {
                    Color mobColour = new Color(
                            44 + (int) (Math.random() * 100),
                            74 + (int) (Math.random() * 100),
                            38 + (int) (Math.random() * 100));
                    Mob m = new Zombie(SIZE, 4, mobColour, this);
                    if (!player.tooCloseToPlayer(m.getX())) {
                        candidate = m;
                        break;
                    }
                    attempts++;
                }
                if (candidate != null) {
                    mobs.add(candidate);
                }
                spawnTimer = 0;
            }
        } else {
            spawnTimer = 0;
        }

        for (DroppedItem drop : worlds[worldIndex + 1].droppedItems) {
            if (!drop.isCollected() && drop.collidesWithPlayer(player.getX(), player.getY(), SIZE)) {
                player.addItem(drop.getType(), drop.getQuantity());
                drop.collect();
            }
        }
    }

    private void spawnPowerUp() {
        int randomWorld = (int) (Math.random() * 3);
        powerUp = new PowerUp(this, (int) (Math.random() * 2), SIZE, randomWorld);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        if (!mainScreen.active) {
            worlds[worldIndex + 1].paint(g2);
            player.paint(g2);
            for (Mob mob : mobs) {
                if (mob.worldIndex == worldIndex) {
                    mob.paint(g2);
                }
            }
            if (powerUp != null) {
                powerUp.paint(g2);
            }
            if (player.keyH.pausePressed || player.keyH.inventoryPressed) {
                g2.setColor(new Color(0, 0, 0, 100));
                g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                if (player.keyH.inventoryPressed) {
                    inventoryScreen.paint(g2);
                }
            }
            player.paintDamage(g2);
            gameOverScreen.paint(g2);
        } else {
            mainScreen.paint(g2);
        }
    }

    @Override
    public void run() {
        final double drawInterval = 1000000000 / 60;
        double delta = 0;
        long lastTime = System.nanoTime();
        while (gameThread.isAlive()) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }
}