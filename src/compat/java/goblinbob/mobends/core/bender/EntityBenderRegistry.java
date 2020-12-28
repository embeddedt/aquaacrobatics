package goblinbob.mobends.core.bender;

import net.minecraft.entity.EntityLivingBase;

@SuppressWarnings("unused")
public class EntityBenderRegistry {

    public static final EntityBenderRegistry instance = null;

    public <E extends EntityLivingBase> EntityBender<E> getForEntity(E entity) {

        return null;
    }

}
