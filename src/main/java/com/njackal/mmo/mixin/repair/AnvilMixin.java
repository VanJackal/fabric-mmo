package com.njackal.mmo.mixin.repair;

import com.njackal.mmo.event.RepairEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMixin extends ItemCombinerMenu {

    public AnvilMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, ItemCombinerMenuSlotDefinition itemCombinerMenuSlotDefinition) {
        super(menuType, i, inventory, containerLevelAccess, itemCombinerMenuSlotDefinition);
    }

    @Inject(at = @At("HEAD"), method="onTake")
    private void anvilRepairEvent(Player player,
                                  ItemStack itemStack,
                                  CallbackInfo ci){
        ItemStack pre = this.inputSlots.getItem(AnvilMenu.INPUT_SLOT);
        ItemStack post = this.inputSlots.getItem(AnvilMenu.RESULT_SLOT);
        //damage value
        int durability = pre.getDamageValue() - post.getDamageValue();
        RepairEvents.ITEM_REPAIRED.invoker().itemRepaired(player, itemStack, durability);
    }
}
