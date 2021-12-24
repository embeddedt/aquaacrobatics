package com.fuzs.aquaacrobatics.core.galacticraft.mixin;

import com.fuzs.aquaacrobatics.entity.Pose;
import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityClientPlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GCEntityClientPlayerMP.class)
public abstract class GCEntityClientPlayerMPMixin extends EntityPlayerSP {
    public GCEntityClientPlayerMPMixin(Minecraft p_i47378_1_, World p_i47378_2_, NetHandlerPlayClient p_i47378_3_, StatisticsManager p_i47378_4_, RecipeBook p_i47378_5_) {
        super(p_i47378_1_, p_i47378_2_, p_i47378_3_, p_i47378_4_, p_i47378_5_);
    }
    
    /**
     * @author embeddedt
     */
    @Overwrite
    public float getEyeHeight() {
        if (this.isPlayerSleeping()) {
            return 0.2f;
        }
        IPlayerResizeable player = ((IPlayerResizeable)this);
        return player.getStandingEyeHeight(player.getPose(), player.getSize(player.getPose()));
        /*
        if (this.isPlayerSleeping()) {
            return super.getEyeHeight();
        } else {
            float ySize = 0.0f;
            if (this.world.provider instanceof IZeroGDimension) {
                GCPlayerStatsClient stats = GCPlayerStatsClient.get(this);
                if (stats.getLandingTicks() > 0) {
                    ySize = stats.getLandingYOffset()[stats.getLandingTicks()];
                } else if (stats.getFreefallHandler().pjumpticks > 0) {
                    ySize = 0.01F * (float)stats.getFreefallHandler().pjumpticks;
                }
            }
            return Math.min(this.getDefaultEyeHeight() - ySize, super.getEyeHeight());
        }
        
         */
    }
}
