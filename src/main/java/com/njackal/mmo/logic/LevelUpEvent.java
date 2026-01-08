package com.njackal.mmo.logic;

import com.njackal.mmo.persistence.XPType;

import java.util.UUID;

public interface LevelUpEvent {
    /**
     * @param player player leveling up
     * @param type type of level gained
     * @param xpGain xp gained for this to happen
     * @param xpTotal total xp for the skill
     * @param level level after level up
     */
    void levelUp(UUID player, XPType type, int xpGain, int xpTotal, int level);
}
