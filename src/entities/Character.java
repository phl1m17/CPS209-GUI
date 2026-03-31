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
}