package com.njackal.mmo.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class RepairEvents {
    private RepairEvents() {}

    public static final Event<ItemRepaired> ITEM_REPAIRED = EventFactory.createArrayBacked(ItemRepaired.class,
            (listeners) -> (player, itemStack, durability) -> {
                for (ItemRepaired event : listeners) {
                    event.itemRepaired(player, itemStack, durability);
                }
            });

    public interface ItemRepaired {
        /**
         * called when a player repairs an item
         *
         * @param player player that repaired their item
         * @param itemStack item stack that was repaired
         * @param durability durability that was repaired
         */
        void itemRepaired(Player player, ItemStack itemStack, int durability);
    }
}
