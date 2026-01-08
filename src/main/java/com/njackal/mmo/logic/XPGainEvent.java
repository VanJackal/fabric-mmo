package com.njackal.mmo.logic;

import com.njackal.mmo.persistence.XPType;

import java.util.UUID;

public interface XPGainEvent {
    /**
     * @param player player gaining xp
     * @param type type of xp gained
     * @param xpGain amount of xp gained
     * @param xpTotal total xp overall after gain
     * @param level current player level
     */
    void xpGained(UUID player, XPType type, int xpGain, int xpTotal, int level);
}
