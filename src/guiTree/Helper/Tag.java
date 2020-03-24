package guiTree.Helper;

public enum Tag {
    LISTENER(true),
    PAINTING(false);

    public boolean value;

    Tag(boolean value) {
        this.value = value;
    }
}
