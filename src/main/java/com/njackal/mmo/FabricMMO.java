package com.njackal.mmo;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.njackal.mmo.event.*;
import com.njackal.mmo.logic.ConfigHandler;
import com.njackal.mmo.logic.XPEventHandler;
import com.njackal.mmo.logic.XPMath;
import com.njackal.mmo.persistence.MMODatabase;
import com.njackal.mmo.persistence.NotificationMode;
import com.njackal.mmo.persistence.XPType;
import com.njackal.mmo.presentation.PlayerUIHandler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class FabricMMO implements ModInitializer {
	public static final String MOD_ID = "fabric-mmo";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private PlayerDamageHandler playerDamageHandler;
	private BlockBreakHandler blockBreakHandler;
	private AcrobaticsHandler acrobaticsHandler;
	private FishingHandler fishingHandler;

	private XPEventHandler xpEventHandler;
	private ConfigHandler configHandler;

	private MMODatabase database;

	private PlayerUIHandler playerUIHandler;

	private MinecraftServer minecraftServer;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Initializing Fabric MMO");
		playerDamageHandler = new PlayerDamageHandler();
		blockBreakHandler = new BlockBreakHandler();
		acrobaticsHandler = new AcrobaticsHandler();
		fishingHandler = new FishingHandler();



		database = new MMODatabase(//todo load this from config
				"jdbc:mysql://localhost/fabricmmo",
				"root",
				"123"
		);

		xpEventHandler = new XPEventHandler(database);
		configHandler = new ConfigHandler(database);


		LOGGER.debug("Initialized");

		ServerTickEvents.START_SERVER_TICK.register(server -> {
			if (minecraftServer == null) {
				minecraftServer = server;
				afterServerInit();
			}
		});

		commandInit();
	}

	private void afterServerInit(){
		LOGGER.info("Server Init");
		playerUIHandler = new PlayerUIHandler(minecraftServer, configHandler);

		PlayerBlockBreakEvents.BEFORE.register((
				world,
				player,
				pos,
				state,
				entity)->{
			BlockBreakData blockBreakData = BlockBreakData.of(player, state);
			blockBreakHandler.handleBlockBreakEvent(blockBreakData);
			return true;
		});

		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source,amount)->{
			if (source.getEntity() instanceof Player) {
				PlayerDamage damage;
				if (amount > entity.getHealth()){
					damage = PlayerDamage.of(entity, source, entity.getHealth());
				} else {
					damage = PlayerDamage.of(entity, source, amount);
				}
				playerDamageHandler.handleDamageEvent(damage);
			}
			return true; // pass the damage
		});

		playerDamageHandler.observe(xpEventHandler);
		blockBreakHandler.observe(xpEventHandler);
		acrobaticsHandler.observe(xpEventHandler);
		fishingHandler.observe(xpEventHandler);

		xpEventHandler.observeLevelUp(playerUIHandler);
		xpEventHandler.observeXpGain(playerUIHandler);

		//UI Bossbar initializer
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server)->{
			playerUIHandler.initPlayerBar(handler.getPlayer());
		});
		//UI Bossbar destructor
		ServerPlayConnectionEvents.DISCONNECT.register((handler, sender)->{
			playerUIHandler.removePlayerBar(handler.getPlayer().getUUID());
		});

		//initialize new players in the database
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			database.initPlayer(handler.getPlayer().getUUID());
		});


		//Acrobatics
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(((livingEntity, damageSource, v) -> {
			if ((livingEntity instanceof Player && damageSource.type().msgId().equals("fall"))) {
				LOGGER.debug("player {} took {} fall damage", livingEntity.getUUID(), v);
				acrobaticsHandler.handleAcrobatics(v, livingEntity.getUUID());
			}

			return true;
		}));

		//Fishing

		FishingEvents.ITEM_FISHED.register(fishingHandler);

	}

	private void commandInit() {
		LOGGER.info("Command Init");

		CommandRegistrationCallback.EVENT.register((dispatch, context, selection) -> {
			dispatch.register(
					Commands.literal("mmo").then(
							Commands.literal("xpbar").then(
									Commands.argument("visible", BoolArgumentType.bool())
											.executes((ctx) -> {
												LOGGER.trace("player setting xpbar visibility");
												boolean visible = ctx.getArgument("visible", Boolean.class);
												Player player = ctx.getSource().getPlayer();
												if (player == null) { // fail if not executed by the player
													return 0;
												}
												configHandler.setXPBarVisibility(player.getUUID(), visible);
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
												LOGGER.trace("player setting notification mode");
												Player player = ctx.getSource().getPlayer();
												if (player == null) { // fail if not executed by the player
													return 0;
												}
												NotificationMode mode = NotificationMode.fromValue(ctx.getArgument("mode", String.class));
												configHandler.setNotificationMode(player.getUUID(), mode);
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