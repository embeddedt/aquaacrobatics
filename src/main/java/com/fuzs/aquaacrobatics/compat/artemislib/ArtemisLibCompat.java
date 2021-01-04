package com.fuzs.aquaacrobatics.compat.artemislib;

import com.artemis.artemislib.util.attributes.ArtemisLibAttributes;
import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.entity.Pose;
import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

public class ArtemisLibCompat {

    private static final UUID SWIMMING_HEIGHT_ID = UUID.fromString("F5ED4D20-4DAB-11EB-AE93-0242AC130002");
    private static final AttributeModifier SWIMMING_HEIGHT = new AttributeModifier(SWIMMING_HEIGHT_ID, "Swimming height modifier", -2.0 / 3.0, 2).setSaved(false);

    public static void updateSwimmingSize(EntityPlayer player, Pose pose) {

        IAttributeInstance iattributeinstance = player.getEntityAttribute(ArtemisLibAttributes.ENTITY_HEIGHT);
        if (iattributeinstance.getModifier(SWIMMING_HEIGHT_ID) != null) {

            iattributeinstance.removeModifier(SWIMMING_HEIGHT);
        }

        if (pose == Pose.SWIMMING) {

            iattributeinstance.applyModifier(SWIMMING_HEIGHT);
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRenderLivingPre(final RenderLivingEvent.Pre<? extends EntityLivingBase> evt) {

        // a lot of mods pop the matrix in between so this goes terribly wrong; still a nice fix if only artemislib is present alongside
        if (ConfigHandler.CompatConfig.artemisLibFixModel && evt.getEntity() instanceof EntityPlayer) {

            if (evt.getEntity().getAttributeMap().getAttributeInstance(ArtemisLibAttributes.ENTITY_HEIGHT).getModifiers().isEmpty()) {

                return;
            }

            IPlayerResizeable player = (IPlayerResizeable) evt.getEntity();
            // prevent flicker when starting to swim
            if (player.getPose() == Pose.SWIMMING && player.getSwimAnimation(evt.getPartialRenderTick()) > 0.0F || player.getPose() == Pose.FALL_FLYING) {

                GlStateManager.scale(1.0F, 3.0F, 1.0F);
                GlStateManager.translate(0.0F, -evt.getY() / 3.0F + evt.getY(), 0.0F);
            }
        }
    }

}
