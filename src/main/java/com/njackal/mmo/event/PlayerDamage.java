package com.njackal.mmo.event;

import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.UUID;

public record PlayerDamage(UUID player, float damageDealt, List<TagKey<Item>> sourceItemTags, LivingEntity target) {
    public static PlayerDamage of(LivingEntity entity, DamageSource source, float amount) {
        assert source.getEntity() instanceof Player;
        Player player = (Player) source.getEntity();
        ItemStack weapon = source.getWeaponItem();
        assert weapon != null;

        return new PlayerDamage(
                player.getUUID(),
                amount,
                weapon.getTags().toList(),
                entity
        );
    }
}
