package com.njackal.mmo.presentation;

import com.njackal.mmo.FabricMMO;
import com.njackal.mmo.logic.LevelUpEvent;
import com.njackal.mmo.logic.XPGainEvent;
import com.njackal.mmo.persistence.XPType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PlayerUIHandler implements LevelUpEvent, XPGainEvent {
    private static final int BOSSBAR_VANISH_TIME = 2; //time to vanish in seconds

    private final MinecraftServer minecraftServer;

    private final Map<UUID, CustomBossEvent> customBossEvents;
    private final ScheduledExecutorService scheduler;
    private final Map<UUID, ScheduledFuture<?>> vanishTimeouts;

    public PlayerUIHandler(MinecraftServer minecraftServer) {
        this.minecraftServer = minecraftServer;
        customBossEvents = new HashMap<>();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        vanishTimeouts = new HashMap<>();
    }

    @Override
    public void levelUp(UUID player, XPType type, int xpGain, int xpTotal, int level) {
        FabricMMO.LOGGER.debug("{} level up to {}", player, level);

        ServerPlayer serverPlayer = minecraftServer.getPlayerList().getPlayer(player);
        if (serverPlayer != null) {
            serverPlayer.connection.send(new ClientboundSetSubtitleTextPacket(
                    Component.literal(String.format("§f%s§r leveled up to level §f%d", type.dbId, level)).withStyle(ChatFormatting.GOLD)
            ));
            serverPlayer.connection.send(new ClientboundSetTitleTextPacket(
                    Component.literal("Level Up!").withStyle(ChatFormatting.GOLD)
            ));

            xpGained(player, type, 1,1);// show a full xp bar on level up
        } else {
            FabricMMO.LOGGER.warn("Player: {} not found on show level up.", player);
        }
    }

    @Override
    public void xpGained(UUID player, XPType type, int xpCurrent, int xpMax) {

        ServerPlayer serverPlayer = minecraftServer.getPlayerList().getPlayer(player);
        if (serverPlayer != null) {
            CustomBossEvent bossEvent = getBossEvent(player, serverPlayer);
            bossEvent.setName(Component.literal(type.dbId));
            bossEvent.setValue(xpCurrent);//todo move math to logic
            bossEvent.setMax(xpMax);
            bossEvent.setVisible(true);

            scheduleBarVanish(player, bossEvent);

        } else {
            FabricMMO.LOGGER.warn("Player: {} not found on show xp gain", player);
        }

    }

    private CustomBossEvent getBossEvent(UUID player, ServerPlayer serverPlayer) {
        if (!customBossEvents.containsKey(player)) {
            initPlayerBar(player, serverPlayer);
        }
        return customBossEvents.get(player);
    }

    /**
     * initialize bossbar for a player
     * @param serverPlayer player to init
     */
    public void initPlayerBar(ServerPlayer serverPlayer) {
        initPlayerBar(serverPlayer.getUUID(), serverPlayer);
    }

    /**
     * remove a players bossbar handler
     * @param player player to remove bossbar for
     */
    public void removePlayerBar(UUID player) {
        customBossEvents.remove(player);
    }

    private void initPlayerBar(UUID player, ServerPlayer serverPlayer) {
        CustomBossEvent event = new CustomBossEvent(Identifier.fromNamespaceAndPath(FabricMMO.MOD_ID, "bossbar"), Component.literal("YOU SHOULDN'T SEE THIS"));
        event.setColor(BossEvent.BossBarColor.GREEN);
        event.setOverlay(BossEvent.BossBarOverlay.NOTCHED_10);
        event.addPlayer(serverPlayer);
        event.setVisible(false);
        customBossEvents.put(player, event);
    }

    private void scheduleBarVanish(UUID player, CustomBossEvent bossEvent) {
        ScheduledFuture<?> future = scheduler.schedule(() -> bossEvent.setVisible(false),BOSSBAR_VANISH_TIME,TimeUnit.SECONDS);
        if (vanishTimeouts.containsKey(player)) {
            vanishTimeouts.get(player).cancel(false);
            vanishTimeouts.remove(player);
        }
        vanishTimeouts.put(player, future);
    }
}
