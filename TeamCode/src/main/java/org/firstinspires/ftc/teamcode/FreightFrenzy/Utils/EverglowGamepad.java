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
            case "DpadLeft":
                if (previousLeft && !sus.y) {
                    return true;
                }
                break;
            case "DpadRight":
                if (previousRight && !sus.y) {
                    return true;
                }
                break;
            case "DpadUp":
                if (previousUp && !sus.y) {
                    return true;
                }
                break;
            case "DpadDown":
                if (previousDown && !sus.y) {
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
