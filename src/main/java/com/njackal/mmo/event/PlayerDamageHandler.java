package com.njackal.mmo.event;

import com.njackal.mmo.FabricMMO;
import com.njackal.mmo.persistence.XPType;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.LinkedList;
import java.util.List;

public class PlayerDamageHandler {
    private final List<PlayerXPEvent> observers;
    public PlayerDamageHandler() {
        observers = new LinkedList<>();
    }

    public void handleDamageEvent(PlayerDamage damage) {
        FabricMMO.LOGGER.info(damage.toString()); // todo change to debug
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


        for (PlayerXPEvent observer : observers) {
            observer.gainXP(xp, (int) damage.damageDealt(), damage.player());
        }
    }

    public void observe(PlayerXPEvent xpEventHandler) {
        observers.add(xpEventHandler);
    }

}
