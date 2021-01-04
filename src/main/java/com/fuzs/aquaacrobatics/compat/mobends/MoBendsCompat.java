package com.fuzs.aquaacrobatics.compat.mobends;

import goblinbob.mobends.core.bender.EntityBender;
import goblinbob.mobends.core.bender.EntityBenderRegistry;
import goblinbob.mobends.core.data.IEntityDataFactory;
import goblinbob.mobends.core.data.LivingEntityData;
import goblinbob.mobends.core.mutators.Mutator;
import goblinbob.mobends.standard.PlayerBender;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;

public class MoBendsCompat {

    public static void registerSwimmingPlayer() {

        EntityBenderRegistry.instance.registerBender(new PlayerBender() {

            @Override
            public IEntityDataFactory<AbstractClientPlayer> getDataFactory() {

                return SwimmingPlayerData::new;
            }

        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void applyRotations(RenderPlayer renderer, AbstractClientPlayer entityLiving) {

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
