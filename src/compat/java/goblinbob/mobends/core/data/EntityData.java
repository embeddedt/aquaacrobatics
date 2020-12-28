package goblinbob.mobends.core.data;

import net.minecraft.entity.Entity;

@SuppressWarnings("unused")
public abstract class EntityData<E extends Entity> {

    public abstract boolean isStillHorizontally();

    public abstract boolean isUnderwater();

}
