package com.njackal.mmo.mixin;

import com.njackal.mmo.event.TameEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TamableAnimal.class)
public abstract class TameMixin extends Animal implements OwnableEntity {
    protected TameMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At(value = "HEAD"), method = "tame")
    public void tameEvent(Player player, CallbackInfo ci){
        TameEvents.ANIMAL_TAMED.invoker().animalTamed(player, this.getType());
    }
}
