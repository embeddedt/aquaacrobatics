package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.entity.Pose;
import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import com.fuzs.aquaacrobatics.entity.player.IPlayerSPSwimming;
import com.fuzs.aquaacrobatics.util.MovementInputStorage;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    public boolean isActuallySneaking() {

        // switched with #isSneaking
        return this.movementInput != null && this.movementInput.sneak;
    }

    @Override
    public boolean isForcedDown() {

        return ((IPlayerResizeable) this).isResizingAllowed() && !this.capabilities.isFlying ? this.isSneaking() || ((IPlayerResizeable) this).isVisuallySwimming() : this.isActuallySneaking();
    }

    @Override
    public boolean isUsingSwimmingAnimation(float moveForward) {

        return this.canSwim() ? this.isMovingForward(moveForward) : (double) moveForward >= 0.8;
    }

    @Override
    public boolean canSwim() {

        return ((IPlayerResizeable) this).getEyesInWaterPlayer();
    }

    @Override
    public boolean isMovingForward(float moveForward) {

        return moveForward > 1.0E-5F;
    }

    @Inject(method = "onLivingUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/util/MovementInput;jump:Z"), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;timeUntilPortal:I"), to = @At(value = "INVOKE", target = "Lnet/minecraft/util/MovementInput;updatePlayerMoveState()V")))
    public void onLivingUpdatePre(CallbackInfo callbackInfo) {

        this.movementStorage.copyFrom(this.movementInput);
        this.movementStorage.sprintToggleTimer = this.sprintToggleTimer;
        this.movementStorage.autoJumpTime = this.autoJumpTime;
        this.movementStorage.sprinting = this.isSprinting();
    }

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/MovementInput;updatePlayerMoveState()V"))
    public void updatePlayerMoveState(MovementInput movementInput) {

        MovementInputStorage.updatePlayerMoveState(movementInput, this.mc.gameSettings, this.isForcedDown());
    }

    @Inject(method = "onLivingUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerCapabilities;allowFlying:Z"), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;collidedHorizontally:Z"), to = @At(value = "FIELD:FIRST", target = "Lnet/minecraft/client/Minecraft;playerController:Lnet/minecraft/client/multiplayer/PlayerControllerMP;")))
    public void onLivingUpdatePost(CallbackInfo callbackInfo) {

        this.sprintToggleTimer = this.movementStorage.sprintToggleTimer;
        this.setSprinting(this.movementStorage.sprinting);

        boolean jumping = this.movementStorage.jump;
        boolean sneaking = this.movementStorage.sneak;
        boolean swimming = this.isUsingSwimmingAnimation(this.movementStorage.moveForward);
        boolean cantStand = ((IPlayerResizeable) this).isPoseClear(Pose.STANDING);
        this.isCrouching = (!this.capabilities.isFlying || !cantStand) && this.getTicksElytraFlying() <= 4 && !((IPlayerResizeable) this).isSwimming() && (!this.isInWater() || this.onGround) && ((IPlayerResizeable) this).isPoseClear(Pose.CROUCHING) && (this.movementInput.sneak || ((IPlayerResizeable) this).isResizingAllowed() && !this.isPlayerSleeping() && !cantStand);
        if (this.isHandActive() && !this.isRiding()) {

            this.sprintToggleTimer = 0;
        }

        boolean hasAutoJumped = this.movementStorage.autoJumpTime > 0;
        if (sneaking) {

            this.sprintToggleTimer = 0;
        }

        boolean flag4 = (float)this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;
        if ((this.onGround || this.canSwim() || this.capabilities.isFlying) && !sneaking && !swimming && this.isUsingSwimmingAnimation(this.movementInput.moveForward) && !this.isSprinting() && flag4 && !this.isHandActive() && !this.isPotionActive(MobEffects.BLINDNESS)) {

            if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {

                this.sprintToggleTimer = 7;
            } else {

                this.setSprinting(true);
            }
        }

        if (!this.isSprinting() && (!this.isInWater() || this.canSwim()) && this.isUsingSwimmingAnimation(this.movementInput.moveForward) && flag4 && !this.isHandActive() && !this.isPotionActive(MobEffects.BLINDNESS) && this.mc.gameSettings.keyBindSprint.isKeyDown()) {

            this.setSprinting(true);
        }

        if (this.isSprinting()) {

            boolean flag5 = !this.isMovingForward(this.movementInput.moveForward) || !flag4;
            // don't stop sprint flying when breaching water surface
            boolean flag6 = flag5 || this.collidedHorizontally || this.isInWater() && !this.canSwim() && !this.capabilities.isFlying;
            if (((IPlayerResizeable) this).isSwimming()) {

                if (!this.movementInput.sneak && flag5 || !this.isInWater()) {

                    this.setSprinting(false);
                }
            } else if (flag6) {

                this.setSprinting(false);
            }
        }

        this.movementStorage.isStartingToFly = false;
        if (this.capabilities.allowFlying) {

            if (this.mc.playerController.isSpectatorMode()) {

                if (!this.capabilities.isFlying) {

                    this.movementStorage.isStartingToFly = true;
                }
            } else if (!jumping && this.movementInput.jump && !hasAutoJumped) {

                if (this.flyToggleTimer != 0 && !((IPlayerResizeable) this).isSwimming()) {

                    this.movementStorage.isStartingToFly = true;
                }
            }
        }
    }

    @Inject(method = "onLivingUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;wasFallFlying:Z"))
    public void onLivingUpdate(CallbackInfo callbackInfo) {

        // 1.15 change for easier elytra takeoff
        if (ConfigHandler.easyElytraTakeoff && this.movementInput.jump && !this.movementStorage.isStartingToFly && !this.movementStorage.jump && this.motionY >= 0.0 && !this.capabilities.isFlying && !this.isRiding() && !this.isOnLadder()) {

            ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isUsable(itemstack)) {

                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_FALL_FLYING));
            }
        }

        // needs to be handled on the client since the server doesn't receive actual sneak state while in water
        if (this.isInWater() && this.movementInput.sneak && !this.capabilities.isFlying) {

            this.handleSneakWater();
        }

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
