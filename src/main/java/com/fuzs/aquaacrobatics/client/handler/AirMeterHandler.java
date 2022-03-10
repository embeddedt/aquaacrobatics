package com.fuzs.aquaacrobatics.client.handler;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AirMeterHandler {

    private final Minecraft mc = Minecraft.getMinecraft();

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Pre evt) {

        if (evt.getType() != RenderGameOverlayEvent.ElementType.AIR) {

            return;
        }

        EntityPlayer playerIn = (EntityPlayer) this.mc.getRenderViewEntity();
        assert playerIn != null;
        if (!playerIn.isInsideOfMaterial(Material.WATER) && playerIn.getAir() < 300) {

            this.mc.profiler.startSection("air");
            GlStateManager.enableBlend();
            int left = evt.getResolution().getScaledWidth() / 2 + 91;
            int top = evt.getResolution().getScaledHeight() - GuiIngameForge.right_height;
            int air = playerIn.getAir();
            int full = MathHelper.ceil((double)(air - 2) * 10.0D / 300.0D);
            int partial = MathHelper.ceil((double)air * 10.0D / 300.0D) - full;
            for (int i = 0; i < full + partial; ++i) {

                this.mc.ingameGUI.drawTexturedModalRect(left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
            }

            GuiIngameForge.right_height += 10;
            GlStateManager.disableBlend();
            this.mc.profiler.endSection();
        }
    }

}
