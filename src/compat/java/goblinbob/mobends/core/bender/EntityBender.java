package goblinbob.mobends.core.bender;

import goblinbob.mobends.core.mutators.Mutator;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;

@SuppressWarnings("unused")
public abstract class EntityBender<T extends EntityLivingBase> {

	public abstract boolean isAnimated();

	public abstract Mutator<?, ?, ?> getMutator(RenderLivingBase<? extends EntityLivingBase> renderer);
	
}
