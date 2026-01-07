package com.njackal.mmo.event;

import com.njackal.mmo.FabricMMO;
import com.njackal.mmo.persistence.XPType;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public class BlockBreakHandler extends PlayerEventHandler {
    Map<Block, Integer> woodcuttingXpDict;
    Map<Block, Integer> miningXpDict;
    Map<Block, Integer> excavationXpDict;


    public BlockBreakHandler() {
        super();

        woodcuttingXpDict = new HashMap<>();
        miningXpDict = new HashMap<>();
        excavationXpDict = new HashMap<>();

        // woodcutting
        woodcuttingXpDict.put(Blocks.OAK_LOG, 5);
        woodcuttingXpDict.put(Blocks.SPRUCE_LOG, 5);
        woodcuttingXpDict.put(Blocks.BIRCH_LOG, 5);
        woodcuttingXpDict.put(Blocks.JUNGLE_LOG, 5);
        woodcuttingXpDict.put(Blocks.ACACIA_LOG, 5);
        woodcuttingXpDict.put(Blocks.DARK_OAK_LOG, 5);
        woodcuttingXpDict.put(Blocks.MANGROVE_LOG, 5);
        woodcuttingXpDict.put(Blocks.CHERRY_LOG, 5);
        woodcuttingXpDict.put(Blocks.PALE_OAK_LOG, 5);
        woodcuttingXpDict.put(Blocks.CRIMSON_STEM, 5);
        woodcuttingXpDict.put(Blocks.WARPED_STEM, 5);

        // mining
        miningXpDict.put(Blocks.STONE, 1);
        miningXpDict.put(Blocks.ANDESITE, 2);
        miningXpDict.put(Blocks.GRANITE, 2);
        miningXpDict.put(Blocks.DIORITE, 2);
        miningXpDict.put(Blocks.TUFF, 2);
        miningXpDict.put(Blocks.DEEPSLATE, 2);
        miningXpDict.put(Blocks.CALCITE, 3);

        miningXpDict.put(Blocks.NETHERRACK, 1);
        miningXpDict.put(Blocks.BLACKSTONE, 2);
        miningXpDict.put(Blocks.BASALT, 1);

        miningXpDict.put(Blocks.COAL_ORE, 5);
        miningXpDict.put(Blocks.DEEPSLATE_COAL_ORE, 5);
        miningXpDict.put(Blocks.IRON_ORE, 8);
        miningXpDict.put(Blocks.DEEPSLATE_IRON_ORE, 8);
        miningXpDict.put(Blocks.GOLD_ORE, 15);
        miningXpDict.put(Blocks.DEEPSLATE_GOLD_ORE, 15);
        miningXpDict.put(Blocks.REDSTONE_ORE, 8);
        miningXpDict.put(Blocks.DEEPSLATE_REDSTONE_ORE, 8);
        miningXpDict.put(Blocks.EMERALD_ORE, 10);
        miningXpDict.put(Blocks.DEEPSLATE_EMERALD_ORE, 10);
        miningXpDict.put(Blocks.LAPIS_ORE, 12);
        miningXpDict.put(Blocks.DEEPSLATE_LAPIS_ORE, 12);
        miningXpDict.put(Blocks.DIAMOND_ORE, 20);
        miningXpDict.put(Blocks.DEEPSLATE_DIAMOND_ORE, 20);

        miningXpDict.put(Blocks.NETHER_QUARTZ_ORE, 12);
        miningXpDict.put(Blocks.NETHER_GOLD_ORE, 8);
        miningXpDict.put(Blocks.ANCIENT_DEBRIS, 30);

        // excavation
        excavationXpDict.put(Blocks.DIRT, 1);
        excavationXpDict.put(Blocks.GRASS_BLOCK, 2);
        excavationXpDict.put(Blocks.MYCELIUM, 5);
        excavationXpDict.put(Blocks.PODZOL, 2);
        excavationXpDict.put(Blocks.GRAVEL, 2);
        excavationXpDict.put(Blocks.SAND, 2);
        excavationXpDict.put(Blocks.RED_SAND, 4);
        excavationXpDict.put(Blocks.MUD, 3);
        excavationXpDict.put(Blocks.MUDDY_MANGROVE_ROOTS, 3);
        excavationXpDict.put(Blocks.CLAY, 5);
        excavationXpDict.put(Blocks.ROOTED_DIRT, 5);

        excavationXpDict.put(Blocks.SOUL_SAND, 3);
        excavationXpDict.put(Blocks.SOUL_SOIL, 3);







    }

    public void handleBlockBreakEvent(BlockBreakData data) {
        FabricMMO.LOGGER.info(data.toString()); //todo debug

        Block minedBlock = data.blockState().getBlock();

        XPType xpType;
        int xpAmount;

        // get xp type and amount for tool/block mined
        if (data.toolTags().contains(ItemTags.AXES)) {
            xpType = XPType.Woodcutting;
            xpAmount = woodcuttingXpDict.get(minedBlock);
        } else if (data.toolTags().contains(ItemTags.PICKAXES)) {
            xpType = XPType.Mining;
            xpAmount = miningXpDict.get(minedBlock);
        } else if (data.toolTags().contains(ItemTags.SHOVELS)) {
            xpType = XPType.Excavation;
            xpAmount = excavationXpDict.get(minedBlock);
        } else {
            return; //exit early, no xp gain
        }

        if (xpAmount != 0) {// dont fire an xp event if no xp gained
            fireXpEvent(xpType, xpAmount, data.player());
        }

    }
}
