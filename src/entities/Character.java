import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Character {
    protected int size;
    protected int maxHealth;
    protected int health;
    protected final Color colour;
    protected double characterX, characterY;

    public Character(int size, int maxHealth, Color colour) {
        this.size = size;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.colour = colour;
    }

    public double getX() {
        return characterX;
    }

    public double getY() {
        return characterY;
    }

    public void takeDamage(int amount) {
        health -= amount;
    }

    public boolean isRemoved() {
        return health <= 0;
    }

    public abstract void paint(Graphics2D g);

    protected void paintBody(Graphics2D g, boolean movingLeft, boolean movingRight, boolean walkFrame,
            Color shirtColor, Color pantsColor) {
        int px = (int) characterX;
        int py = (int) characterY;

        int headWidth = 36;
        int headHeight = 36;
        int bodyWidth = 24;
        int bodyHeight = 44;
        int legWidth = 12;
        int legHeight = 42;
        int armWidth = 10;
        int armHeight = 10;

        // head
        int headX = px + size / 2 - headWidth / 2;
        int headY = py + 4;
        g.setColor(colour);
        g.fillRoundRect(headX, headY, headWidth, headHeight, 8, 8);

        // body
        int bodyX = px + size / 2 - bodyWidth / 2;
        int bodyY = headY + headHeight;

        // legs
        int legGap = 2;
        int leftLegX = px + size / 2 - legWidth - legGap / 2;
        int rightLegX = px + size / 2 + legGap / 2;
        int legsY = bodyY + bodyHeight - 3;
        int legOffset = (movingLeft || movingRight) ? (walkFrame ? 3 : -3) : 0;

        g.setColor(pantsColor);
        g.fillRoundRect(leftLegX, legsY, legWidth, legHeight - legOffset, 4, 4);
        g.fillRoundRect(rightLegX, legsY, legWidth, legHeight + legOffset, 4, 4);

        g.setColor(shirtColor);
        g.fillRoundRect(bodyX, bodyY, bodyWidth, bodyHeight, 6, 6);

        // boots
        g.setColor(Color.BLACK);
        g.fillRect(leftLegX - 2, legsY + legHeight - legOffset, legWidth + 2, 6);
        g.fillRect(rightLegX, legsY + legHeight + legOffset, legWidth + 2, 6);

        // arms
        int leftArmX = bodyX - armWidth;
        int rightArmX = bodyX + bodyWidth;
        int armsY = bodyY + 4;
        if (movingLeft) {
            rightArmX = bodyX + bodyWidth - armWidth;
        }
        if (movingRight) {
            leftArmX = bodyX;
        }

        g.setColor(colour);
        g.fillRoundRect(leftArmX, armsY, armWidth, armHeight, 4, 4);
        g.fillRoundRect(rightArmX, armsY, armWidth, armHeight, 4, 4);

        // g.setColor(Color.red);
        // g.drawRect(px, py, size, size * 2);
    }
}