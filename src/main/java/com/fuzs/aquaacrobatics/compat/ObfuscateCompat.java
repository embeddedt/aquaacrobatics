package com.fuzs.aquaacrobatics.compat;

import com.fuzs.aquaacrobatics.entity.player.IPlayerSPSwimming;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ObfuscateCompat implements IModCompat {

    @Override
    public void apply(Object... data) {

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onRenderItemHeld(final RenderItemEvent.Held.Pre evt) {

        EntityLivingBase entityIn = evt.getEntity();
        if (entityIn.isSneaking() && entityIn instanceof IPlayerSPSwimming && !((IPlayerSPSwimming) entityIn).isCrouching()) {

            GlStateManager.translate(0.0F, 0.0F, 0.2F);
        }
    }

}
