package com.njackal.mmo.event;

import com.njackal.mmo.FabricMMO;
import com.njackal.mmo.config.MMOConfig;
import com.njackal.mmo.persistence.XPType;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Map;

public class PlayerDamageHandler extends PlayerEventHandler {
    private final Map<String, Integer> combatXp;
    public PlayerDamageHandler(MMOConfig config) {
        super();
        combatXp = config.combat().weaponTypes();
    }

    public void handleDamageEvent(PlayerDamage damage) {
        FabricMMO.LOGGER.debug(damage.toString());
        XPType xp;

        List<TagKey<Item>> tags = damage.sourceItemTags();
        if (tags.contains(ItemTags.SWORDS)) {
            xp = XPType.Swords;
        } else if (tags.contains(ItemTags.AXES)) {
            xp = XPType.Axes;
        } else if (tags.contains(ItemTags.SPEARS)) {
            xp = XPType.Spears;
        }else if (tags.contains(ItemTags.BOW_ENCHANTABLE)) {
            xp = XPType.Archery;
        } else if (tags.contains(ItemTags.CROSSBOW_ENCHANTABLE)) {
            xp = XPType.Crossbows;
        } else if (tags.contains(ItemTags.TRIDENT_ENCHANTABLE)) {
            xp = XPType.Tridents;
        } else if (tags.contains(ItemTags.MACE_ENCHANTABLE)){
            xp = XPType.Maces;
        } else {
            xp = XPType.Unarmed;
        }

        fireXpEvent(xp, (int) (damage.damageDealt() * combatXp.getOrDefault(xp.dbId.toLowerCase(),0)), damage.player());
    }

}
