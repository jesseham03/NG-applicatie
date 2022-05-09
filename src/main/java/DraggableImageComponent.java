import java.awt.*;
import java.awt.image.ImageObserver;

import static java.awt.Toolkit.getDefaultToolkit;

public class DraggableImageComponent extends DraggableComponent implements ImageObserver {

    private final InfrastructureComponent component;

    public DraggableImageComponent(InfrastructureComponent component) {
        super();
        this.component = component;
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        switch (component.getType()) {
            case Web -> setImage(g2d, "/webservericon.png");
            case Database -> setImage(g2d, "/databaseservericon.png");
            case Firewall -> setImage(g2d, "/firewallicon.png");
        }
        Rectangle textRectangle = new Rectangle(0, getHeight() - 20, getWidth(), 15);
        drawCenteredString(g, component.getComponentName(), textRectangle, new Font("Default", Font.BOLD, 10));
    }

    //From https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
    private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }

    private void setImage(Graphics2D g2d, String imagePath) {
        Image image = getDefaultToolkit().getImage(getClass().getResource(imagePath));
        if (image == null) {
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
        } else {
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
        repaint();
    }
}
