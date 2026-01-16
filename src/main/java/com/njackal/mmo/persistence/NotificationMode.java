package com.njackal.mmo.persistence;

public enum NotificationMode {
    Disabled("disabled"),
    Title("title"),
    Actionbar("actionbar"),
    Chat("chat"),
    ;
    public final String value;

    NotificationMode(String value) {
        this.value = value;
    }

    public static NotificationMode fromValue(String value) {
        return switch (value.toLowerCase()) {
            case "title" -> Title;
            case "actionbar" -> Actionbar;
            case "chat" -> Chat;
            default -> Disabled;
        };
    }
}
