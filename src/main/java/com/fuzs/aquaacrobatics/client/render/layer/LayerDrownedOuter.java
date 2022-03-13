package com.fuzs.aquaacrobatics.client.render.layer;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import com.fuzs.aquaacrobatics.client.render.RenderDrowned;
import com.fuzs.aquaacrobatics.entity.EntityDrowned;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.util.ResourceLocation;

public class LayerDrownedOuter implements LayerRenderer<EntityDrowned> {
    private static final ResourceLocation DROWNED_OUTER = new ResourceLocation(AquaAcrobatics.MODID, "textures/entity/drowned_outer_layer.png");
    private final RenderDrowned renderer;
    private final ModelZombie layerModel = new ModelZombie(0.25F, false);

    public LayerDrownedOuter(RenderDrowned p_i47183_1_)
    {
        this.renderer = p_i47183_1_;
    }

    public void doRenderLayer(EntityDrowned entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if(!entitylivingbaseIn.isInvisible()) {
            this.layerModel.setModelAttributes(this.renderer.getMainModel());
            this.layerModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.renderer.bindTexture(DROWNED_OUTER);
            this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }

    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}
