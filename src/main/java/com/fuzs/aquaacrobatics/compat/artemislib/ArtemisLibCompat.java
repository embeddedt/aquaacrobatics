package com.fuzs.aquaacrobatics.compat.artemislib;

import com.artemis.artemislib.util.AttachAttributes;
import com.artemis.artemislib.util.attributes.ArtemisLibAttributes;
import com.fuzs.aquaacrobatics.core.mixin.accessor.IEventBusAccessor;
import com.fuzs.aquaacrobatics.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

import java.util.Optional;
import java.util.UUID;

public class ArtemisLibCompat {

    private static final UUID SWIMMING_HEIGHT_ID = UUID.fromString("F5ED4D20-4DAB-11EB-AE93-0242AC130002");
    private static final AttributeModifier SWIMMING_HEIGHT = new AttributeModifier(SWIMMING_HEIGHT_ID, "Swimming height modifier", -2.0 / 3.0, 2);

    public static void register() {

        Optional<Object> optional = ((IEventBusAccessor) MinecraftForge.EVENT_BUS).getListeners().keySet().stream()
                .filter(key -> key instanceof AttachAttributes).findFirst();
        if (optional.isPresent()) {

            MinecraftForge.EVENT_BUS.unregister(optional.get());
            MinecraftForge.EVENT_BUS.register(new AttachAttributesFix());
        }
    }

    public static void updateSwimmingSize(EntityPlayer player, Pose pose) {

        IAttributeInstance iattributeinstance = player.getEntityAttribute(ArtemisLibAttributes.ENTITY_HEIGHT);
        if (iattributeinstance.getModifier(SWIMMING_HEIGHT_ID) != null) {

            iattributeinstance.removeModifier(SWIMMING_HEIGHT);
        }

        if (pose == Pose.SWIMMING) {

            iattributeinstance.applyModifier(SWIMMING_HEIGHT);
        }
    }

}
