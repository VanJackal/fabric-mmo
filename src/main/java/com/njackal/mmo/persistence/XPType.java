package com.njackal.mmo.persistence;

public enum XPType {
    Swords("Swords"),
    Archery("Archery"),
    Crossbows("Crossbows"),
    Tridents("Tridents"),
    Spears("Spears"),
    Maces("Maces"),
    Axes("Axes"),
    Unarmed("Unarmed"),
    TNT("TNT"),
    Mining("Mining"),
    Excavation("Excavation"),
    Woodcutting("Woodcutting"),
    Acrobatics("Acrobatics"),
    Alchemy("Alchemy"),
    Fishing("Fishing"),
    Herbalism("Herbalism"),
    Repair("Repair"),
    Taming("Taming"),;

    public final String dbId;

    XPType(String dbId) {
        this.dbId = dbId;
    }

    public static XPType fromDbId(String dbId) {
        for (XPType xpType : XPType.values()) {
            if (xpType.dbId.equalsIgnoreCase(dbId)) {
                return xpType;
            }
        }
        return null;
    }
}
