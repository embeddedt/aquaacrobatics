package com.fuzs.fivefeetsmall.core.mixin;

import com.fuzs.fivefeetsmall.entity.EntitySize;
import com.fuzs.fivefeetsmall.entity.Pose;
import com.fuzs.fivefeetsmall.entity.player.IPlayerSwimming;
import com.fuzs.fivefeetsmall.network.datasync.PoseSerializer;
import com.fuzs.fivefeetsmall.util.MathHelper;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityLivingBase implements IPlayerSwimming {

    private static final EntitySize SLEEPING_SIZE = EntitySize.fixed(0.2F, 0.2F);
    private static final EntitySize STANDING_SIZE = EntitySize.flexible(0.6F, 1.8F);
    private static final Map<Pose, EntitySize> SIZE_BY_POSE = ImmutableMap.<Pose, EntitySize>builder().put(Pose.STANDING, STANDING_SIZE).put(Pose.SLEEPING, SLEEPING_SIZE).put(Pose.FALL_FLYING, EntitySize.flexible(0.6F, 0.6F)).put(Pose.SWIMMING, EntitySize.flexible(0.6F, 0.6F)).put(Pose.SPIN_ATTACK, EntitySize.flexible(0.6F, 0.6F)).put(Pose.CROUCHING, EntitySize.flexible(0.6F, 1.5F)).put(Pose.DYING, EntitySize.fixed(0.2F, 0.2F)).build();
    private static final DataParameter<Pose> POSE = EntityDataManager.createKey(EntityPlayer.class, PoseSerializer.POSE);

    protected boolean eyesInWater;
    protected boolean eyesInWaterPlayer;
    private EntitySize size;
    private float eyeHeight;
    private float swimAnimation;
    private float lastSwimAnimation;

    @Shadow
    public PlayerCapabilities capabilities;

    public EntityPlayerMixin(World worldIn) {

        super(worldIn);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onConstructed(CallbackInfo callbackInfo) {

        this.size = EntitySize.flexible(0.6F, 1.8F);
        this.eyeHeight = this.getEyeHeight(Pose.STANDING, this.size);
        this.dataManager.register(POSE, Pose.STANDING);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {

        if (POSE.equals(key)) {

            this.recalculateSize();
        }

        super.notifyDataManagerChange(key);
    }

    @Override
    public void onEntityUpdate() {

        super.onEntityUpdate();

        // updateAquatics
        this.updateEyesInWater();
        this.updateSwimming();
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    public void onDeath(DamageSource cause, CallbackInfo callbackInfo) {

        if (this.isDead && this.getPose() != Pose.DYING) {

            this.setPose(Pose.DYING);
        }
    }

    public boolean canSwim() {
        return this.eyesInWater && this.isInWater();
    }

    public void updateSwimming() {

        if (this.isSwimming()) {

            this.setSwimming(this.isSprinting() && this.isInWater() && !this.isRiding());
        } else {

            this.setSwimming(this.isSprinting() && this.canSwim() && !this.isRiding());
        }
    }

    private void updateEyesInWater() {

        this.eyesInWater = this.isInsideOfMaterial(Material.WATER);
    }

    protected boolean updateEyesInWaterPlayer() {

        this.eyesInWaterPlayer = this.isInsideOfMaterial(Material.WATER);
        return this.eyesInWaterPlayer;
    }

    public boolean getEyesInWaterPlayer() {

        return this.eyesInWaterPlayer;
    }

    @Override
    public EntitySize getSize(Pose poseIn) {

        return poseIn == Pose.SLEEPING ? SLEEPING_SIZE : SIZE_BY_POSE.getOrDefault(poseIn, STANDING_SIZE);
    }

    @Override
    public void recalculateSize() {

        EntitySize entitysize = this.size;
        Pose pose = this.getPose();
        EntitySize entitysize1 = this.getSize(pose);
        this.size = entitysize1;
        this.eyeHeight = this.getEyeHeight(pose, entitysize1);
        if (entitysize1.width < entitysize.width) {

            double d0 = (double)entitysize1.width / 2.0D;
            this.setEntityBoundingBox(new AxisAlignedBB(this.posX - d0, this.posY, this.posZ - d0, this.posX + d0, this.posY + (double) entitysize1.height, this.posZ + d0));
        } else {

            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
            this.setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double) entitysize1.width, axisalignedbb.minY + (double) entitysize1.height, axisalignedbb.minZ + (double) entitysize1.width));
            if (entitysize1.width > entitysize.width && !this.firstUpdate && !this.world.isRemote) {

                float f = entitysize.width - entitysize1.width;
                this.move(MoverType.SELF, f, 0.0, f);
            }

        }
    }

    protected float getEyeHeight(Pose poseIn, EntitySize sizeIn) {

        return poseIn == Pose.SLEEPING ? 0.2F : this.getStandingEyeHeight(poseIn, sizeIn);
    }

    @Override
    public float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {

        switch(poseIn) {

            case SWIMMING:
            case FALL_FLYING:
            case SPIN_ATTACK:
                return 0.4F;
            case CROUCHING:
                return 1.27F;
            default:
                return 1.62F;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public float getEyeHeight(Pose poseIn) {

        return this.getEyeHeight(poseIn, this.getSize(poseIn));
    }

    @Inject(method = "getEyeHeight", at = @At("HEAD"), cancellable = true)
    public final void getEyeHeight(CallbackInfoReturnable<Float> callbackInfoReturnable) {

        callbackInfoReturnable.setReturnValue(this.eyeHeight);
    }

    @Shadow
    public abstract boolean isSpectator();

    protected void setPose(Pose poseIn) {

        this.dataManager.set(POSE, poseIn);
    }

    @Override
    public Pose getPose() {

        return this.dataManager.get(POSE);
    }

    public boolean isPoseClear(Pose poseIn) {

        return this.world.getCollisionBoxes(this, this.getBoundingBox(poseIn)).isEmpty();
    }

    @Inject(method = "onUpdate", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/entity/EntityLivingBase;onUpdate()V"))
    public void onUpdate(CallbackInfo callbackInfo) {

        this.updateSwimAnimation();
        this.updateEyesInWaterPlayer();
    }

    @Inject(method = "updateSize", at = @At("HEAD"), cancellable = true)
    protected void updateSize(CallbackInfo callbackInfo) {

        this.updatePose();
        FMLCommonHandler.instance().onPlayerPostTick((EntityPlayer) (Object) this);
        callbackInfo.cancel();
    }


    protected void updatePose() {

        if (this.isPoseClear(Pose.SWIMMING)) {

            Pose pose;
            if (this.isElytraFlying()) {

                pose = Pose.FALL_FLYING;
            } else if (this.isPlayerSleeping()) {

                pose = Pose.SLEEPING;
            } else if (this.isSwimming()) {

                pose = Pose.SWIMMING;
            } else if (this.isSneaking() && !this.capabilities.isFlying) {

                pose = Pose.CROUCHING;
            } else {

                pose = Pose.STANDING;
            }

            Pose pose1;
            if (!this.isSpectator() && !this.isRiding() && !this.isPoseClear(pose)) {

                if (this.isPoseClear(Pose.CROUCHING)) {

                    pose1 = Pose.CROUCHING;
                } else {

                    pose1 = Pose.SWIMMING;
                }
            } else {

                pose1 = pose;
            }

            this.setPose(pose1);

            // update those as they're still in use in a lot of places
            this.width = this.getSize(pose1).width;
            this.height = this.getSize(pose1).height;
        }
    }

    protected AxisAlignedBB getBoundingBox(Pose p_213321_1_) {
        
        EntitySize entitysize = this.getSize(p_213321_1_);
        float f = entitysize.width / 2.0F;
        Vec3d vec3d = new Vec3d(this.posX - (double)f, this.posY, this.posZ - (double)f);
        Vec3d vec3d1 = new Vec3d(this.posX + (double)f, this.posY + (double)entitysize.height, this.posZ + (double)f);
        return new AxisAlignedBB(vec3d, vec3d1);
    }

    @Override
    public boolean isSwimming() {

        return !this.capabilities.isFlying && !this.isSpectator() && this.getFlag(4);
    }

    @Override
    public boolean isActuallySwimming() {

        return this.getPose() == Pose.SWIMMING || !this.isElytraFlying() && this.getPose() == Pose.FALL_FLYING;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isVisuallySwimming() {

        return this.isActuallySwimming() && !this.isInWater();
    }

    @Override
    public void setSwimming(boolean flag) {

        this.setFlag(4, flag);
    }

    @Override
    public float getSwimAnimation(float partialTicks) {

        return MathHelper.lerp(partialTicks, this.lastSwimAnimation, this.swimAnimation);
    }

    private void updateSwimAnimation() {

        this.lastSwimAnimation = this.swimAnimation;
        if (((IPlayerSwimming) this).isActuallySwimming()) {

            this.swimAnimation = Math.min(1.0F, this.swimAnimation + 0.09F);
        } else {

            this.swimAnimation = Math.max(0.0F, this.swimAnimation - 0.09F);
        }
    }

}
