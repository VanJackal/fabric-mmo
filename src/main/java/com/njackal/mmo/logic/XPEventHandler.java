package com.njackal.mmo.logic;

import com.njackal.mmo.FabricMMO;
import com.njackal.mmo.event.PlayerXPEvent;
import com.njackal.mmo.persistence.MMODatabase;
import com.njackal.mmo.persistence.XPType;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class XPEventHandler implements PlayerXPEvent {

    private final MMODatabase database;

    private final List<XPGainEvent> xpGainEventHandlers;
    private final List<LevelUpEvent> levelUpEventHandlers;

    public XPEventHandler(MMODatabase database) {
        this.database = database;
        xpGainEventHandlers = new LinkedList<>();
        levelUpEventHandlers = new LinkedList<>();
    }

    @Override
    public void gainXP(XPType type, int xp, UUID player) {
        FabricMMO.LOGGER.info("{} earned {} {} xp", player, xp, type);//todo debug
        database.addXp(player,type, xp);

        fireLevelUpEvent(player, type, xp, xp *10, xp%5);
        fireXpGainEvent(player, type, xp, xp *10, xp%5);
    }


    public void observeXpGain(XPGainEvent xpGainEvent) {
        xpGainEventHandlers.add(xpGainEvent);
    }

    public void observeLevelUp(LevelUpEvent levelUpEvent) {
        levelUpEventHandlers.add(levelUpEvent);
    }

    private void fireXpGainEvent(UUID player, XPType type, int xpGain, int xpTotal, int level) {
        for  (XPGainEvent xpGainEvent : xpGainEventHandlers) {
            xpGainEvent.xpGained(player, type, xpGain, xpTotal, level);
        }

    }
    private void fireLevelUpEvent(UUID player, XPType type, int xpGain, int xpTotal, int level) {
        for (LevelUpEvent levelUpEvent : levelUpEventHandlers) {
            levelUpEvent.levelUp(player, type, xpGain, xpTotal, level);
        }
    }
}
