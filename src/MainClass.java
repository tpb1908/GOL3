import javax.swing.*;
import java.awt.*;


public class MainClass
{
    /**
     * Main method of this application
     */
    public static void main(final String[] arg)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                //Sets up the frame
                JFrame frame = new WindowManager();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);

                frame.setLocationRelativeTo(null);
            }
        });
        Animal one = new Animal("An animal", 1, 5);
        one.increaseAge(5);
    }
}