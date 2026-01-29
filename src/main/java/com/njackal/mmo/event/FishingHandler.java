package com.njackal.mmo.event;

import com.njackal.mmo.config.MMOConfig;
import com.njackal.mmo.persistence.XPType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Map;

public class FishingHandler extends PlayerEventHandler implements FishingEvents.ItemFished{

    Map<Item, Integer> fishingXpDict;

    public FishingHandler(MMOConfig config) {
        super();
        fishingXpDict = config.fishing().fishables();
    }

    @Override
    public void item_fished(ServerPlayer player, Collection<ItemStack> items) {
        int totalXp = 0;
        for (ItemStack stack : items) {
            totalXp += fishingXpDict.getOrDefault(stack.getItem(), 0);
        }
        if (totalXp != 0) {
            fireXpEvent(XPType.Fishing, totalXp, player.getUUID());
        }
    }
}
