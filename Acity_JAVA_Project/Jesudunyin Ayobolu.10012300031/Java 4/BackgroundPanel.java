import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private static ImageIcon backgroundImage;

    public BackgroundPanel() {
        backgroundImage = new ImageIcon("bg1.jpg");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Image img = backgroundImage.getImage();
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }
    }
}