package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class TotemSystem {
    private final LinearOpMode opMode;

    public Servo azimuth;
    public Servo altitude;
    public Servo meter;

    public TotemSystem(LinearOpMode opMode) {
        this.opMode = opMode;
        azimuth = opMode.hardwareMap.get(Servo.class, "azimuth");
        altitude = opMode.hardwareMap.get(Servo.class, "altitude");
        meter    = opMode.hardwareMap.get(Servo.class, "extend");
    }

    public void setAzimuth(double pos) {
//        if (pos < 0) {
//            azimuth.setPosition(0);
//        } else {
//            azimuth.setPosition(pos);
//        }
        azimuth.setPosition(pos);
    }
    public void setAltitude(double pos) {
//        if (pos < 0) {
//            altitude.setPosition(0);
//        } else {
//            altitude.setPosition(pos);
//        }
        altitude.setPosition(pos);
    }
    public void extend(double pos) {
//        if (pos < 0) {
//            meter.setPosition(0);
//        } else {
//            meter.setPosition(pos);
//        }
        meter.setPosition(pos);
    }
}
