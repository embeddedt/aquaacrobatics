package com.fuzs.aquaacrobatics.client.render;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import com.fuzs.aquaacrobatics.client.render.layer.LayerDrownedOuter;
import com.fuzs.aquaacrobatics.entity.EntityDrowned;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerStrayClothing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nonnull;

public class RenderDrowned extends RenderLiving<EntityDrowned> {

    private ResourceLocation mobTexture = new ResourceLocation(AquaAcrobatics.MODID, "textures/entity/drowned.png");

    public static final Factory FACTORY = new Factory();

    public RenderDrowned(RenderManager rendermanagerIn) {
        // We use the vanilla zombie model here and we simply
        // retexture it. Of course you can make your own model
        super(rendermanagerIn, new ModelZombie(), 0.5F);
        this.addLayer(new LayerDrownedOuter(this));
    }

    @Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull EntityDrowned entity) {
        return mobTexture;
    }

    public static class Factory implements IRenderFactory<EntityDrowned> {

        @Override
        public Render<? super EntityDrowned> createRenderFor(RenderManager manager) {
            return new RenderDrowned(manager);
        }

    }

}