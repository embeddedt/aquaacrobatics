package goblinbob.mobends.core.mutators;

import goblinbob.mobends.core.data.LivingEntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;

@SuppressWarnings("unused")
public abstract class Mutator<D extends LivingEntityData<E>, E extends EntityLivingBase, M extends ModelBase> {

    public abstract D getData(E entity);

}
