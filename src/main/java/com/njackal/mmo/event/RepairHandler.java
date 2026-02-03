package com.njackal.mmo.event;

import com.njackal.mmo.config.MMOConfig;
import com.njackal.mmo.persistence.XPType;

import java.util.UUID;

public class RepairHandler extends PlayerEventHandler{
    int repairMultiplier;
    public RepairHandler(MMOConfig config) {
        repairMultiplier = config.repairMultiplier();
    }

    public void handleRepair(UUID player, int durability) {
        if (durability > 0) {
            fireXpEvent(XPType.Repair, durability * repairMultiplier, player);
        }
    }
}
