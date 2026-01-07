package com.njackal.mmo.event;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.UUID;

public record BlockBreakData(UUID player, BlockState blockState, List<TagKey<Item>> toolTags) {
    public static BlockBreakData of(Player player, BlockState blockState) {
        return new BlockBreakData(player.getUUID(), blockState, player.getMainHandItem().getTags().toList());
    }
}
