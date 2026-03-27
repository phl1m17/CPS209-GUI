import javax.swing.JFrame;

public class Project2Runner {

    public static void main(String[] args) {
        JFrame window = new JFrame("2D Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Panel panel = new Panel();

        window.add(panel);
        window.pack();
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        panel.startGameThread();
    }
}
