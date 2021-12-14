package org.firstinspires.ftc.teamcode.FreightFrenzy.Utils;

import com.qualcomm.robotcore.hardware.Gamepad;

public class EverglowGamepad {
    Gamepad sus;

    boolean previousA;
    boolean previousB;
    boolean previousX;
    boolean previousY;

    boolean previousRight;
    boolean previousLeft;
    boolean previousUp;
    boolean previousDown;

    public EverglowGamepad(Gamepad gamepad) {
        this.sus = gamepad;
    }

    public boolean buttonPress(String buttonName) {
        switch (buttonName) {
            case "a":
                if (previousA && !sus.a) {
                    return true;
                }
                break;
            case "b":
                if (previousB && !sus.b) {
                    return true;
                }
                break;
            case "x":
                if (previousX && !sus.x) {
                    return true;
                }
                break;
            case "y":
                if (previousY && !sus.y) {
                    return true;
                }
                break;
            case "dpad_left":
                if (previousLeft && !sus.dpad_left) {
                    return true;
                }
                break;
            case "dpad_right":
                if (previousRight && !sus.dpad_right) {
                    return true;
                }
                break;
            case "dpad_up":
                if (previousUp && !sus.dpad_up) {
                    return true;
                }
                break;
            case "dpad_down":
                if (previousDown && !sus.dpad_down) {
                    return true;
                }
                break;
        }
        return false;
    }

    public void update() {
        previousA = sus.a;
        previousB = sus.b;
        previousX = sus.x;
        previousY = sus.y;
    }

}
