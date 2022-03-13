package com.fuzs.aquaacrobatics.core.mixin.accessor;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Invoker("getFlag")
    boolean invokeGetFlag(int flag);

    @Invoker("setFlag")
    void invokeSetFlag(int flag, boolean set);
}
