import javax.swing.*;
import java.awt.*;

public class DragDropPanel extends JPanel {

    private Network network;

    public DragDropPanel(Network network) {
        this.network = network;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (InfrastructureComponent component : network.getAllComponentsCopy()) {

            Vector2 mousePosition = new Vector2(
                    MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x,
                    MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y
            );
            if (component.getPosition().equals(new Vector2())) {
                component.setPosition(mousePosition);
            }

            drawInfrastructureComponent(component, g);
        }
    }

    private void drawInfrastructureComponent(InfrastructureComponent component, Graphics g) {
        switch (component.getType()) {
            case Web -> g.setColor(Color.green);
            case Database -> g.setColor(Color.yellow);
            case Firewall -> g.setColor(Color.red);
        }
        g.fillRect(component.getPosition().x - 15, component.getPosition().y - 15, 30, 30);
        g.setColor(Color.lightGray);
        Rectangle textRectangle = new Rectangle(component.getPosition().x - 20, component.getPosition().y + 15, 40, 15);
        g.setColor(Color.black);
        drawCenteredString(g, component.getComponentName(), textRectangle, new Font("Default", Font.BOLD, 14));
    }

    //From https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
    public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }
}
