package net.iotsite.wekit.api.entity;

public enum MessageType {

    TEXT("text"),
    VOICE("voice");

    private String type;

    public String getType() {
        return type;
    }

    MessageType(String type) {
        this.type = type;
    }
}
