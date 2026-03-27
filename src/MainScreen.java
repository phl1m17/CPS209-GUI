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
    private final Font titleFont = new Font("Arial", Font.BOLD, 40);
    private final Font defaultFont = new Font("Arial", Font.PLAIN, 12);
    private JButton startButton;
    private JLabel subtitleLabel;

    public MainScreen(Panel panel) {
        this.panel = panel;

        startButton = new JButton("Start Game");
        startButton.setBounds(panel.SCREEN_WIDTH / 2 - 80, panel.SCREEN_HEIGHT / 2, 160, 40);
        startButton.addActionListener(this);
        panel.add(startButton);

        subtitleLabel = new JLabel(
                "<html><center>" +
                        "<br><br><b>How to Play</b><br><br>" +
                        "Move: Left Right Arrow / A D &nbsp;&nbsp; Jump: Up Arrow / SPACE <br>" +
                        "Inventory: E &nbsp;&nbsp; Pause: ESC<br><br>" +
                        "Chop trees, craft weapons, survive the night!<br><br><br><br><br>" +
                        "<i>By Phil Clarence Manag</i><br>" +
                        "<i>Student #: 501351456</i>" +
                        "</center></html>");
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);
        subtitleLabel.setBounds(panel.SCREEN_WIDTH / 2 - 200, panel.SCREEN_HEIGHT / 2 + 30, 400, 250);
        panel.add(subtitleLabel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        active = false;
        startButton.setVisible(false);
        subtitleLabel.setVisible(false);
    }

    public void paint(Graphics2D g) {
        if (!active)
            return;

        g.setColor(new Color(109, 199, 112));
        g.fillRect(0, 0, panel.SCREEN_WIDTH, panel.SCREEN_HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(titleFont);
        String title = "2D Game";
        FontMetrics fm = g.getFontMetrics();
        int x = panel.SCREEN_WIDTH / 2 - fm.stringWidth(title) / 2;
        g.drawString(title, x, panel.SCREEN_HEIGHT / 3);
        g.setFont(defaultFont);
    }
}