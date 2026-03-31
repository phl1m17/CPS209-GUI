import java.awt.Color;

public class TimeManager {
    private final int DAY_LENGTH = 60 * 46;
    private int ticks = DAY_LENGTH / 4;

    public void update() {
        ticks = (ticks + 1) % DAY_LENGTH;
    }

    public double getDayProgress() {
        return Math.sin(Math.PI * ticks / DAY_LENGTH);
    }

    public boolean isDay() {
        return ticks < DAY_LENGTH / 2;
    }

    public Color getSkyColor() {
        int dayR = 143, dayG = 201, dayB = 255;
        int nightR = 8, nightG = 8, nightB = 18;

        double t = getDayProgress();
        int r = (int) (nightR + t * (dayR - nightR));
        int g = (int) (nightG + t * (dayG - nightG));
        int b = (int) (nightB + t * (dayB - nightB));

        return new Color(r, g, b);
    }
}