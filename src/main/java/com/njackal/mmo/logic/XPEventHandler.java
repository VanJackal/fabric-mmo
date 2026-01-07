package com.njackal.mmo.logic;

import com.njackal.mmo.FabricMMO;
import com.njackal.mmo.event.PlayerXPEvent;
import com.njackal.mmo.persistence.XPType;

import java.util.UUID;

public class XPEventHandler implements PlayerXPEvent {

    @Override
    public void gainXP(XPType type, int xp, UUID player) {
        FabricMMO.LOGGER.info("{} earned {} {} xp", player, xp, type);//todo debug
    }
}
