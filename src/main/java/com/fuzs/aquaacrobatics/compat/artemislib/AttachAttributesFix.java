package com.fuzs.aquaacrobatics.compat.artemislib;

import com.artemis.artemislib.compatibilities.sizeCap.ISizeCap;
import com.artemis.artemislib.compatibilities.sizeCap.SizeCapPro;
import com.artemis.artemislib.util.AttachAttributes;
import com.artemis.artemislib.util.attributes.ArtemisLibAttributes;
import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.entity.Pose;
import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import com.fuzs.aquaacrobatics.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AttachAttributesFix extends AttachAttributes {

    @Override
    @SubscribeEvent
    public void attachAttributes(final EntityEvent.EntityConstructing evt) {

        super.attachAttributes(evt);
    }

    @Override
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent evt) {

        super.onPlayerTick(evt);
    }

    @Override
    @SubscribeEvent
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent evt) {

        super.onLivingUpdate(evt);
    }

    @Override
    @SubscribeEvent
    public void onEntityRenderPre(final RenderLivingEvent.Pre evt) {
        
        EntityLivingBase entity = evt.getEntity();
        if(entity.hasCapability(SizeCapPro.sizeCapability, null)) {

            ISizeCap cap = entity.getCapability(SizeCapPro.sizeCapability, null);
            if(cap != null && cap.getTrans()) {

                boolean isWidthModified = !entity.getAttributeMap().getAttributeInstance(ArtemisLibAttributes.ENTITY_WIDTH).getModifiers().isEmpty();
                boolean isHeightModified = !entity.getAttributeMap().getAttributeInstance(ArtemisLibAttributes.ENTITY_HEIGHT).getModifiers().isEmpty();
                if (isWidthModified || isHeightModified) {

                    double widthAttribute = entity.getAttributeMap().getAttributeInstance(ArtemisLibAttributes.ENTITY_WIDTH).getAttributeValue();
                    double heightAttribute = entity.getAttributeMap().getAttributeInstance(ArtemisLibAttributes.ENTITY_HEIGHT).getAttributeValue();
                    // prevent flicker when starting to swim by also checking the animation
                    if (entity instanceof IPlayerResizeable && ((IPlayerResizeable) entity).getPose() == Pose.SWIMMING && ((IPlayerResizeable) entity).getSwimAnimation(evt.getPartialRenderTick()) > 0.0F) {

                        heightAttribute *= 3.0;
                    }

                    widthAttribute = Math.max(widthAttribute, 0.15F);
                    heightAttribute = Math.max(heightAttribute, 0.25F);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(widthAttribute, heightAttribute, widthAttribute);
                    GlStateManager.translate(evt.getX() / widthAttribute - evt.getX(), evt.getY() / heightAttribute - evt.getY(), evt.getZ() / widthAttribute - evt.getZ());
                }
            }
        }
    }

    @Override
    @SubscribeEvent
    public void onLivingRenderPost(final RenderLivingEvent.Post evt) {

        super.onLivingRenderPost(evt);
    }

}
