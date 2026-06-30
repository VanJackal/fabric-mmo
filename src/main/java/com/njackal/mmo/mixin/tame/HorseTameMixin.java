package com.njackal.mmo.mixin.tame;

import com.njackal.mmo.event.TameEvents;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public class HorseTameMixin {
    @Inject(at=@At(value = "HEAD"), method="tameWithName")
    public void horseTameEvent(Player player, CallbackInfoReturnable<Boolean> cir){
        TameEvents.ANIMAL_TAMED.invoker().animalTamed(player, EntityTypes.HORSE);
    }
}
