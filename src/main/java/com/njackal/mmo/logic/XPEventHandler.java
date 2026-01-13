package com.njackal.mmo.logic;

import com.njackal.mmo.FabricMMO;
import com.njackal.mmo.event.PlayerXPEvent;
import com.njackal.mmo.persistence.DatabaseException;
import com.njackal.mmo.persistence.MMODatabase;
import com.njackal.mmo.persistence.XPType;

import java.util.*;

public class XPEventHandler implements PlayerXPEvent {

    private static final float XP_DELTA_THRESHOLD = 0.1f;//fraction of a level to show level progress

    private final MMODatabase database;

    private final List<XPGainEvent> xpGainEventHandlers;
    private final List<LevelUpEvent> levelUpEventHandlers;

    private final Map<UUID, Map<XPType, Integer>> xpDeltas;

    public XPEventHandler(MMODatabase database) {
        this.database = database;
        xpGainEventHandlers = new LinkedList<>();
        levelUpEventHandlers = new LinkedList<>();
        xpDeltas = new HashMap<>();
    }

    @Override
    public void gainXP(XPType type, int xp, UUID player) {
        FabricMMO.LOGGER.info("{} earned {} {} xp", player, xp, type);//todo debug
        try {
            int totalXp = database.addXp(player,type, xp);
            int level = XPMath.levelFromXp(totalXp);

            incrementXPDelta(player, type, xp);
            int delta = getXPDelta(player, type);

            if (didLevelUp(xp, totalXp, level)) {
                fireLevelUpEvent(player, type, xp, totalXp, level);
            } else if (canShowXpGain(delta, level)){
                fireXpGainEvent(player, type, totalXp, XPMath.xpFromLevel(level + 1) - XPMath.levelFromXp(totalXp));
                resetXPDelta(player, type);// reset delta since we've shown xp
            }
        } catch ( DatabaseException e ) {
            return;// fail gracefully if the database fails
        }
    }

    private boolean canShowXpGain(int delta, int level) {
        int levelDeltaXp = XPMath.xpFromLevel(level + 1) - XPMath.xpFromLevel(level);//total xp to go from level x to x+1
        float xpThreshold = XP_DELTA_THRESHOLD * (float)levelDeltaXp; // threshold amount
        return xpThreshold <= delta; // is change in xp greater than threshold change
    }

    private boolean didLevelUp(int xpGain, int totalXp, int level) {
        int reqXp = XPMath.xpFromLevel(level);
        return totalXp - xpGain < reqXp;
    }


    public void observeXpGain(XPGainEvent xpGainEvent) {
        xpGainEventHandlers.add(xpGainEvent);
    }

    public void observeLevelUp(LevelUpEvent levelUpEvent) {
        levelUpEventHandlers.add(levelUpEvent);
    }

    private void fireXpGainEvent(UUID player, XPType type, int xpTotal, int xpMax) {
        for  (XPGainEvent xpGainEvent : xpGainEventHandlers) {
            xpGainEvent.xpGained(player, type, xpTotal, xpMax);
        }

    }
    private void fireLevelUpEvent(UUID player, XPType type, int xpGain, int xpTotal, int level) {
        for (LevelUpEvent levelUpEvent : levelUpEventHandlers) {
            levelUpEvent.levelUp(player, type, xpGain, xpTotal, level);
        }
    }

    private int getXPDelta(UUID player, XPType type) {
        tryInitXPDelta(player);
        return xpDeltas.get(player).getOrDefault(type,0);
    }

    private void resetXPDelta(UUID player, XPType type) {
        tryInitXPDelta(player);
        xpDeltas.get(player).put(type,0);
    }

    private void incrementXPDelta(UUID player, XPType type, int delta) {
        tryInitXPDelta(player);
        Map<XPType, Integer> xpDelta = xpDeltas.get(player);
        xpDelta.put(type,xpDelta.getOrDefault(type,0) + delta);
    }

    private void tryInitXPDelta(UUID player) {
        if (!xpDeltas.containsKey(player)) {
            xpDeltas.put(player, new HashMap<>());
        }
    }
}
