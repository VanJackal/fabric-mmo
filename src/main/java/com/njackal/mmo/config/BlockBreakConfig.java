package com.njackal.mmo.config;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public record BlockBreakConfig(
        Map<Block,Integer> blocks,
        Map<TagKey<Block>,Integer> tags
) {
    public static BlockBreakConfig from(Map<String,Map<String,Integer>> map, Registry<Block> blocks) {
        Map<Block,Integer> blockXpMap = new HashMap<>();
        Map<TagKey<Block>,Integer> tagXpMap = new HashMap<>();
        // parse blocks
        if(map.get("blocks") != null) {
            for (Map.Entry<String,Integer> block : map.get("blocks").entrySet()) {
                blockXpMap.put(
                        blocks.getValue(Identifier.parse(block.getKey())),
                        block.getValue()
                );
            }
        }
        //parse Tags

        if (map.get("tags") != null) {
            for (Map.Entry<String, Integer> tag : map.get("tags").entrySet()) {
                tagXpMap.put(
                        TagKey.create(Registries.BLOCK, Identifier.parse(tag.getKey())),
                        tag.getValue()
                );
            }
        }

        return new BlockBreakConfig(blockXpMap, tagXpMap);
    }
}
