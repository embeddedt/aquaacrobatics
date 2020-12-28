package com.fuzs.aquaacrobatics.compat;

import goblinbob.mobends.core.bender.EntityBender;
import goblinbob.mobends.core.bender.EntityBenderRegistry;
import goblinbob.mobends.core.data.LivingEntityData;
import goblinbob.mobends.core.mutators.Mutator;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;

public class MoBendsCompat implements IModCompat {

    @SuppressWarnings({"rawtypes", "unchecked", "ConstantConditions"})
    @Override
    public void apply(Object... data) {

        RenderPlayer renderer = (RenderPlayer) data[0];
        AbstractClientPlayer entityLiving = (AbstractClientPlayer) data[1];
        EntityBender<AbstractClientPlayer> entityBender = EntityBenderRegistry.instance.getForEntity(entityLiving);
        if (entityBender != null && entityBender.isAnimated()) {

            Mutator mutator = entityBender.getMutator(renderer);
            LivingEntityData<AbstractClientPlayer> entityData = mutator.getData(entityLiving);
            if (!entityData.isStillHorizontally() && !entityData.isDrawingBow() && !(entityData.getTicksAfterAttack() < 10) && entityData.isUnderwater()) {

                GlStateManager.translate(0.0F, 0.2F, -0.9F);
            }
        }
    }

}
