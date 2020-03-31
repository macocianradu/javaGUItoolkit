import guiTree.Components.ScrollPanel;
import guiTree.Window;
import parser.XAMLParser;

public class Main {
    public static void main(String[] args) {
        try{
            Window window = XAMLParser.parse("ui.xml");
            assert window != null;
            ScrollPanel scrollPanel = (ScrollPanel)window.findByName("ScrollPane");
            System.out.println();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
