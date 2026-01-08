package com.njackal.mmo;

import com.njackal.mmo.event.BlockBreakData;
import com.njackal.mmo.event.BlockBreakHandler;
import com.njackal.mmo.event.PlayerDamage;
import com.njackal.mmo.event.PlayerDamageHandler;
import com.njackal.mmo.logic.XPEventHandler;
import com.njackal.mmo.persistence.MMODatabase;
import com.njackal.mmo.presentation.PlayerUIHandler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabricMMO implements ModInitializer {
	public static final String MOD_ID = "fabric-mmo";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private PlayerDamageHandler playerDamageHandler;
	private BlockBreakHandler blockBreakHandler;

	private XPEventHandler xpEventHandler;

	private MMODatabase database;

	private PlayerUIHandler playerUIHandler;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Initializing Fabric MMO");
		playerDamageHandler = new PlayerDamageHandler();
		blockBreakHandler = new BlockBreakHandler();


		database = new MMODatabase(//todo load this from config
				"jdbc:mysql://localhost/fabricmmo",
				"root",
				"123"
		);

		xpEventHandler = new XPEventHandler(database);

		playerUIHandler = new PlayerUIHandler();

		LOGGER.debug("Initialized");//todo get log levels working


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
				PlayerDamage damage = PlayerDamage.of(entity, source, amount);
				playerDamageHandler.handleDamageEvent(damage);
			}
			return true; // pass the damage
		});

		playerDamageHandler.observe(xpEventHandler);
		blockBreakHandler.observe(xpEventHandler);

		xpEventHandler.observeLevelUp(playerUIHandler);
		xpEventHandler.observeXpGain(playerUIHandler);
	}
}