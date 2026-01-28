package com.njackal.mmo.mixin;

import com.njackal.mmo.event.FishingEvents;
import net.minecraft.advancements.criterion.FishingRodHookedTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(FishingRodHookedTrigger.class)
public class FishingMixin {
    @Inject(at = @At("HEAD"), method="trigger")
    private void fishEvent(ServerPlayer serverPlayer,
                           ItemStack itemStack,
                           FishingHook fishingHook,
                           Collection<ItemStack> collection,
                           CallbackInfo ci) {
        FishingEvents.ITEM_FISHED.invoker().item_fished(serverPlayer, collection);
    }
}
