package com.njackal.mmo.logic;

import com.njackal.mmo.persistence.XPType;

import java.util.UUID;

public interface XPGainEvent {
    /**
     * @param player player gaining xp
     * @param type type of xp gained
     * @param xpCurrent current amount of xp
     * @param xpMax max xp for level
     */
    void xpGained(UUID player, XPType type, int xpCurrent, int xpMax);
}
