package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.entity.Pose;
import com.fuzs.aquaacrobatics.entity.player.IPlayerSPSwimming;
import com.fuzs.aquaacrobatics.entity.player.IPlayerSwimming;
import com.fuzs.aquaacrobatics.util.MovementInputStorage;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin extends AbstractClientPlayer implements IPlayerSPSwimming {

    @Shadow
    protected Minecraft mc;
    @Shadow
    protected int sprintToggleTimer;
    @Shadow
    private int autoJumpTime;
    @Shadow
    public MovementInput movementInput;

    private final MovementInputStorage storage = new MovementInputStorage();
    private boolean isCrouching;

    public EntityPlayerSPMixin(World worldIn, GameProfile playerProfile) {

        super(worldIn, playerProfile);
    }

    @Inject(method = "isSneaking", at = @At("HEAD"), cancellable = true)
    public void isSneaking(CallbackInfoReturnable<Boolean> callbackInfo) {

        callbackInfo.setReturnValue(this.isCrouching);
    }

    @Override
    public boolean isForcedDown() {

        return this.isSneaking() || ((IPlayerSwimming) this).isVisuallySwimming();
    }

    private boolean isUsingSwimmingAnimation() {

        return this.canSwim() ? this.isMovingForward() : (double)this.movementInput.moveForward >= 0.8D;
    }

    @Override
    public boolean canSwim() {

        return ((IPlayerSwimming) this).getEyesInWaterPlayer();
    }

    @Override
    public boolean isMovingForward() {

        return this.movementInput.moveForward > 1.0E-5F;
    }

    @Inject(method = "onLivingUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/util/MovementInput;jump:Z"), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;timeUntilPortal:I"), to = @At(value = "INVOKE", target = "Lnet/minecraft/util/MovementInput;updatePlayerMoveState()V")))
    public void onLivingUpdatePre(CallbackInfo callbackInfo) {

        this.storage.copyFrom(this.movementInput);
        this.storage.sprintToggleTimer = this.sprintToggleTimer;
        this.storage.autoJumpTime = this.autoJumpTime;
        this.storage.sprinting = this.isSprinting();
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "onLivingUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerCapabilities;allowFlying:Z"), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;collidedHorizontally:Z"), to = @At(value = "FIELD:FIRST", target = "Lnet/minecraft/client/Minecraft;playerController:Lnet/minecraft/client/multiplayer/PlayerControllerMP;")))
    public void onLivingUpdatePost(CallbackInfo callbackInfo) {

        this.storage.copyTo(this.movementInput);
        this.sprintToggleTimer = this.storage.sprintToggleTimer;
        this.autoJumpTime = this.storage.autoJumpTime;
        this.setSprinting(this.storage.sprinting);

        boolean flag1 = this.movementInput.sneak;
        boolean flag2 = this.isUsingSwimmingAnimation();
        this.isCrouching = !this.capabilities.isFlying && !((IPlayerSwimming) this).isSwimming() && (!this.isInWater() || this.onGround) && ((IPlayerSwimming) this).isPoseClear(Pose.CROUCHING) && (this.movementInput != null && this.movementInput.sneak || !this.isPlayerSleeping() && !((IPlayerSwimming) this).isPoseClear(Pose.STANDING));
        MovementInputStorage.updatePlayerMoveState(this.movementInput, this.mc.gameSettings, this.isForcedDown());
        net.minecraftforge.client.ForgeHooksClient.onInputUpdate((EntityPlayerSP) (Object) this, this.movementInput);

        if (this.isHandActive() && !this.isRiding()) {

            this.movementInput.moveStrafe *= 0.2F;
            this.movementInput.moveForward *= 0.2F;
            this.sprintToggleTimer = 0;
        }

        if (this.autoJumpTime > 0) {

            --this.autoJumpTime;
            this.movementInput.jump = true;
        }

        if (flag1) {

            this.sprintToggleTimer = 0;
        }

        boolean flag4 = (float)this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;
        if ((this.onGround || this.canSwim() || this.capabilities.isFlying) && !flag1 && !flag2 && this.isUsingSwimmingAnimation() && !this.isSprinting() && flag4 && !this.isHandActive() && !this.isPotionActive(MobEffects.BLINDNESS)) {

            if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {

                this.sprintToggleTimer = 7;
            } else {

                this.setSprinting(true);
            }
        }

        if (!this.isSprinting() && (!this.isInWater() || this.canSwim()) && this.isUsingSwimmingAnimation() && flag4 && !this.isHandActive() && !this.isPotionActive(MobEffects.BLINDNESS) && this.mc.gameSettings.keyBindSprint.isKeyDown()) {

            this.setSprinting(true);
        }

        if (this.isSprinting()) {

            boolean flag5 = !this.isMovingForward() || !flag4;
            boolean flag6 = flag5 || this.collidedHorizontally || this.isInWater() && !this.canSwim();
            if (((IPlayerSwimming) this).isSwimming()) {

                if (!this.onGround && !this.movementInput.sneak && flag5 || !this.isInWater()) {

                    this.setSprinting(false);
                }
            } else if (flag6) {

                this.setSprinting(false);
            }
        }
    }

    @Inject(method = "onLivingUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;wasFallFlying:Z"))
    public void onLivingUpdate(CallbackInfo callbackInfo) {

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
