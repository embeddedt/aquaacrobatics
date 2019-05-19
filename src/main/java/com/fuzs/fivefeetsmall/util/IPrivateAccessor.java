package com.fuzs.fivefeetsmall.util;

import com.fuzs.fivefeetsmall.FiveFeetSmall;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Method;

public interface IPrivateAccessor {

    String[] ENTITY_SETSIZE = new String[]{"setSize", "func_70105_a"};
    
    default Method findSetSize() {
        try {
            return ObfuscationReflectionHelper.findMethod(Entity.class, ENTITY_SETSIZE[1], void.class, float.class, float.class);
        } catch (Exception e) {
            FiveFeetSmall.LOGGER.error("findSetSize() failed", e);
        }
        return null;
    }
}
