import java.awt.*;
import java.awt.image.ImageObserver;

public class DraggableImageComponent extends DraggableComponent implements ImageObserver {

    protected Image image;

    public DraggableImageComponent(InfrastructureComponent component) {
        super();
        setLayout(null);
        setBackground(Color.black);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, getWidth(), getHeight());
        if (image != null) {
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
        if (infoflags == ALLBITS) {
            repaint();
            return false;
        }
        return true;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(String image) {
        setImage(Toolkit.getDefaultToolkit().getImage(image));
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }
}
