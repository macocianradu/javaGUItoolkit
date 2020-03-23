package guiTree.Helper;

public class Debugger {
    private static Timer timer = new Timer();

    public static void log(String message, Tag tag) {
        if(tag.value) {
            System.out.println("[" + tag.toString() + "] " + message);
        }
    }
}
