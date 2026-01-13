package com.njackal.mmo.presentation;

import com.njackal.mmo.FabricMMO;
import com.njackal.mmo.logic.LevelUpEvent;
import com.njackal.mmo.logic.XPGainEvent;
import com.njackal.mmo.persistence.XPType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class PlayerUIHandler implements LevelUpEvent, XPGainEvent {
    private final MinecraftServer minecraftServer;

    public PlayerUIHandler(MinecraftServer minecraftServer) {
        this.minecraftServer = minecraftServer;
    }

    @Override
    public void levelUp(UUID player, XPType type, int xpGain, int xpTotal, int level) {
        FabricMMO.LOGGER.info("{} level up to {}", player, level);

        ServerPlayer serverPlayer = minecraftServer.getPlayerList().getPlayer(player);
        if (serverPlayer != null) {
            serverPlayer.connection.send(new ClientboundSetSubtitleTextPacket(
                    Component.literal(String.format("§f%s§r leveled up to level §f%d", type.dbId, level)).withStyle(ChatFormatting.GOLD)
            ));
            serverPlayer.connection.send(new ClientboundSetTitleTextPacket(
                    Component.literal("Level Up!").withStyle(ChatFormatting.GOLD)
            ));
        } else {
            FabricMMO.LOGGER.warn("Player: {} not found on show level up.", player);
        }
    }

    @Override
    public void xpGained(UUID player, XPType type, int xpGain, int xpTotal, int level) {
        FabricMMO.LOGGER.info("{} XP GAINED {}", player, xpGain);
    }
}
