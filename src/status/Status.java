package status;

public enum Status {
    NEW("new"), IN_PROGRESS("inProgress"), DONE("done");
    private String translation;

    Status() {
    }

    Status(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }

    public String toString() {
        return translation;
    }
}
