import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

public class Player extends Character {
    KeyHandler keyH;
    private final double MAX_SPEED = 6;
    private final double ACCELERATION = 0.8;
    private final double FRICTION = 0.75;
    private double velocityX = 0;
    private double velocityY = 0;
    private boolean isGrounded = false;
    private final double GRAVITY = 0.5;
    private final double JUMP_FORCE = -12;
    private final int MAX_SLOTS = 3;
    private int GROUND;
    private int selectedSlot = 0;
    protected int damageMultiplier = 1;
    protected int strengthTimer;
    protected boolean paintDamage = false;
    protected int paintDamageTimer = 0;
    private boolean walkFrame = false;
    private int walkTimer = 0;

    LinkedList<Inventory> inventory = new LinkedList<>();
    Panel panel;

    public Player(int size, int health, Color color, Panel panel) {
        super(size, health, color);
        keyH = new KeyHandler(panel);
        this.panel = panel;

        characterX = panel.SCREEN_WIDTH / 2 - size / 2;
        characterY = panel.SCREEN_HEIGHT / 2 - size / 2;
        GROUND = (int) characterY;

        emptyInventory();
    }

    public void emptyInventory() {
        inventory.clear();
        for (int i = 0; i < MAX_SLOTS; i++) {
            inventory.add(new Inventory(panel, size));
        }
    }

    private Inventory findSlot(Item.Type type) {
        for (Inventory slot : inventory) {
            if (!slot.isEmpty() && slot.getItem().getType() == type) {
                return slot;
            }
        }
        return null;
    }

    private Inventory findEmptySlot() {
        for (Inventory slot : inventory) {
            if (slot.isEmpty())
                return slot;
        }
        return null;
    }

    public void addItem(Item.Type type, int amount) {
        Inventory existing = findSlot(type);
        if (existing != null) {
            existing.getItem().addQuantity(amount);
            if (existing.getItem().getQuantity() <= 0) {
                inventory.remove(existing);
                inventory.add(new Inventory(panel, size));
            }
        } else if (amount > 0) {
            Inventory empty = findEmptySlot();
            if (empty != null) {
                empty.setItem(new Item(type, amount));
            }
        }
    }

    public Item getSelectedItem() {
        Inventory slot = inventory.get(selectedSlot);
        return slot.isEmpty() ? null : slot.getItem();
    }

    public void setSelectedSlot(int slot) {
        if (slot >= 0 && slot < MAX_SLOTS)
            selectedSlot = slot;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public void breakOne(Item.Type type) {
        Inventory slot = findSlot(type);
        if (slot == null)
            return;

        slot.getItem().addQuantity(-1);
        slot.getItem().resetDurability();

        if (slot.getItem().getQuantity() <= 0) {
            inventory.remove(slot);
            inventory.add(new Inventory(panel, size));
        }
    }

    public boolean canCraft(Item.Type result) {
        Inventory woodSlot = findSlot(Item.Type.WOOD);
        int woodQty = woodSlot == null ? 0 : woodSlot.getItem().getQuantity();
        switch (result) {
            case SWORD:
                return woodQty >= 6;
            case AXE:
                return woodQty >= 4;
            case PLANK:
                return woodQty >= 1;
            default:
                return false;
        }
    }

    public boolean craft(Item.Type result) {
        if (!canCraft(result))
            return false;
        switch (result) {
            case SWORD:
                addItem(Item.Type.WOOD, -6);
                addItem(Item.Type.SWORD, 1);
                return true;
            case AXE:
                addItem(Item.Type.WOOD, -4);
                addItem(Item.Type.AXE, 1);
                return true;
            case PLANK:
                addItem(Item.Type.WOOD, -1);
                addItem(Item.Type.PLANK, 4);
                return true;
            default:
                return false;
        }
    }

    public void paint(Graphics2D g) {
        paintBody(g,
                keyH.leftPressed, keyH.rightPressed, walkFrame,
                new Color(255, 255, 129),
                new Color(93, 174, 236));
        paintFace(g);
        paintHUD(g);
        paintItem(g);
        for (int i = 0; i < inventory.size(); i++) {
            inventory.get(i).paint(g, i, i == selectedSlot);
        }
    }

    public void paintFace(Graphics2D g) {
        g.setColor(Color.BLACK);
        int x1 = keyH.leftPressed ? (int) characterX + 18
                : keyH.rightPressed ? (int) characterX + 30 : (int) characterX + 24;
        int eyeGap = 10;
        int x2 = x1 + eyeGap + 3;
        int y = (int) characterY + 15;

        g.fillRect(x1, y, 3, 9);
        g.fillRect(x2, y, 3, 9);
    }

    public void paintItem(Graphics2D g) {
        Item item = getSelectedItem();
        boolean left = keyH.leftPressed;
        boolean right = keyH.rightPressed;
        if (item != null) {
            int gap = 0;
            switch (item.getType()) {
                case WOOD:
                    gap = left ? -18 : right ? 24 : -18;
                    break;

                case AXE:
                    gap = left ? -6 : right ? 18 : -8;
                    break;
                case SWORD:
                    gap = left ? -2 : right ? 16 : -4;
                    break;
                case PLANK:
                    gap = left ? -18 : right ? 24 : -18;
                    break;
            }

            int x = (int) characterX + gap;
            int y = (int) characterY + 24;
            item.paint(g, x, y, size / 2, true, true, right);
        }
    }

    public void paintHUD(Graphics2D g) {
        int radius = 16;
        int x = 10;
        int y = 30;

        int normalHearts = Math.min(maxHealth / 2, health / 2);
        int extraHearts = Math.max(0, (health - maxHealth) / 2);

        for (int i = 0; i < normalHearts; i++) {
            g.setColor(Color.RED);
            g.fillOval((int) (x + radius * i * 1.25), y, radius, radius);

            g.setColor(Color.WHITE);
            g.drawOval((int) (x + radius * i * 1.25), y, radius, radius);
        }

        for (int i = 0; i < extraHearts; i++) {
            int index = normalHearts + i;

            g.setColor(new Color(224, 180, 20));
            g.fillOval((int) (x + radius * index * 1.25), y, radius, radius);

            g.setColor(Color.WHITE);
            g.drawOval((int) (x + radius * index * 1.25), y, radius, radius);
        }

        if (strengthTimer > 0) {

            int boxWidth = 140;
            int boxHeight = 36;

            int boxX = panel.SCREEN_WIDTH - boxWidth - 10;
            int boxY = 10;

            g.setColor(new Color(0, 0, 0, 150));
            g.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 12, 12);

            g.setColor(Color.WHITE);
            g.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 12, 12);

            int potionSize = 18;
            int potionX = boxX + 8;
            int potionY = boxY + 9;

            g.setColor(new Color(204, 101, 10));
            g.fillOval(potionX, potionY, potionSize, potionSize);

            g.setColor(Color.WHITE);
            g.drawOval(potionX, potionY, potionSize, potionSize);

            int totalSeconds = strengthTimer / 60;

            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;

            String timeText;

            if (minutes == 0) {
                timeText = seconds + "s";
            } else if (seconds == 0) {
                timeText = minutes + "m";
            } else {
                timeText = minutes + "m " + seconds + "s";
            }

            g.drawString("STRENGTH",
                    potionX + potionSize + 8,
                    boxY + 15);

            g.drawString(timeText,
                    potionX + potionSize + 8,
                    boxY + 30);
        }
    }

    public void playerUpdate() {
        for (Mob mob : panel.mobs) {
            if (mob.worldIndex != panel.worldIndex)
                continue;
            if (mob instanceof Zombie) {
                Zombie z = (Zombie) mob;
                if (z.collidesWithAny(characterX, characterY, size)) {
                    if (z.canDamage()) {
                        damagePlayer();
                        z.resetDamageCooldown();
                    }
                }
            }
        }
        if (strengthTimer > 0) {
            strengthTimer--;
            if (strengthTimer == 0)
                damageMultiplier = 1;
        }
    }

    public void damagePlayer() {
        health--;
        paintDamage = true;
    }

    public void paintDamage(Graphics2D g) {
        if (paintDamage) {
            if (paintDamageTimer < 20) {
                paintDamageTimer++;
            } else {
                paintDamage = false;
                paintDamageTimer = 0;
            }
            g.setColor(new Color(255, 25, 25, 30));
            g.fillRect(0, 0, panel.SCREEN_WIDTH, panel.SCREEN_HEIGHT);
        }
    }

    public void playerRestart() {
        health = maxHealth;
        emptyInventory();
        resetPosition();
        strengthTimer = 0;
        damageMultiplier = 1;
        paintDamage = false;
    }

    public void playerMovement() {
        applyHorizontalMovement();
        applyVerticalMovement();
        if (keyH.leftPressed || keyH.rightPressed) {
            walkTimer++;
            if (walkTimer >= 16) {
                walkFrame = !walkFrame;
                walkTimer = 0;
            }
        } else {
            walkFrame = false;
            walkTimer = 0;
        }
    }

    private void applyHorizontalMovement() {
        if (keyH.leftPressed)
            velocityX -= ACCELERATION;
        if (keyH.rightPressed)
            velocityX += ACCELERATION;
        if (!keyH.leftPressed && !keyH.rightPressed)
            velocityX *= FRICTION;
        velocityX = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, velocityX));

        double nextX = characterX + velocityX;

        if (tryWorldTransition(nextX))
            return;

        boolean blockedX = nextX <= 0 || nextX + size >= panel.SCREEN_WIDTH;
        for (Tree tree : panel.worlds[panel.worldIndex + 1].trees) {
            if (tree.collidesWithAny(nextX, characterY + size, size)) {
                blockedX = true;
                break;
            }
        }

        for (Block block : panel.worlds[panel.worldIndex + 1].blocks) {
            if (block.collidesWithAny(nextX, characterY, size, size * 2)) {
                blockedX = true;
                break;
            }
        }

        if (!blockedX)
            characterX = nextX;
        else
            velocityX = 0;
    }

    private boolean tryWorldTransition(double nextX) {
        if (nextX <= 0 && panel.worldIndex > -1) {
            double shift = panel.SCREEN_WIDTH - size - 1;
            panel.worldIndex--;
            characterX = panel.SCREEN_WIDTH - size - 1;
            velocityX = 0;
            for (Mob mob : panel.mobs) {
                mob.worldIndex = panel.worldIndex;
                mob.setX((int) (mob.getX() + shift));
            }
            return true;
        }
        if (nextX + size >= panel.SCREEN_WIDTH && panel.worldIndex < 1) {
            double shift = -(panel.SCREEN_WIDTH - size - 1);
            panel.worldIndex++;
            characterX = 1;
            velocityX = 0;
            for (Mob mob : panel.mobs) {
                mob.worldIndex = panel.worldIndex;
                mob.setX((int) (mob.getX() + shift));
            }
            return true;
        }
        return false;
    }

    private void applyVerticalMovement() {
        if (keyH.jumpPressed && isGrounded) {
            velocityY = JUMP_FORCE;
            isGrounded = false;
        }
        velocityY += GRAVITY;

        double nextY = characterY + velocityY;
        boolean blockedY = false;
        for (Tree tree : panel.worlds[panel.worldIndex + 1].trees) {
            if (tree.collidesWithAny(characterX, nextY + size, size)) {
                blockedY = true;
                break;
            }
        }
        for (Block block : panel.worlds[panel.worldIndex + 1].blocks) {
            if (block.collidesWithAny(characterX, nextY, size, size * 2)) {
                blockedY = true;
                break;
            }
        }
        if (!blockedY)
            characterY = nextY;
        else {
            if (velocityY > 0)
                isGrounded = true;
            velocityY = 0;
        }

        if (characterY >= GROUND) {
            characterY = GROUND;
            velocityY = 0;
            isGrounded = true;
        }
    }

    public void setPowerUp(PowerUp.Type type) {
        switch (type) {
            case HEALTH:
                health += 4;
                break;
            case STRENGTH:
                damageMultiplier = 2;
                strengthTimer += 60 * 60;
                break;
            default:
                break;
        }
    }

    public void resetPosition() {
        characterX = panel.SCREEN_WIDTH / 2 - size / 2;
        characterY = GROUND;
        velocityX = 0;
        velocityY = 0;
        isGrounded = false;
    }

    public boolean tooCloseToPlayer(double posX) {
        return Math.abs(posX - characterX) < size * 2;
    }
}