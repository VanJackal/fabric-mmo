package com.njackal.mmo.event;

import com.njackal.mmo.config.BlockBreakConfig;
import com.njackal.mmo.persistence.XPType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BlockBreakToolSkill extends BlockBreakSkill{
    private final TagKey<Item> tool;

    public BlockBreakToolSkill(XPType type, BlockBreakConfig config, TagKey<Item> tool) {
        super(type, config);
        this.tool = tool;
    }

    @Override
    public void handleBlockBreak(BlockBreakData data) {
        if (data.toolTags().contains(tool)) {
            super.handleBlockBreak(data);
        }
    }
}
