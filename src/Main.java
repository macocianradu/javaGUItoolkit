import guiTree.Window;
import parser.XAMLParser;

public class Main {
    public static void main(String[] args) {
        Window window = null;
        try {
            window = XAMLParser.parse("ui.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
