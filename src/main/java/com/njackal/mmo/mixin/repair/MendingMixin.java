package com.njackal.mmo.mixin.repair;

import com.njackal.mmo.event.RepairEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(ExperienceOrb.class)
public class MendingMixin {
    @Inject(at = @At(value = "INVOKE", target="Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V", shift = At.Shift.AFTER), method="repairPlayerItems", locals = LocalCapture.CAPTURE_FAILHARD)
    private void mendingEvent(ServerPlayer serverPlayer,
                              int i,
                              CallbackInfoReturnable<Integer> cir,
                              Optional optional,
                              ItemStack itemStack,
                              int j,
                              int k
    ){
        RepairEvents.ITEM_REPAIRED.invoker().itemRepaired(serverPlayer, itemStack, k);
    }
}
