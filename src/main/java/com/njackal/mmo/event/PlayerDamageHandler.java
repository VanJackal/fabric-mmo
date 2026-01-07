package com.njackal.mmo.event;

import com.njackal.mmo.FabricMMO;

public class PlayerDamageHandler {
    public PlayerDamageHandler() {}

    public void handleDamageEvent(PlayerDamage damage) {
        FabricMMO.LOGGER.info(damage.toString()); // todo change to debug
    }
}
