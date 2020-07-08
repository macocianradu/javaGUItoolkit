package guiTree.Helper;

public class Debugger {
    public enum Tag {
        LISTENER(false),
        PAINTING(false),
        FPS(false),
        ANIMATIONS(false),
        PARSING(false);

        public boolean value;

        private void setValue(Boolean value) {
            this.value = value;
        }

        Tag(boolean value) {
            this.value = value;
        }
    }

    private static Timer timer = new Timer();

    public static void log(String message, Tag tag) {
        if(tag.value) {
            System.out.println("[" + tag.toString() + "] " + message);
        }
    }

    public void enableTag(Tag tag, Boolean value) {
        tag.setValue(value);
    }
}
