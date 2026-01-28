package com.njackal.mmo.presentation;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.njackal.mmo.FabricMMO;
import com.njackal.mmo.logic.PlayerConfigHandler;
import com.njackal.mmo.logic.XPMath;
import com.njackal.mmo.persistence.MMODatabase;
import com.njackal.mmo.persistence.NotificationMode;
import com.njackal.mmo.persistence.XPType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class CommandHandler {
    private final PlayerConfigHandler playerConfigHandler;
    private final MMODatabase database;

    public CommandHandler(PlayerConfigHandler playerConfigHandler, MMODatabase database) {
        this.playerConfigHandler = playerConfigHandler;
        this.database = database;
    }

    public void initCommands() {
        FabricMMO.LOGGER.info("Command Init");

        CommandRegistrationCallback.EVENT.register((dispatch, context, selection) -> {
            dispatch.register(
                    Commands.literal("mmo").then(
                            Commands.literal("xpbar").then(
                                    Commands.argument("visible", BoolArgumentType.bool())
                                            .executes((ctx) -> {
                                                FabricMMO.LOGGER.trace("player setting xpbar visibility");
                                                boolean visible = ctx.getArgument("visible", Boolean.class);
                                                Player player = ctx.getSource().getPlayer();
                                                if (player == null) { // fail if not executed by the player
                                                    return 0;
                                                }
                                                playerConfigHandler.setXPBarVisibility(player.getUUID(), visible);
                                                return 1;
                                            })
                            )
                    ).then(
                            Commands.literal("notification").then(
                                    Commands.argument("mode", StringArgumentType.string())
                                            .suggests((ctx, builder)->{
                                                for (NotificationMode mode : NotificationMode.values()){
                                                    builder.suggest(mode.value);
                                                }
                                                return builder.buildFuture();
                                            })
                                            .executes(ctx -> {
                                                FabricMMO.LOGGER.trace("player setting notification mode");
                                                Player player = ctx.getSource().getPlayer();
                                                if (player == null) { // fail if not executed by the player
                                                    return 0;
                                                }
                                                NotificationMode mode = NotificationMode.fromValue(ctx.getArgument("mode", String.class));
                                                playerConfigHandler.setNotificationMode(player.getUUID(), mode);
                                                return 1;
                                            })
                            )
                    ).then(
                            Commands.literal("level").then(
                                    Commands.argument("skill", StringArgumentType.string())
                                            .suggests((ctx,builder)->{
                                                String start = "";
                                                try {
                                                    start = ctx.getArgument("skill", String.class).toLowerCase();
                                                } catch (RuntimeException e) {
                                                    // do nothing, (doesn't seem to be a way to precheck this)
                                                }

                                                for (XPType t : XPType.values()){
                                                    if (start.isEmpty() || t.dbId.toLowerCase().startsWith(start)){
                                                        builder.suggest(t.dbId);
                                                    }
                                                }

                                                return builder.buildFuture();
                                            })
                                            .executes(ctx-> {
                                                Player player = ctx.getSource().getPlayer();
                                                if (player == null) { // fail if not executed by the player
                                                    return 0;
                                                }
                                                XPType type = XPType.fromDbId(ctx.getArgument("skill", String.class));
                                                if (type == null) {
                                                    ctx.getSource().sendFailure(Component.literal("Invalid skill"));
                                                    return 0;
                                                }

                                                int level = XPMath.levelFromXp( database.getXp(player.getUUID(), type) );
                                                ctx.getSource().sendSystemMessage(Component.literal("Your %s level is %d".formatted(type.name(), level)));

                                                return 1;
                                            })
                            ).executes(ctx -> {
                                Player player = ctx.getSource().getPlayer();
                                if (player == null) { // fail if not executed by the player
                                    return 0;
                                }

                                ctx.getSource().sendSystemMessage(
                                        Component.literal("Your Levels:")
                                );
                                for(XPType t : XPType.values()){
                                    ctx.getSource().sendSystemMessage(
                                            Component.literal("%s: %d".formatted(t.name(), database.getXp(player.getUUID(), t)))
                                    );
                                }


                                return 1;
                            })
                    )
            );
        });
    }
}
