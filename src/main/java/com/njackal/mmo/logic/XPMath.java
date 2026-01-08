package com.njackal.mmo.logic;

public class XPMath {
    private static final int XP_MULTIPLIER = 100;

    public static int levelFromXp(int xp) {
        return (int) Math.floor(Math.sqrt((double)xp/XP_MULTIPLIER));
    }
}
