package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class TotemSystem {
    private final LinearOpMode opMode;
    public static final double AZIMUTH_ZERO  = 0.45;
    public static final double ALTITUDE_ZERO = 0.74;
    public static final double AZIMUTH_SO_ALTITUDE_CAN_GET_LARGE = 0.8;
    public static final double ALTITUDE_MAX = 0.8;
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
        meter.setPower(-power);
    }

    public void stop() {
        meter.setPower(0);
    }

    public void collectTotem(ArmSystem.Floors floor){
        moveAltitude(0.01);
        switch (floor) {
            case FIRST:
                moveAzimuth(-0.04);
                extend(0.5);
                TimeUtils.sleep(2000);
                break;
            case SECOND:
                moveAzimuth(0.05);
                extend(0.5);
                TimeUtils.sleep(2000);
                break;
            case THIRD:
                moveAzimuth(0.145);
                extend(0.5);
                TimeUtils.sleep(2500);
        }
        stop();
        setAltitude(ALTITUDE_MAX);
        setAzimuth(AZIMUTH_SO_ALTITUDE_CAN_GET_LARGE);
        moveAltitude(0.1);
        extend(-0.5);
        switch (floor) {
            case FIRST:
            case SECOND:
                TimeUtils.sleep(2000);
                break;
            case THIRD:
                TimeUtils.sleep(2500);
        }
        stop();
        setAltitude(ALTITUDE_ZERO);
        setAzimuth(AZIMUTH_ZERO);
    }
}
