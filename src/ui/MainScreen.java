import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;

public class MainScreen implements ActionListener {
    Panel panel;
    boolean active = true;

    private final Font titleFont = new Font("Arial", Font.BOLD, 48);
    private final Font descFont = new Font("Arial", Font.PLAIN, 15);
    private final Font defaultFont = new Font("Arial", Font.PLAIN, 12);

    private JButton startButton;
    private JLabel controlLabel;

    public MainScreen(Panel panel) {
        this.panel = panel;

        startButton = new JButton("Start Game");
        startButton.setBounds(panel.SCREEN_WIDTH / 2 - 80, panel.SCREEN_HEIGHT / 2, 160, 40);
        startButton.addActionListener(this);
        panel.add(startButton);

        controlLabel = new JLabel(
                "<html><center>" +
                        "<b>Controls</b><br><br>" +
                        "Left: A &nbsp;&nbsp; Right: D &nbsp;&nbsp; Jump: SPACE <br>(or arrow keys)<br><br>" +
                        "Inventory: E &nbsp;&nbsp;&nbsp; Pause: ESC<br>" +
                        "Attack / Break: Left Click &nbsp;&nbsp; Place: Right Click<br><br><br>" +
                        "<i>By Phil Clarence Manag &nbsp;&mdash;&nbsp; Student #: 501351456</i>" +
                        "</center></html>");
        controlLabel.setForeground(Color.WHITE);
        controlLabel.setHorizontalAlignment(JLabel.CENTER);
        controlLabel.setBounds(panel.SCREEN_WIDTH / 2 - 220, panel.SCREEN_HEIGHT / 2 + 65, 440, 220);
        panel.add(controlLabel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        active = false;
        startButton.setVisible(false);
        controlLabel.setVisible(false);
    }

    public void paint(Graphics2D g) {
        if (!active)
            return;

        g.setColor(new Color(30, 120, 50));
        g.fillRect(0, 0, panel.SCREEN_WIDTH, panel.SCREEN_HEIGHT);

        int cx = panel.SCREEN_WIDTH / 2;
        int titleY = panel.SCREEN_HEIGHT / 4;

        g.setFont(titleFont);
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        String title = "2D Survival Game";
        g.drawString(title, cx - fm.stringWidth(title) / 2, titleY);

        g.setColor(new Color(0, 0, 0, 60));
        g.drawString(title, cx - fm.stringWidth(title) / 2 + 2, titleY + 2);
        g.setColor(Color.WHITE);
        g.drawString(title, cx - fm.stringWidth(title) / 2, titleY);

        g.setFont(descFont);
        g.setColor(new Color(220, 255, 220));
        fm = g.getFontMetrics();

        String line1 = "Chop trees, craft weapons, and build shelter before the night falls.";
        String line2 = "How long can you survive?";
        int descY = titleY + 50;

        g.drawString(line1, cx - fm.stringWidth(line1) / 2, descY);
        g.drawString(line2, cx - fm.stringWidth(line2) / 2, descY + fm.getHeight() + 4);

        g.setFont(defaultFont);
    }
}