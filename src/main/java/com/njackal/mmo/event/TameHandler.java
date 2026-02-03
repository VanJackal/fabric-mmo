package com.njackal.mmo.event;

import com.njackal.mmo.config.MMOConfig;
import com.njackal.mmo.persistence.XPType;
import net.minecraft.world.entity.EntityType;

import java.util.Map;
import java.util.UUID;

public class TameHandler extends PlayerEventHandler{
    private final Map<EntityType<?>, Integer> mobXp;
    private static final int DEFAULT_TAME = 0;
    public TameHandler(MMOConfig config) {
        mobXp = config.tame().mobs();
    }

    public void handleTame(UUID player, EntityType<?> type){
        fireXpEvent(XPType.Taming, mobXp.getOrDefault(type, DEFAULT_TAME), player);
    }
}
