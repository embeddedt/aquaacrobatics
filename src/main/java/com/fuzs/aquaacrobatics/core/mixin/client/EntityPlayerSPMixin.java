package com.fuzs.aquaacrobatics.core.mixin.client;

import com.fuzs.aquaacrobatics.client.entity.IPlayerSPSwimming;
import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.entity.Pose;
import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import com.fuzs.aquaacrobatics.util.MovementInputStorage;
import com.fuzs.aquaacrobatics.util.math.AxisAlignedBBSpliterator;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SuppressWarnings("unused")
@Mixin(EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin extends AbstractClientPlayer implements IPlayerSPSwimming {

    @Shadow
    @Final
    public NetHandlerPlayClient connection;
    @Shadow
    protected Minecraft mc;
    @Shadow
    protected int sprintToggleTimer;
    @Shadow
    private int autoJumpTime;
    @Shadow
    public MovementInput movementInput;

    private final MovementInputStorage movementStorage = new MovementInputStorage();
    private boolean isCrouching;

    public EntityPlayerSPMixin(World worldIn, GameProfile playerProfile) {

        super(worldIn, playerProfile);
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isSneaking()Z"))
    private boolean onUpdateWalkingPlayerIsSneaking(EntityPlayerSP playerIn) {

        // send actual sneak state to server
        return this.isActuallySneaking();
    }

    @Inject(method = "isSneaking", at = @At("HEAD"), cancellable = true)
    public void isSneaking(CallbackInfoReturnable<Boolean> callbackInfo) {

        // don't check this directly every time to prevent crash with random things mod caused by loop
        callbackInfo.setReturnValue(this.isCrouching);
    }

    @Override
    public void spawnRunningParticles() {

        if (!this.capabilities.isFlying && !this.isSpectator() && !this.isSneaking() && !this.isInLava() && this.isEntityAlive()) {

            super.spawnRunningParticles();
        }
    }

    @Override
    public boolean isActuallySneaking() {

        // switched with #isSneaking
        return this.movementInput != null && this.movementInput.sneak;
    }

    @Override
    public boolean isForcedDown() {

        return ((IPlayerResizeable) this).isResizingAllowed() && !this.capabilities.isFlying ? this.isSneaking() || ((IPlayerResizeable) this).isVisuallySwimming() : this.isActuallySneaking();
    }

    @Override
    public boolean isUsingSwimmingAnimation(float moveForward, float moveStrafe) {

        if (this.canSwim()) {

            return this.isMovingForward(moveForward, moveStrafe);
        }

        if (ConfigHandler.MovementConfig.sidewaysSprinting) {

            return moveForward >= 0.8F || Math.abs(moveStrafe) > 0.8F;
        }

        return moveForward >= 0.8F;
    }

    @Override
    public boolean canSwim() {

        return ((IPlayerResizeable) this).getEyesInWaterPlayer();
    }

    @Override
    public boolean isMovingForward(float moveForward, float moveStrafe) {

        if (moveForward > 1.0E-5F) {

            return true;
        } else if (ConfigHandler.MovementConfig.sidewaysSwimming) {

            return Math.abs(moveStrafe) > 1.0E-5F;
        }

        return false;
    }

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    protected void pushOutOfBlocks(double x, double y, double z, CallbackInfoReturnable<Boolean> callbackInfo) {

        if (ConfigHandler.playerBlockCollisions != ConfigHandler.PlayerBlockCollisions.EXACT) {

            return;
        }

        if (!this.noClip) {

            this.setPlayerOffsetMotion(x, z);
        }

        // return value is never used
        callbackInfo.setReturnValue(false);
    }

    private void setPlayerOffsetMotion(double x, double z) {

        BlockPos blockpos = new BlockPos(x, this.posY, z);
        if (this.shouldBlockPushPlayer(blockpos)) {

            double d0 = x - blockpos.getX();
            double d1 = z - blockpos.getZ();
            EnumFacing direction = null;
            double d2 = Double.MAX_VALUE;
            EnumFacing[] xzPlane = new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH};

            for (EnumFacing direction1 : xzPlane) {

                EnumFacing.Axis axis = direction1.getAxis();
                double d3 = axis == EnumFacing.Axis.X ? d0 : axis == EnumFacing.Axis.Z ? d1 : 0.0;
                double d4 = direction1.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? 1.0 - d3 : d3;
                if (d4 < d2 && !this.shouldBlockPushPlayer(blockpos.offset(direction1))) {

                    d2 = d4;
                    direction = direction1;
                }
            }

            if (direction != null) {

                if (direction.getAxis() == EnumFacing.Axis.X) {

                    this.motionX = 0.1 * direction.getDirectionVec().getX();
                } else {

                    this.motionZ = 0.1 * direction.getDirectionVec().getZ();
                }
            }
        }
    }

    private boolean shouldBlockPushPlayer(BlockPos pos) {

        double minY = this.getEntityBoundingBox().minY;
        double maxY = this.getEntityBoundingBox().maxY;
        AxisAlignedBB aabb = new AxisAlignedBB(pos.getX(), minY, pos.getZ(), pos.getX() + 1.0, maxY, pos.getZ() + 1.0);

        // don't use IBlockState#causesSuffocation as it works differently in newer versions
        return !isAxisAlignedBBNotClear(this.world, this, aabb.shrink(1.0E-7));
    }

    private static boolean isAxisAlignedBBNotClear(World world, @Nullable Entity entity, AxisAlignedBB aabb) {

        return createAxisAlignedBBStream(world, entity, aabb).allMatch(Objects::isNull);
    }

    private static Stream<AxisAlignedBB> createAxisAlignedBBStream(World world, @Nullable Entity entity, AxisAlignedBB aabb) {

        return StreamSupport.stream(new AxisAlignedBBSpliterator(world, entity, aabb), false);
    }

    @Redirect(method = "pushOutOfBlocks", at = @At(value = "INVOKE", target = "Ljava/lang/Math;ceil(D)D"))
    private double ceil(double a) {

        if (ConfigHandler.playerBlockCollisions == ConfigHandler.PlayerBlockCollisions.APPROXIMATE) {

            a -= 0.65;
        }

        // make the player be able to sneak under full cubes with their new height of 1.5 blocks
        return Math.ceil(a);
    }

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    public void onLivingUpdatePre(CallbackInfo callbackInfo) {

        this.updateSprintToggleTimer();
        this.movementStorage.copyFrom(this.movementInput);
        this.movementStorage.isSprinting = this.isSprinting();
        this.movementStorage.isFlying = this.capabilities.isFlying;
        this.movementStorage.isStartingToFly = this.isStartingToFly();
    }

    private void updateSprintToggleTimer() {

        // added in 1.13+, so do this for the actual field
        if (this.movementInput.sneak) {

            this.sprintToggleTimer = 0;
        }

        this.movementStorage.sprintToggleTimer = this.sprintToggleTimer;
        if (this.movementStorage.sprintToggleTimer > 0) {

            --this.movementStorage.sprintToggleTimer;
        }

        if (this.isHandActive() && !this.isRiding()) {

            this.movementStorage.sprintToggleTimer = 0;
        }
    }

    private boolean isStartingToFly() {

        if (this.capabilities.allowFlying) {

            if (this.mc.playerController.isSpectatorMode()) {

                return !this.capabilities.isFlying;
            } else if (!this.movementInput.jump && this.mc.gameSettings.keyBindJump.isKeyDown() && this.autoJumpTime == 0) {

                return this.flyToggleTimer != 0 && !((IPlayerResizeable) this).isSwimming();
            }
        }

        return false;
    }

    @Inject(method = "onLivingUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;wasFallFlying:Z"))
    public void onLivingUpdate(CallbackInfo callbackInfo) {

        this.updatePlayerMoveState();
        this.isCrouching = this.isCrouching(((IPlayerResizeable) this).isPoseClear(Pose.STANDING));
        // handle sprinting behaviour
        this.setSprinting(this.movementStorage.isSprinting);
        boolean isSaturated = (float)this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;
        this.startSprinting(isSaturated);
        this.stopSprinting(isSaturated);
        // handle misc movement
        this.handleElytraTakeoff();
        this.handleWaterSneaking();
        this.slowDownSneakFlying();
    }

    private void updatePlayerMoveState() {

        if (!this.movementInput.sneak && this.isForcedDown()) {

            this.movementInput.moveStrafe = (float) ((double) this.movementInput.moveStrafe * 0.3);
            this.movementInput.moveForward = (float) ((double) this.movementInput.moveForward * 0.3);
        }

        if (this.movementInput.sneak && !this.isForcedDown()) {

            this.movementInput.moveStrafe = (float) ((double) this.movementInput.moveStrafe / 0.3);
            this.movementInput.moveForward = (float) ((double) this.movementInput.moveForward / 0.3);
        }
    }

    private boolean isCrouching(boolean cantStand) {

        if ((!this.movementStorage.isFlying || !cantStand) && this.getTicksElytraFlying() <= 4) {

            if (!((IPlayerResizeable) this).isSwimming() && (this.onGround || !this.isInWater())) {

                if (!this.isOnLadder() && ((IPlayerResizeable) this).isPoseClear(Pose.CROUCHING)) {

                    return this.movementInput.sneak || ((IPlayerResizeable) this).isResizingAllowed() && !this.isPlayerSleeping() && !cantStand;
                }
            }
        }

        return false;
    }

    private void startSprinting(boolean isSaturated) {

        boolean wasSneaking = this.movementStorage.sneak;
        boolean wasSwimming = this.isUsingSwimmingAnimation(this.movementStorage.moveForward, this.movementStorage.moveStrafe);
        boolean isSprintingEnvironment = this.onGround || this.canSwim() || this.movementStorage.isFlying;
        if (isSprintingEnvironment && !wasSneaking && !wasSwimming && this.isUsingSwimmingAnimation(this.movementInput.moveForward, this.movementInput.moveStrafe) && !this.isSprinting() && isSaturated && !this.isHandActive() && !this.isPotionActive(MobEffects.BLINDNESS)) {

            if (this.movementStorage.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {

                this.sprintToggleTimer = ConfigHandler.MovementConfig.noDoubleTapSprinting ? 0 : 7;
            } else {

                this.setSprinting(true);
            }
        }

        if (!this.isSprinting() && (!this.isInWater() || this.canSwim()) && this.isUsingSwimmingAnimation(this.movementInput.moveForward, this.movementInput.moveStrafe) && isSaturated && !this.isHandActive() && !this.isPotionActive(MobEffects.BLINDNESS) && this.mc.gameSettings.keyBindSprint.isKeyDown()) {

            this.setSprinting(true);
        }
    }

    private void stopSprinting(boolean isSaturated) {

        if (this.isSprinting()) {

            boolean isNotMoving = !this.isMovingForward(this.movementInput.moveForward, this.movementInput.moveStrafe) || !isSaturated;
            // don't stop sprint flying when breaching water surface
            boolean hasCollided = isNotMoving || this.collidedHorizontally || this.isInWater() && !this.canSwim() && !this.movementStorage.isFlying;
            if (((IPlayerResizeable) this).isSwimming()) {

                if (!this.movementInput.sneak && isNotMoving || !this.isInWater()) {

                    this.setSprinting(false);
                }
            } else if (hasCollided) {

                this.setSprinting(false);
            }
        }
    }

    private void handleElytraTakeoff() {

        // 1.15 change for easier elytra takeoff
        if (ConfigHandler.MovementConfig.easyElytraTakeoff && this.movementInput.jump && !this.movementStorage.isStartingToFly && !this.movementStorage.jump && this.motionY >= 0.0 && !this.capabilities.isFlying && !this.isRiding() && !this.isOnLadder()) {

            ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isUsable(itemstack)) {

                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_FALL_FLYING));
            }
        }
    }

    private void handleWaterSneaking() {

        // needs to be handled on the client since the server doesn't receive actual sneak state while in water
        if (this.isInWater() && this.movementInput.sneak && !this.capabilities.isFlying) {

            this.handleSneakWater();
        }
    }

    private void slowDownSneakFlying() {

        if (this.capabilities.isFlying && this.isCurrentViewEntity()) {

            if (this.movementInput.sneak) {

                // normally used to counter sneaking slowdown when flying, but sneaking is no longer activated while flying now
                this.movementInput.moveStrafe = (float) ((double) this.movementInput.moveStrafe * 0.3);
                this.movementInput.moveForward = (float) ((double) this.movementInput.moveForward * 0.3);
            }
        }
    }

    protected void handleSneakWater() {

        this.motionY -= 0.03999999910593033 * this.getEntityAttribute(SWIM_SPEED).getAttributeValue();
    }

    @Shadow
    protected abstract boolean isCurrentViewEntity();

}
