package com.fuzs.aquaacrobatics.core.mixin.accessor;

import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
@Mixin(EventBus.class)
public interface IEventBusAccessor {

    @Accessor(remap = false)
    ConcurrentHashMap<Object, ArrayList<IEventListener>> getListeners();

}
