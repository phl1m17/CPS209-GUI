import java.awt.Color;

public class Character {
    protected final int size;
    protected final int maxHealth;
    protected int health;
    protected final Color colour;
    protected double characterX;
    protected double characterY;

    public Character(int size, int maxHealth, Color colour) {
        this.size = size;
        this.maxHealth = maxHealth;
        this.colour = colour;
        health = maxHealth;
    }

    public double getX() {
        return characterX;
    }

    public double getY() {
        return characterY;
    }
}
