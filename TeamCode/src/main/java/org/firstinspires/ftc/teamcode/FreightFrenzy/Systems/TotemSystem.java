package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

public class TotemSystem {
    private final LinearOpMode  opMode;
    public final Servo         altitude1;
    public final Servo         altitude2;
    private final CRServo       meterLeft;
    private final CRServo       meterRight;

    private final double ALTITUDE1_ZERO = 0.5;
    private final double ALTITUDE2_ZERO = 0.5;
    private final double ALTITUDE1_MAX = 0.67;
    private final double ALTITUDE2_MAX = 0.32;


    public TotemSystem(LinearOpMode opMode) {
        this.opMode   = opMode;
        altitude1     = opMode.hardwareMap.get(Servo.class, "altitude_right");
        altitude2     = opMode.hardwareMap.get(Servo.class, "altitude_left");
        meterLeft     = opMode.hardwareMap.get(CRServo.class, "meter_left");
        meterRight    = opMode.hardwareMap.get(CRServo.class, "meter_right");
        altitude1.setPosition(ALTITUDE1_ZERO);
        altitude2.setPosition(ALTITUDE2_ZERO);
    }

    public void extendLeft(double power) {
        meterLeft.setPower(power);
    }

    public void extendRight(double power) {
        meterRight.setPower(power);
    }

    public void stopLeft() {
        meterLeft.setPower(0);
    }

    public void stopRight() {
        meterRight.setPower(0);
    }

    public void moveAltitude(double delta) {
        if(altitude1.getPosition() + delta > ALTITUDE1_MAX || altitude2.getPosition() - delta < ALTITUDE2_MAX) return;

        altitude1.setPosition(altitude1.getPosition() + delta);
        altitude2.setPosition(altitude2.getPosition() - delta);
    }

    public void setAltitude(double position) {
        altitude1.setPosition(position);
        altitude2.setPosition(1 - position);
    }
}
