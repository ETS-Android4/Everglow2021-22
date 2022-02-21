package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class TotemSystem {
    private final LinearOpMode opMode;

    public Servo   azimuth;
    public Servo   altitude;
    public CRServo meter;

    private final double AZIMUTH_ZERO  = 0.449;
    private final double ALTITUDE_ZERO = 0.741;

    private double TOTEM_DELAY_TIME_MS = 250;

    private final  ElapsedTime lastAzimuthTime = new ElapsedTime();
    private final ElapsedTime lastAltitudeTime = new ElapsedTime();

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
     * @param speed the number of complete movements from one side to another that the robot should make in 1 second.
     */
    public void moveAzimuth(double speed) {
        if(lastAzimuthTime.milliseconds() < TOTEM_DELAY_TIME_MS || Math.abs(speed) < 0.05){
            return;
        }
        lastAzimuthTime.reset();
        azimuth.setPosition(azimuth.getPosition() + speed * TOTEM_DELAY_TIME_MS / 1000.);
    }

    public void moveAltitude(double speed) {
        if(lastAltitudeTime.milliseconds() < TOTEM_DELAY_TIME_MS || Math.abs(speed) < 0.05){
            return;
        }
        lastAltitudeTime.reset();
        altitude.setPosition(altitude.getPosition() + speed * TOTEM_DELAY_TIME_MS / 1000.);
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
