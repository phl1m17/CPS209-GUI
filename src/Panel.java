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

    int maxMobs = 3;
    int worldIndex = 0;
    World[] worlds = new World[3];
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

        worlds[0] = new World(this, SIZE);
        worlds[1] = new World(this, SIZE);
        worlds[2] = new World(this, SIZE);

        addKeyListener(player.keyH);
        addGameMouseListener();
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

                double reach = SIZE * 5;
                Item selected = player.getSelectedItem();

                for (Mob mob : new ArrayList<>(mobs)) {
                    if (mob.worldIndex != worldIndex)
                        continue;
                    if (!mob.isRemoved() && mob.containsPoint(e.getX(), e.getY())) {
                        double dx = mob.getX() - player.getX();
                        double dy = mob.getY() - player.getY();
                        if (Math.sqrt(dx * dx + dy * dy) <= reach) {
                            int damage = 1;
                            if (selected != null && selected.getType() == Item.Type.SWORD) {
                                damage = 2;
                                if (selected.useDurability()) {
                                    player.breakOne(Item.Type.SWORD);
                                }
                            }
                            damage *= player.damageMultiplier;
                            mob.takeDamage(damage);
                        }
                        return;
                    }
                }
                for (Tree tree : worlds[worldIndex + 1].trees) {
                    if (!tree.isRemoved() && tree.containsPoint(e.getX(), e.getY())) {
                        boolean mobOnTree = false;
                        for (Mob mob : mobs) {
                            if (!mob.isRemoved() && mob.containsPoint(e.getX(), e.getY())) {
                                mobOnTree = true;
                                break;
                            }
                        }
                        if (!mobOnTree) {
                            double dx = tree.getX() - player.getX();
                            double dy = tree.getY() - player.getY();
                            if (Math.sqrt(dx * dx + dy * dy) <= reach) {
                                int damage = 1;
                                if (selected != null && selected.getType() == Item.Type.AXE) {
                                    damage = 2;
                                    if (selected.useDurability()) {
                                        player.breakOne(Item.Type.AXE);
                                    }
                                }
                                tree.takeDamage(damage);
                            }
                        }
                        return;
                    }
                }

                for (int i = 0; i < player.inventory.size(); i++) {
                    int slotX = (int) (16 + SIZE * i * 1.25);
                    int slotY = SCREEN_HEIGHT - 30 - SIZE;
                    if (e.getX() >= slotX && e.getX() <= slotX + SIZE &&
                            e.getY() >= slotY && e.getY() <= slotY + SIZE) {
                        player.setSelectedSlot(i);
                        break;
                    }
                }
            }
        });
    }

    public void restart() {
        player.health = player.maxHealth;
        player.emptyInventory();
        player.resetPosition();

        mobs.clear();
        spawnTimer = 0;
        maxMobs = 3;
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
            if (dayCount % 2 == 0) {
                for (int i = 0; i < worlds.length; i++) {
                    worlds[i].spawnTrees();
                }
            }
            if (dayCount % 4 == 0)
                maxMobs++;

            if (dayCount % 1 == 0 && powerUp == null) {
                spawnPowerUp();
            }
        }

        if (powerUp != null) {
            powerUp.animatePowerUp();

            if (powerUp != null && powerUp.worldIndex == worldIndex + 1) {
                if (powerUp.collides((int) player.getX(), (int) player.getY(), SIZE)) {
                    player.setPowerUp(powerUp.getType());
                    powerUp = null;
                }
            }
        }
        wasDay = isDay;

        for (World w : worlds) {
            w.update();
        }

        mobs.removeIf(mob -> mob.isRemoved());

        for (Mob mob : mobs) {
            if (mob.worldIndex == worldIndex) {
                mob.moveMob(player.getX());
            }
        }

        if (player.health <= 0) {
            gameOverScreen.show();
            return;
        }

        if (!timeManager.isDay()) {
            spawnTimer++;
            if (spawnTimer >= SPAWN_INTERVAL && mobs.size() < maxMobs) {
                int attempts = 0;
                Mob candidate = null;
                while (attempts < 100) {
                    Color mobColour = new Color(
                            44 + (int) (Math.random() * 100),
                            74 + (int) (Math.random() * 100),
                            38 + (int) (Math.random() * 100));
                    Mob m = new Mob(SIZE, 4, mobColour, this, worldIndex);
                    if (!player.tooCloseToPlayer(m.getX())) {
                        candidate = m;
                        break;
                    }
                    attempts++;
                }
                if (candidate != null) {
                    // mobs.add(candidate);
                }
                spawnTimer = 0;
            }
        } else {
            spawnTimer = 0;
        }
    }

    private void spawnPowerUp() {
        powerUp = new PowerUp(this, (int) (Math.random() * 2), SIZE);
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
                powerUp.paintComponent(g2);
            }
            if (player.keyH.pausePressed || player.keyH.inventoryPressed) {
                g2.setColor(new Color(0, 0, 0, 100));
                g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                if (player.keyH.inventoryPressed) {
                    inventoryScreen.paint(g2);
                }
            }
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