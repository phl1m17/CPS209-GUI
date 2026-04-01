import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    boolean leftPressed, rightPressed, jumpPressed;
    boolean pausePressed, inventoryPressed = false;
    Panel panel;

    public KeyHandler(Panel panel) {
        this.panel = panel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT)
            leftPressed = true;
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT)
            rightPressed = true;
        if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP)
            jumpPressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT)
            leftPressed = false;
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT)
            rightPressed = false;
        if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_W || key == KeyEvent.VK_UP)
            jumpPressed = false;
        if (key == KeyEvent.VK_ESCAPE) {
            if (panel.gameOverScreen.active)
                return;
            if (inventoryPressed) {
                inventoryPressed = false;
                return;
            }
            pausePressed = !pausePressed;
        }
        if (key == KeyEvent.VK_E && !pausePressed && !panel.gameOverScreen.active && !panel.mainScreen.active)
            inventoryPressed = !inventoryPressed;
        if (key == KeyEvent.VK_1 && !pausePressed && !panel.gameOverScreen.active && !panel.mainScreen.active)
            panel.player.setSelectedSlot(0);
        if (key == KeyEvent.VK_2 && !pausePressed && !panel.gameOverScreen.active && !panel.mainScreen.active)
            panel.player.setSelectedSlot(1);
        if (key == KeyEvent.VK_3 && !pausePressed && !panel.gameOverScreen.active && !panel.mainScreen.active)
            panel.player.setSelectedSlot(2);
        if (key == KeyEvent.VK_4 && !pausePressed && !panel.gameOverScreen.active && !panel.mainScreen.active)
            panel.player.setSelectedSlot(3);
    }
}
