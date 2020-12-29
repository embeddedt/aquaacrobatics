package com.fuzs.aquaacrobatics.core.mixin;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unused")
@Mixin(GuiIngameForge.class)
public abstract class GuiIngameForgeMixin extends GuiIngame {

    public GuiIngameForgeMixin(Minecraft mcIn) {

        super(mcIn);
    }

    @Redirect(method = "renderAir", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;isInsideOfMaterial(Lnet/minecraft/block/material/Material;)Z"))
    public boolean isInsideOfMaterial(EntityPlayer playerIn, Material materialIn) {

        return playerIn.isInsideOfMaterial(materialIn) || playerIn.getAir() < 300;
    }

}
