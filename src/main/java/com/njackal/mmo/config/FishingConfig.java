package com.njackal.mmo.config;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public record FishingConfig(
    Map<Item, Integer> fishables
) {
    public static FishingConfig from(Map<String,Map<String, Integer>> fishables, Registry<Item> items) {
        Map<Item,Integer> fishablesMap = new HashMap<>();
        if (fishables.get("items") != null) {
            for(Map.Entry<String, Integer> entry : fishables.get("items").entrySet()) {
                fishablesMap.put(
                        items.getValue(Identifier.parse(entry.getKey())),
                        entry.getValue()
                );
            }
        }
        return new FishingConfig(fishablesMap);
    }
}
