package com.njackal.mmo.config;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public record TameConfig(Map<EntityType<?>, Integer> mobs) {
    public static TameConfig from(Map<String,Integer> map, Registry<EntityType<?>> entities){
        Map<EntityType<?>, Integer> mobXpMap = new HashMap<>();
        if(map!= null) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                mobXpMap.put(
                        entities.getValue(Identifier.parse(entry.getKey())),
                        entry.getValue()
                );
            }
        }

        return new TameConfig(mobXpMap);
    }
}
