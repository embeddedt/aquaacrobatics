package goblinbob.mobends.core.data;

import net.minecraft.entity.EntityLivingBase;

public abstract class LivingEntityData<E extends EntityLivingBase> extends EntityData<E> {

    public abstract boolean isDrawingBow();

    public abstract float getTicksAfterAttack();

}
