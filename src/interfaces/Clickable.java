public interface Clickable {
    double getX();

    double getY();

    boolean containsPoint(int x, int y);

    void onClick();
}