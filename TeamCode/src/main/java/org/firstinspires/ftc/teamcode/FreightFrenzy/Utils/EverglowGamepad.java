package org.firstinspires.ftc.teamcode.FreightFrenzy.Utils;

import com.qualcomm.robotcore.hardware.Gamepad;

public class EverglowGamepad {
    private final Gamepad gamepad;

    private boolean previousA;
    private boolean previousB;
    private boolean previousX;
    private boolean previousY;
    private boolean previousRight;
    private boolean previousLeft;
    private boolean previousUp;
    private boolean previousDown;
    private boolean previousRb;
    private boolean  previousRt;
    private boolean previousLb;
    private boolean  previousLt;

    private boolean currentA;
    private boolean currentB;
    private boolean currentX;
    private boolean currentY;
    private boolean currentRight;
    private boolean currentLeft;
    private boolean currentUp;
    private boolean currentDown;
    private boolean currentRb;
    private boolean  currentRt;
    private boolean currentLb;
    private boolean  currentLt;


    public EverglowGamepad(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    public boolean a(){
        return currentA && !previousA;
    }

    public boolean b(){
        return currentB && !previousB;
    }

    public boolean x(){
        return currentX && !previousX;
    }

    public boolean y(){
        return currentY && !previousY;
    }

    public boolean dpad_left(){
        return currentLeft && !previousLeft;
    }

    public boolean dpad_right(){
        return currentRight && !previousRight;
    }

    public boolean dpad_up(){
        return currentUp && !previousUp;
    }

    public boolean dpad_down(){
        return currentDown && !previousDown;
    }

    public boolean rt(){
        return currentRt && !previousRt;
    }

    public boolean lt(){
        return currentLt && !previousLt;
    }

    public boolean rb(){
        return currentRb && !previousRb;
    }

    public boolean lb(){
        return currentLb && !previousLb;
    }

    public void update() {
        previousA = currentA;
        previousB = currentB;
        previousX = currentX;
        previousY = currentY;
        previousRight = currentRight;
        previousLeft  = currentLeft;
        previousUp    = currentUp;
        previousDown  = currentDown;

        previousLb = currentLb;
        previousLt = currentLt;
        previousRb = currentRb;
        previousRt = currentRt;

        currentA = gamepad.a;
        currentB = gamepad.b;
        currentX = gamepad.x;
        currentY = gamepad.y;
        currentRight = gamepad.dpad_right;
        currentLeft = gamepad.dpad_left;
        currentUp = gamepad.dpad_up;
        currentDown = gamepad.dpad_down;
        currentLb = gamepad.left_bumper;
        currentLt = gamepad.left_trigger > 0.1;
        currentRb = gamepad.right_bumper;
        currentRt = gamepad.right_trigger > 0.1;

    }

}
