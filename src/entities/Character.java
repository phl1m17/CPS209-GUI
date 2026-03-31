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
        int u = 2;

        // head
        int headWidth = 16 * u;
        int headHeight = 14 * u;
        int headX = px + size / 2 - headWidth / 2;
        int headY = py + 2 * u;
        g.setColor(colour);
        g.fillRoundRect(headX, headY, headWidth, headHeight, 6, 6);

        // body
        int bodyWidth = 10 * u;
        int bodyHeight = 8 * u;
        int bodyX = px + size / 2 - bodyWidth / 2;
        int bodyY = headY + headHeight;
        g.setColor(shirtColor);
        g.fillRoundRect(bodyX, bodyY, bodyWidth, bodyHeight, 6, 6);

        // pants
        int pantsWidth1 = 2 * u;
        int pantsWidth2 = 3 * u;
        int pantsHeight1 = 2 * u;
        int pantsHeight2 = 6 * u;
        int pantsX1 = px + size / 2 - pantsWidth1 / 2;
        int pantsX2 = pantsX1 - pantsWidth2;
        int pantsX3 = pantsX1 + pantsWidth1;
        int pantsY = bodyY + bodyHeight;
        int legOffset = (movingLeft || movingRight) ? (walkFrame ? u : -u) : 0;

        g.setColor(pantsColor);
        g.fillRect(pantsX1, pantsY, pantsWidth1, pantsHeight1);
        g.fillRect(pantsX2, pantsY, pantsWidth2, pantsHeight2 - legOffset);
        g.fillRect(pantsX3, pantsY, pantsWidth2, pantsHeight2 + legOffset);

        g.setColor(Color.BLACK);
        g.fillRect(pantsX2 - u, pantsY + pantsHeight2 - legOffset, pantsWidth2 + u, 2 * u);
        g.fillRect(pantsX3, pantsY + pantsHeight2 + legOffset, pantsWidth2 + u, 2 * u);

        // arms
        int armsWidth = 4 * u;
        int armsHeight = 3 * u;
        int armsX1 = headX - u;
        int armsX2 = headX + headWidth - 3 * u;
        int armsY = headY + headHeight + 2 * u;
        if (movingLeft)
            armsX2 = headX + headWidth - 6 * u;
        if (movingRight)
            armsX1 = headX + armsWidth - 2 * u;

        g.setColor(colour);
        g.fillRoundRect(armsX1, armsY, armsWidth, armsHeight, 6, 6);
        g.fillRoundRect(armsX2, armsY, armsWidth, armsHeight, 6, 6);
    }
}