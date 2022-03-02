package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class TotemSystem {
    private final LinearOpMode opMode;
    public static final double AZIMUTH_ZERO  = 0.095;
    public static final double ALTITUDE_ZERO = 0.765;
    public static final double AZIMUTH_SO_ALTITUDE_CAN_GET_LARGE = 0.42;
    public static final double ALTITUDE_MAX = 0.75;
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
                setAzimuth(0.05);
                setAltitude(0.62);
                extend(0.5);
                TimeUtils.sleep(3000);
                break;
            case SECOND:
                setAzimuth(0.148);
                setAltitude(0.618);
                extend(0.5);
                TimeUtils.sleep(2500);
                break;
            case THIRD:
                setAzimuth(0.223);
                setAltitude(0.625);
                extend(0.5);
                TimeUtils.sleep(3000);
        }
        stop();
        setAltitude(ALTITUDE_MAX);
        extend(-0.5);
        switch (floor) {
            case SECOND:
                TimeUtils.sleep(2500);
                break;

            case FIRST:
            case THIRD:
                TimeUtils.sleep(3000);
        }
        stop();
        setAzimuth(AZIMUTH_SO_ALTITUDE_CAN_GET_LARGE);
        TimeUtils.sleep(800);
    }
}
