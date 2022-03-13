package com.fuzs.aquaacrobatics.core.mixin.content;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityZombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityZombie.class)
public abstract class EntityZombieMixin {
    @Redirect(method = "initEntityAI", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 0))
    private void skipAddingSwimming(EntityAITasks instance, int priority, EntityAIBase task) {
        if(!(task instanceof EntityAISwimming)) {
            instance.addTask(priority, task);
        }
    }
}
