package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.entity.Pose;
import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(EntityPlayerMP.class)
public abstract class EntityPlayerMPMixin extends EntityPlayer {

    public EntityPlayerMPMixin(World worldIn, GameProfile gameProfileIn) {

        super(worldIn, gameProfileIn);
    }

    @Inject(method = "onDeath", at = @At("TAIL"), require = 0)
    public void onDeath(DamageSource cause, CallbackInfo callbackInfo) {

        // super method is never called where this is set in vanilla
        ((IPlayerResizeable) this).setPose(Pose.DYING);
    }

}
