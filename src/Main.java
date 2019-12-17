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
        int x = 20;
        int y = 20;
        while(true){
            if(x > win.getWidth()){
                x = 0;
                y+=20;
            }
            if(y > win.getHeight()){
                y = 0;
            }
            button.setLocation(x, y);
            x+=10;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
