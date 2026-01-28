package com.njackal.mmo.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class FishingEvents {
    private  FishingEvents() {}

    public static final Event<ItemFished> ITEM_FISHED = EventFactory.createArrayBacked(ItemFished.class,
            (listeners)-> (player, items) -> {
                for (ItemFished event : listeners) {
                    event.item_fished(player, items);
                }
            });

    public interface ItemFished {
        /**
         * Called after a player fishes item/items
         *
         * @param player player that fished the items
         * @param items collection of items that were fished
         */
        void item_fished(ServerPlayer player, Collection<ItemStack> items);
    }
}
