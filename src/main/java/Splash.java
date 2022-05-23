import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Splash extends JWindow{

    public Splash() {
    JWindow j=new JWindow();

    Dimension d=Toolkit.getDefaultToolkit().getScreenSize();

    Icon img= new ImageIcon(this.getClass().getResource("Favicon2.png"));
    JLabel label = new JLabel(img);
    label.setSize(200,300);
    j.getContentPane().add(label);
        j.setShape(new RoundRectangle2D.Double(0, 0, 400, 400, 150, 150));
        j.setBounds(((int)d.getWidth()-400)/2,((int)d.getHeight()-400)/2,400,400);
    j.setVisible(true);
    try{
        Thread.sleep(6000);
    }
    catch(InterruptedException e)
    {
        e.printStackTrace();
    }
     j.setVisible(false);
    }
}
