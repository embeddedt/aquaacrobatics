package com.fuzs.aquaacrobatics.client.render;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import com.fuzs.aquaacrobatics.client.render.layer.LayerDrownedOuter;
import com.fuzs.aquaacrobatics.entity.EntityDrowned;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.client.renderer.entity.layers.LayerStrayClothing;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nonnull;

public class RenderDrowned extends RenderZombie {

    private ResourceLocation mobTexture = new ResourceLocation(AquaAcrobatics.MODID, "textures/entity/drowned.png");

    public static final Factory FACTORY = new Factory();

    public RenderDrowned(RenderManager rendermanagerIn) {
        super(rendermanagerIn);
        this.addLayer(new LayerDrownedOuter(this));
    }

    @Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull EntityZombie entity) {
        return mobTexture;
    }

    public static class Factory implements IRenderFactory<EntityDrowned> {

        @Override
        public Render<? super EntityDrowned> createRenderFor(RenderManager manager) {
            return new RenderDrowned(manager);
        }

    }

}