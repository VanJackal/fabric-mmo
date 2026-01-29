package com.njackal.mmo;

import com.njackal.mmo.config.ConfigManager;
import com.njackal.mmo.event.*;
import com.njackal.mmo.logic.PlayerConfigHandler;
import com.njackal.mmo.logic.XPEventHandler;
import com.njackal.mmo.persistence.MMODatabase;
import com.njackal.mmo.presentation.CommandHandler;
import com.njackal.mmo.presentation.PlayerUIHandler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
	private PlayerConfigHandler playerConfigHandler;

	private MMODatabase database;
	private ConfigManager configManager;

	private PlayerUIHandler playerUIHandler;
	private CommandHandler commandHandler;

	private MinecraftServer minecraftServer;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Initializing Fabric MMO");
		configManager = new ConfigManager();





		database = new MMODatabase(//todo load this from config
				"jdbc:mysql://localhost/fabricmmo",
				"root",
				"123"
		);

		xpEventHandler = new XPEventHandler(database);
		playerConfigHandler = new PlayerConfigHandler(database);

		commandHandler = new CommandHandler(playerConfigHandler, database);


		LOGGER.debug("Initialized");

		ServerTickEvents.START_SERVER_TICK.register(server -> {
			if (minecraftServer == null) {
				minecraftServer = server;
				afterServerInit();
			}
		});

		commandHandler.initCommands();
	}

	private void afterServerInit(){
		LOGGER.info("Server Init");
		try {
			configManager.init("config/FabricMMO.yaml", "config.yaml", minecraftServer.registryAccess());
		} catch (IOException e) {
			LOGGER.error("Failed to load config", e);
		}
		playerUIHandler = new PlayerUIHandler(minecraftServer, playerConfigHandler);

		playerDamageHandler = new PlayerDamageHandler();
		blockBreakHandler = new BlockBreakHandler(configManager.config());
		acrobaticsHandler = new AcrobaticsHandler();
		fishingHandler = new FishingHandler();

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
		Registry<DamageType> damageTypeReg = minecraftServer.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);
		DamageType fallDamage = damageTypeReg.getValue(DamageTypes.FALL.identifier());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(((livingEntity, damageSource, v) -> {
			if ((livingEntity instanceof Player && damageSource.type() == fallDamage)) {
				LOGGER.debug("player {} took {} fall damage", livingEntity.getUUID(), v);
				acrobaticsHandler.handleAcrobatics(v, livingEntity.getUUID());
			}

			return true;
		}));

		//Fishing

		FishingEvents.ITEM_FISHED.register(fishingHandler);

	}

}