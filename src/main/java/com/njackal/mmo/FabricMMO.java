package com.njackal.mmo;

import com.njackal.mmo.event.PlayerDamage;
import com.njackal.mmo.event.PlayerDamageHandler;
import com.njackal.mmo.logic.XPEventHandler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabricMMO implements ModInitializer {
	public static final String MOD_ID = "fabric-mmo";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private PlayerDamageHandler playerDamageHandler;

	private XPEventHandler xpEventHandler;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Initializing Fabric MMO");
		playerDamageHandler = new PlayerDamageHandler();
		xpEventHandler = new XPEventHandler();

		LOGGER.debug("Initialized");//todo get log levels working


		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source,amount)->{
			if (source.getEntity() instanceof Player) {
				PlayerDamage damage = PlayerDamage.of(entity, source, amount);
				playerDamageHandler.handleDamageEvent(damage);
			}
			return true; // pass the damage
		});

		playerDamageHandler.observe(xpEventHandler);
	}
}