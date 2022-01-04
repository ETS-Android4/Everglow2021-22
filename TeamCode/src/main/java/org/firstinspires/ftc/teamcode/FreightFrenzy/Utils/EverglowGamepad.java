package org.firstinspires.ftc.teamcode.FreightFrenzy.Utils;

import com.qualcomm.robotcore.hardware.Gamepad;

public class EverglowGamepad {
    private final Gamepad sus;

    private boolean previousA;
    private boolean previousB;
    private boolean previousX;
    private boolean previousY;

    private boolean previousRight;
    private boolean previousLeft;
    private boolean previousUp;
    private boolean previousDown;

    private boolean previousRb;
    private double previousRt;
    private boolean previousLb;
    private double previousLt;

    public EverglowGamepad(Gamepad gamepad) {
        this.sus = gamepad;
    }

    public boolean buttonPress(String buttonName) {
        switch (buttonName) {
            case "a":
                if (!previousA && sus.a) {
                    return true;
                }
                break;
            case "b":
                if (!previousB && sus.b) {
                    return true;
                }
                break;
            case "x":
                if (!previousX && sus.x) {
                    return true;
                }
                break;
            case "y":
                if (!previousY && sus.y) {
                    return true;
                }
                break;
            case "dpad_left":
                if (!previousLeft && sus.dpad_left) {
                    return true;
                }
                break;
            case "dpad_right":
                if (!previousRight && sus.dpad_right) {
                    return true;
                }
                break;
            case "dpad_up":
                if (!previousUp && sus.dpad_up) {
                    return true;
                }
                break;
            case "dpad_down":
                if (!previousDown && sus.dpad_down) {
                    return true;
                }
                break;
            case "Rt":
                if ((previousRt < 0.1) && (sus.right_trigger > 0.1)) {
                    return true;
                }
                break;
            case "Rb":
                if (!previousRb && sus.right_bumper) {
                    return true;
                }
                break;
            case "Lt":
                if ((previousLt < 0.1) && (sus.left_trigger > 0.1)) {
                    return true;
                }
                break;
            case "Lb":
                if (!previousLb && sus.left_bumper) {
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

        previousRight = sus.dpad_right;
        previousLeft = sus.dpad_left;
        previousUp = sus.dpad_up;
        previousDown = sus.dpad_down;

        previousLb = sus.left_bumper;
        previousLt = sus.left_trigger;
        previousRb = sus.right_bumper;
        previousRt = sus.right_trigger;
    }

}
