package com.fuzs.aquaacrobatics.util;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;

public class MovementInputStorage extends MovementInput {

    public int sprintToggleTimer;
    public int autoJumpTime;
    public boolean sprinting;
    public boolean isStartingToFly;

    public void copyFrom(MovementInput movement) {

        this.moveStrafe = movement.moveStrafe;
        this.moveForward = movement.moveForward;
        this.forwardKeyDown = movement.forwardKeyDown;
        this.backKeyDown = movement.backKeyDown;
        this.leftKeyDown = movement.leftKeyDown;
        this.rightKeyDown = movement.rightKeyDown;
        this.jump = movement.jump;
        this.sneak = movement.sneak;
    }

    public static void updatePlayerMoveState(MovementInput movement, GameSettings gameSettings, boolean isCrouching) {

        movement.forwardKeyDown = gameSettings.keyBindForward.isKeyDown();
        movement.backKeyDown = gameSettings.keyBindBack.isKeyDown();
        movement.leftKeyDown = gameSettings.keyBindLeft.isKeyDown();
        movement.rightKeyDown = gameSettings.keyBindRight.isKeyDown();
        movement.moveForward = movement.forwardKeyDown == movement.backKeyDown ? 0.0F : (movement.forwardKeyDown ? 1.0F : -1.0F);
        movement.moveStrafe = movement.leftKeyDown == movement.rightKeyDown ? 0.0F : (movement.leftKeyDown ? 1.0F : -1.0F);
        movement.jump = gameSettings.keyBindJump.isKeyDown();
        movement.sneak = gameSettings.keyBindSneak.isKeyDown();
        if (isCrouching) {

            movement.moveStrafe = (float) ((double) movement.moveStrafe * 0.3);
            movement.moveForward = (float) ((double) movement.moveForward * 0.3);
        }
    }

}