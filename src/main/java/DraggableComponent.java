import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DraggableComponent extends JComponent {

    protected Point anchorPoint;
    protected boolean overbearing = false;

    public DraggableComponent(InfrastructureComponent component) {
        addDragListeners(component);
    }

    private void addDragListeners(InfrastructureComponent component) {
        final DraggableComponent handle = this;
        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                anchorPoint = e.getPoint();
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int anchorX = anchorPoint.x;
                int anchorY = anchorPoint.y;

                Point parentOnScreen = getParent().getLocationOnScreen();
                Point mouseOnScreen = e.getLocationOnScreen();
                Point position = new Point(mouseOnScreen.x - parentOnScreen.x - anchorX, mouseOnScreen.y - parentOnScreen.y - anchorY);
                setLocation(position);
                component.setLocation(position);

                if (overbearing) {
                    getParent().setComponentZOrder(handle, 0);
                    repaint();
                }
            }

//            @Override
//            public void mouseClicked(MouseEvent e) {
//                System.out.println("Clicked on: " + component);
//                Frame.frame.showDetails(component);
//            }
        });
    }

    public void setOverbearing(boolean overbearing) {
        this.overbearing = overbearing;
    }
}
