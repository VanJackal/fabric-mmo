package com.njackal.mmo.presentation;

import com.njackal.mmo.FabricMMO;
import com.njackal.mmo.logic.LevelUpEvent;
import com.njackal.mmo.logic.XPGainEvent;
import com.njackal.mmo.persistence.XPType;
import net.minecraft.server.MinecraftServer;

import java.util.UUID;

public class PlayerUIHandler implements LevelUpEvent, XPGainEvent {
    private final MinecraftServer minecraftServer;

    public PlayerUIHandler(MinecraftServer minecraftServer) {
        this.minecraftServer = minecraftServer;
    }

    @Override
    public void levelUp(UUID player, XPType type, int xpGain, int xpTotal, int level) {
        FabricMMO.LOGGER.info("{} level up to {}", player, level);
    }

    @Override
    public void xpGained(UUID player, XPType type, int xpGain, int xpTotal, int level) {
        FabricMMO.LOGGER.info("{} XP GAINED {}", player, xpGain);
    }
}
