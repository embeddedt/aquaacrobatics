package com.fuzs.aquaacrobatics.core.mixin.accessor;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(World.class)
public interface IWorldAccessor {

    @Invoker
    boolean callGetCollisionBoxes(@Nullable Entity entityIn, AxisAlignedBB aabb, boolean hasNoEntity, @Nullable List<AxisAlignedBB> outList);

}
