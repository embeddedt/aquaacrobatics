package com.fuzs.fivefeetsmall.core.mixin;

import com.fuzs.fivefeetsmall.entity.Pose;
import com.fuzs.fivefeetsmall.entity.player.IPlayerSwimming;
import com.fuzs.fivefeetsmall.entity.player.IPlayerSPSwimming;
import com.fuzs.fivefeetsmall.util.MovementInputStorage;
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

    public EntityPlayerSPMixin(World worldIn, GameProfile playerProfile) {

        super(worldIn, playerProfile);
    }

    @Override
    public boolean isSneaking() {

        return this.hasSneakingInput();
    }

    @Override
    public boolean hasSneakingInput() {

        return this.movementInput != null && this.movementInput.sneak;
    }

    @Override
    public boolean shouldRenderSneaking() {

        if (!this.capabilities.isFlying && !((IPlayerSwimming) this).isSwimming() && ((IPlayerSwimming) this).isPoseClear(Pose.CROUCHING)) {

            return this.hasSneakingInput() || !this.isPlayerSleeping() && !((IPlayerSwimming) this).isPoseClear(Pose.STANDING);
        } else {

            return false;
        }
    }

    @Override
    public boolean func_228354_I_() {

        return this.shouldRenderSneaking() || ((IPlayerSwimming) this).isVisuallySwimming();
    }

    private boolean func_223110_ee() {

        return this.canSwim() ? this.func_223135_b() : (double)this.movementInput.moveForward >= 0.8D;
    }

    @Override
    public boolean canSwim() {

        return ((IPlayerSwimming) this).getEyesInWaterPlayer();
    }

    @Override
    public boolean func_223135_b() {

        return this.movementInput.moveForward > 1.0E-5F;
    }

    @Inject(method = "onLivingUpdate", at = @At(value = "FIELD", shift = At.Shift.BEFORE, target = "Lnet/minecraft/util/MovementInput;jump:Z"), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;timeUntilPortal:I"), to = @At(value = "INVOKE", target = "Lnet/minecraft/util/MovementInput;updatePlayerMoveState()V")))
    public void onLivingUpdate1(CallbackInfo callbackInfo) {

        this.storage.copyFrom(this.movementInput);
        this.storage.sprintToggleTimer = this.sprintToggleTimer;
        this.storage.autoJumpTime = this.autoJumpTime;
        this.storage.sprinting = this.isSprinting();
    }

    @Inject(method = "onLivingUpdate", at = @At(value = "FIELD", shift = At.Shift.BEFORE, target = "Lnet/minecraft/entity/player/PlayerCapabilities;allowFlying:Z"), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;collidedHorizontally:Z"), to = @At(value = "FIELD:FIRST", target = "Lnet/minecraft/client/Minecraft;playerController:Lnet/minecraft/client/multiplayer/PlayerControllerMP;")))
    public void onLivingUpdate2(CallbackInfo callbackInfo) {

        this.storage.copyTo(this.movementInput);
        this.sprintToggleTimer = this.storage.sprintToggleTimer;
        this.autoJumpTime = this.storage.autoJumpTime;
        this.setSprinting(this.storage.sprinting);

        boolean flag1 = this.movementInput.sneak;
        boolean flag2 = this.func_223110_ee();
        MovementInputStorage.updatePlayerMoveState(this.movementInput, this.mc.gameSettings, this.func_228354_I_());
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

        boolean flag4 = (float)this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;
        if ((this.onGround || this.canSwim()) && !flag1 && !flag2 && this.func_223110_ee() && !this.isSprinting() && flag4 && !this.isHandActive() && !this.isPotionActive(MobEffects.BLINDNESS)) {

            if (this.storage.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {

                this.sprintToggleTimer = 7;
            } else {

                this.setSprinting(true);
            }
        }

        if (!this.isSprinting() && (!this.isInWater() || this.canSwim()) && this.func_223110_ee() && flag4 && !this.isHandActive() && !this.isPotionActive(MobEffects.BLINDNESS) && this.mc.gameSettings.keyBindSprint.isKeyDown()) {

            this.setSprinting(true);
        }

        if (this.isSprinting()) {

            boolean flag5 = !this.func_223135_b() || !flag4;
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

}
