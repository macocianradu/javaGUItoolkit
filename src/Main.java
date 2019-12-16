import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        Window win = new Window();
        win.setVisible(true);
        win.setSize(640, 480);
        win.setPositionRelativeTo(null);
        win.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                win.dispose();
            }
        });

        Button button = new Button();
        win.addVisual(button);
    }
}
