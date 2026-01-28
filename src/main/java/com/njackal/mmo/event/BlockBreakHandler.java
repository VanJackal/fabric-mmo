package com.njackal.mmo.event;

import com.njackal.mmo.FabricMMO;
import com.njackal.mmo.persistence.XPType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class BlockBreakHandler extends PlayerEventHandler {
    private static final int FLOWER_XP = 10;

    Map<Block, Integer> woodcuttingXpDict;
    Map<Block, Integer> miningXpDict;
    Map<Block, Integer> excavationXpDict;

    Map<Block, Integer> herbalismXpDict;


    public BlockBreakHandler() {
        super();

        woodcuttingXpDict = new HashMap<>();
        miningXpDict = new HashMap<>();
        excavationXpDict = new HashMap<>();

        herbalismXpDict = new HashMap<>();

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


        // herbalism
        herbalismXpDict.put(Blocks.SHORT_DRY_GRASS,1);
        herbalismXpDict.put(Blocks.SHORT_GRASS,1);
        herbalismXpDict.put(Blocks.TALL_GRASS,1);
        herbalismXpDict.put(Blocks.TALL_DRY_GRASS,1);
        herbalismXpDict.put(Blocks.SEAGRASS,1);
        herbalismXpDict.put(Blocks.TALL_SEAGRASS,1);
        herbalismXpDict.put(Blocks.BUSH, 1);
        herbalismXpDict.put(Blocks.FERN, 1);
        herbalismXpDict.put(Blocks.LARGE_FERN, 1);

        herbalismXpDict.put(Blocks.VINE, 5);
        herbalismXpDict.put(Blocks.CAVE_VINES, 5);
        herbalismXpDict.put(Blocks.CAVE_VINES_PLANT, 5);
        herbalismXpDict.put(Blocks.PUMPKIN, 10);
        herbalismXpDict.put(Blocks.MELON, 10);

        herbalismXpDict.put(Blocks.COCOA, 20);
        herbalismXpDict.put(Blocks.SUGAR_CANE, 20);
        herbalismXpDict.put(Blocks.CACTUS, 20);

        herbalismXpDict.put(Blocks.WHEAT, 50);
        herbalismXpDict.put(Blocks.POTATOES, 50);
        herbalismXpDict.put(Blocks.CARROTS, 50);
        herbalismXpDict.put(Blocks.BEETROOTS, 50);
        herbalismXpDict.put(Blocks.NETHER_WART, 50);

        herbalismXpDict.put(Blocks.LILY_PAD, 50);
        herbalismXpDict.put(Blocks.BIG_DRIPLEAF, 50);
        herbalismXpDict.put(Blocks.BIG_DRIPLEAF_STEM, 50);
        herbalismXpDict.put(Blocks.SMALL_DRIPLEAF, 100);

        herbalismXpDict.put(Blocks.RED_MUSHROOM, 150);
        herbalismXpDict.put(Blocks.BROWN_MUSHROOM, 150);
        herbalismXpDict.put(Blocks.CRIMSON_FUNGUS, 150);
        herbalismXpDict.put(Blocks.WARPED_FUNGUS, 150);

        herbalismXpDict.put(Blocks.SHROOMLIGHT, 250);





    }

    public void handleBlockBreakEvent(BlockBreakData data) {
        FabricMMO.LOGGER.debug(data.toString());

        Block minedBlock = data.blockState().getBlock();

        if (isMining(minedBlock)) {
            doMining(data, minedBlock);
        } else {
            doHerbalism(data);
        }

    }

    private void doHerbalism(BlockBreakData data) {
        BlockState state = data.blockState();
        int xpAmount;

        if (herbalismXpDict.containsKey(state.getBlock())) {
            // special breaking
            xpAmount = herbalismXpDict.get(state.getBlock());

        } else if(state.getTags().anyMatch(tag
                -> tag == BlockTags.FLOWERS || tag == BlockTags.SMALL_FLOWERS)) {
            // flowers etc
            xpAmount = FLOWER_XP;
        } else {
            return;
        }

        if (xpAmount != 0) {
            fireXpEvent(XPType.Herbalism, xpAmount, data.player());
        }
        // do nothing if no matches
    }

    private boolean isMining(Block block) {
        return woodcuttingXpDict.containsKey(block) ||
                miningXpDict.containsKey(block) ||
                excavationXpDict.containsKey(block);
    }

    private void doMining(BlockBreakData data, Block minedBlock) {
        XPType xpType;
        int xpAmount;
        // get xp type and amount for tool/block mined
        if (data.toolTags().contains(ItemTags.AXES)) {
            xpType = XPType.Woodcutting;
            xpAmount = woodcuttingXpDict.getOrDefault(minedBlock, 0);
        } else if (data.toolTags().contains(ItemTags.PICKAXES)) {
            xpType = XPType.Mining;
            xpAmount = miningXpDict.getOrDefault(minedBlock,0);
        } else if (data.toolTags().contains(ItemTags.SHOVELS)) {
            xpType = XPType.Excavation;
            xpAmount = excavationXpDict.getOrDefault(minedBlock, 0);
        } else {
            return;
        }

        if (xpAmount != 0) {// dont fire an xp event if no xp gained
            fireXpEvent(xpType, xpAmount, data.player());
        }
    }

}
