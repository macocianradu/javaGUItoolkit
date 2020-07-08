import parser.XMLParser;

public class Main {
    public static void main(String[] args) {
        try {
            XMLParser.parse("otherui.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
