package com.njackal.mmo.event;

import com.njackal.mmo.config.BlockBreakConfig;
import com.njackal.mmo.persistence.XPType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.stream.Collectors;

public class BlockBreakSkill extends PlayerEventHandler {
    private final XPType type;
    private final Map<Block,Integer> blockXp;
    private final Map<TagKey<Block>, Integer> tagXp;

    public BlockBreakSkill(XPType type, BlockBreakConfig config) {
        this.type = type;
        blockXp = config.blocks();
        tagXp = config.tags();
    }

    public void handleBlockBreak(BlockBreakData data){
        int xp;

        if (blockXp.containsKey(data.blockState().getBlock())){
            xp = blockXp.get(data.blockState().getBlock());
        } else {
            xp = (int) data.blockState().getTags()
                    .filter(tagXp::containsKey)
                    .collect(
                            Collectors.summarizingInt(tagXp::get)
                    ).getSum();
        }

        if(xp!=0){
            fireXpEvent(type, xp, data.player());
        }
    }
}
