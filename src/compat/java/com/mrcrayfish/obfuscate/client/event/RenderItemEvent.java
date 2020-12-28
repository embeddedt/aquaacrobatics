package com.mrcrayfish.obfuscate.client.event;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@SuppressWarnings("unused")
public class RenderItemEvent extends Event {

    public RenderItemEvent(ItemStack heldItem, ItemCameraTransforms.TransformType transformType, float partialTicks) {

    }

    @Cancelable
    public static class Held extends RenderItemEvent {

        private EntityLivingBase entity;

        public Held(EntityLivingBase entity, ItemStack heldItem, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide, float partialTicks) {

            super(heldItem, transformType, partialTicks);
        }

        public EntityLivingBase getEntity() {

            return this.entity;
        }

        public static class Pre extends Held {

            public Pre(EntityLivingBase entity, ItemStack heldItem, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide, float partialTicks) {

                super(entity, heldItem, transformType, handSide, partialTicks);
            }
        }
    }

}
