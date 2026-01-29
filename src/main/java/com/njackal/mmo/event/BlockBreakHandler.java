package com.njackal.mmo.event;

import com.njackal.mmo.FabricMMO;
import com.njackal.mmo.config.MMOConfig;
import com.njackal.mmo.persistence.XPType;
import net.minecraft.tags.ItemTags;

import java.util.LinkedList;
import java.util.List;

public class BlockBreakHandler extends PlayerEventHandler {

    List<BlockBreakSkill> skills;
    public BlockBreakHandler(MMOConfig config) {
        super();
        skills = new LinkedList<>();

        skills.add(new BlockBreakSkill(XPType.Herbalism, config.herbalism()));
        skills.add(new BlockBreakToolSkill(XPType.Woodcutting, config.woodcutting(), ItemTags.AXES));
        skills.add(new BlockBreakToolSkill(XPType.Mining, config.mining(), ItemTags.PICKAXES));
        skills.add(new BlockBreakToolSkill(XPType.Excavation, config.excavation(), ItemTags.SHOVELS));
    }

    public void handleBlockBreakEvent(BlockBreakData data) {
        FabricMMO.LOGGER.debug(data.toString());

        for(BlockBreakSkill skill : skills){
            skill.handleBlockBreak(data);
        }
    }

    @Override
    public void observe(PlayerXPEvent xpEventHandler) {
        for(BlockBreakSkill skill : skills){
            skill.observe(xpEventHandler);
        }
    }

}
