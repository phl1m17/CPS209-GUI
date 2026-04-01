import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class GameOverScreen implements ActionListener {
    Panel panel;
    boolean active = false;
    private final Font titleFont = new Font("Arial", Font.BOLD, 40);
    private final Font defaultFont = new Font("Arial", Font.PLAIN, 12);
    private JButton restartButton;
    private JButton quitButton;

    public GameOverScreen(Panel panel) {
        this.panel = panel;

        restartButton = new JButton("Restart");
        restartButton.setBounds(panel.SCREEN_WIDTH / 2 - 80, panel.SCREEN_HEIGHT / 2 + 20, 160, 40);
        restartButton.addActionListener(this);
        restartButton.setVisible(false);

        quitButton = new JButton("Quit");
        quitButton.setBounds(panel.SCREEN_WIDTH / 2 - 80, panel.SCREEN_HEIGHT / 2 + 80, 160, 40);
        quitButton.addActionListener(this);
        quitButton.setVisible(false);

        panel.add(restartButton);
        panel.add(quitButton);
    }

    public void show() {
        active = true;
        restartButton.setVisible(true);
        quitButton.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == restartButton) {
            active = false;
            restartButton.setVisible(false);
            restartButton.setEnabled(false);
            panel.restart();
            restartButton.setEnabled(true);
        }
        if (e.getSource() == quitButton) {
            System.exit(0);
        }
    }

    public void paint(Graphics2D g) {
        if (!active)
            return;

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, panel.SCREEN_WIDTH, panel.SCREEN_HEIGHT);

        int centerX = panel.SCREEN_WIDTH / 2;
        int centerY = panel.SCREEN_HEIGHT / 2;
        FontMetrics fm;

        g.setColor(Color.RED);
        g.setFont(titleFont);
        fm = g.getFontMetrics();
        String title = "YOU DIED";
        g.drawString(title, centerX - fm.stringWidth(title) / 2, centerY - 80);

        g.setFont(defaultFont);
        fm = g.getFontMetrics();
        g.setColor(Color.WHITE);

        String msg1 = "You were defeated!";
        String msg2 = "Better luck next time.";
        String days = "Days Survived: " + panel.dayCount;

        g.drawString(msg1, centerX - fm.stringWidth(msg1) / 2, centerY - 40);
        g.drawString(msg2, centerX - fm.stringWidth(msg2) / 2, centerY - 20);
        g.drawString(days, centerX - fm.stringWidth(days) / 2, centerY + 10);

        restartButton.setBounds(centerX - 80, centerY + 40, 160, 40);
    }
}