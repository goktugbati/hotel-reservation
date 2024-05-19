package model.room;

public enum RoomType {
    SINGLE("1"),
    DOUBLE("2");

    public final String label;

    RoomType(String label) {
        this.label = label;
    }
}