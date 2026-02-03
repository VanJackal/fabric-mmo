package com.njackal.mmo.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public class TameEvents {
    private TameEvents(){}

    public static final Event<AnimalTamed> ANIMAL_TAMED = EventFactory.createArrayBacked(AnimalTamed.class,
            (listeners) -> (player, animal) -> {
                for (AnimalTamed animalTamed : listeners) {
                    animalTamed.animalTamed(player, animal);
                }
            }
            );

    public interface AnimalTamed {
        /**
         * called when a player tames an animal
         * @param player player that tamed the animal
         * @param animal animal that was tamed
         */
        void animalTamed(Player player, EntityType<?> animal);

    }
}
