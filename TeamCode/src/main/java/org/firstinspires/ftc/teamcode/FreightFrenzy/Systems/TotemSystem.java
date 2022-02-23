package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class TotemSystem {
    private final LinearOpMode opMode;
    public final double AZIMUTH_ZERO  = 0.45;
    public final double ALTITUDE_ZERO = 0.74;
    public final double AZIMUTH_SO_ALTITUDE_CAN_GET_LARGE = 0.8;
    public final double ALTITUDE_MAX = 0.8;
    private final double TOTEM_DELAY_TIME_MS = 250;
    private final ElapsedTime lastAzimuthTime  = new ElapsedTime();
    private final ElapsedTime lastAltitudeTime = new ElapsedTime();
    public Servo   azimuth;
    public Servo   altitude;
    public CRServo meter;

    public TotemSystem(LinearOpMode opMode) {
        this.opMode = opMode;
        azimuth     = opMode.hardwareMap.get(Servo.class, "azimuth");
        altitude    = opMode.hardwareMap.get(Servo.class, "altitude");
        meter       = opMode.hardwareMap.get(CRServo.class, "extend");
        azimuth.setPosition(AZIMUTH_ZERO);
        altitude.setPosition(ALTITUDE_ZERO);
    }

    /**
     * Moves the azimuch of the totem. Positive numbers move up, negetive numbers down.
     *
     * @param pos the number of complete movements from one side to another that the robot should make in 1 second.
     */
    public void moveAzimuth(double pos) {
//        if (lastAzimuthTime.milliseconds() < TOTEM_DELAY_TIME_MS || Math.abs(pos) < 0.05) {
//            return;
//        }
//        lastAzimuthTime.reset();
//        azimuth.setPosition(azimuth.getPosition() + pos * TOTEM_DELAY_TIME_MS / 1000.);
        azimuth.setPosition(azimuth.getPosition() + pos);
    }

    public void moveAltitude(double pos) {
//        if (lastAltitudeTime.milliseconds() < TOTEM_DELAY_TIME_MS || Math.abs(pos) < 0.05) {
//            return;
//        }
//        lastAltitudeTime.reset();
//        altitude.setPosition(altitude.getPosition() + pos * TOTEM_DELAY_TIME_MS / 1000.);
        altitude.setPosition(altitude.getPosition() + pos);
    }

    public void setAzimuth(double pos) {
        azimuth.setPosition(pos);
    }

    public void setAltitude(double pos) {
        altitude.setPosition(pos);
    }

    public void extend(double power) {
        meter.setPower(-power);
    }

    public void stop() {
        meter.setPower(0);
    }
}
