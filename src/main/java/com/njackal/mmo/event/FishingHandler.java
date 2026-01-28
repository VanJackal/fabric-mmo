package com.njackal.mmo.event;

import com.njackal.mmo.persistence.XPType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FishingHandler extends PlayerEventHandler implements FishingEvents.ItemFished{

    Map<Item, Integer> fishingXpDict;

    public FishingHandler(){
        super();

        fishingXpDict = new HashMap<>();

        //Fish
        fishingXpDict.put(Items.COD, 1);
        fishingXpDict.put(Items.SALMON, 3);
        fishingXpDict.put(Items.PUFFERFISH, 5);
        fishingXpDict.put(Items.TROPICAL_FISH, 7);

        //Treasure
        fishingXpDict.put(Items.BOW, 11);
        fishingXpDict.put(Items.FISHING_ROD, 11);
        fishingXpDict.put(Items.NAME_TAG, 11);
        fishingXpDict.put(Items.NAUTILUS_SHELL, 11);
        fishingXpDict.put(Items.SADDLE, 11);
        fishingXpDict.put(Items.ENCHANTED_BOOK, 11);
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
