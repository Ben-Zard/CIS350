import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Graphics;
class BackgroundPanel extends JPanel
{

    protected Image bgImage;
    public BackgroundPanel(Image image)
    {
        super(true);
        bgImage = image;
        setOpaque(false);
    }
    public void paint(Graphics g)
    {
        g.drawImage(bgImage, 0 ,0 ,this);
        super.paint(g);
    }
} // BackgroundPanel