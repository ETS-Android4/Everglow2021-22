package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

public class TotemSystem {
    private final LinearOpMode opMode;

    public Servo   azimuth;
    public Servo   altitude;
    public CRServo meter;

    private final double AZIMUTH_ZERO  = 0.56;
    private final double ALTITUDE_ZERO = 0.09;

    public TotemSystem(LinearOpMode opMode) {
        this.opMode = opMode;
        azimuth     = opMode.hardwareMap.get(Servo.class, "azimuth");
        altitude    = opMode.hardwareMap.get(Servo.class, "altitude");
        meter       = opMode.hardwareMap.get(CRServo.class, "extend");
        azimuth.setPosition(AZIMUTH_ZERO);
        altitude.setPosition(ALTITUDE_ZERO);
    }

    public void moveAzimuth(double pos) {
        azimuth.setPosition(azimuth.getPosition() + pos);
    }

    public void moveAltitude(double pos) {
        altitude.setPosition(altitude.getPosition() + pos);
    }

    public void setAzimuth(double pos) {
        azimuth.setPosition(pos);
    }

    public void setAltitude(double pos) {
        altitude.setPosition(pos);
    }

    public void extend(double power) {
        meter.setPower(power);
    }

    public void stop() {
        meter.setPower(0);
    }
}
