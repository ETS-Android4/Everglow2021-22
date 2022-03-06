package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class TotemSystem {
    public static final double THIRD_FLOOR_SIDEWAYS_DISTANCE = 14;

    private final LinearOpMode opMode;
    public static final double AZIMUTH_ZERO = 0.095;
    public static final double ALTITUDE_ZERO = 0.765;
    public static final double AZIMUTH_SO_ALTITUDE_CAN_GET_LARGE = 0.43;
    public static final double ALTITUDE_MAX = 0.85;
    public Servo azimuth;
    public Servo altitude;
    public CRServo meter;
    DrivingSystem drivingSystem;

    public TotemSystem(LinearOpMode opMode, boolean startCollected) {
        this.opMode = opMode;
        azimuth = opMode.hardwareMap.get(Servo.class, "azimuth");
        altitude = opMode.hardwareMap.get(Servo.class, "altitude");
        meter = opMode.hardwareMap.get(CRServo.class, "extend");
        drivingSystem = new DrivingSystem(opMode);
        if (startCollected) {
            azimuth.setPosition(AZIMUTH_SO_ALTITUDE_CAN_GET_LARGE);
            altitude.setPosition(ALTITUDE_MAX);
        } else {
            azimuth.setPosition(AZIMUTH_ZERO);
            altitude.setPosition(ALTITUDE_ZERO);
        }
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

    public void collectTotem(ArmSystem.Floors floor) {
        switch (floor) {
            case FIRST:
                setAzimuth(0.04);
                setAltitude(0.63);
                TimeUtils.sleep(50);
                drivingSystem.driveStraight(12, -0.5);
                break;
            case SECOND:
                setAzimuth(0.175);
                setAltitude(0.628);
                TimeUtils.sleep(50);
                drivingSystem.driveStraight(13, -0.5);
                break;
            case THIRD:
                setAzimuth(0.22);
                setAltitude(0.628);
                TimeUtils.sleep(100);
                drivingSystem.driveSideways(THIRD_FLOOR_SIDEWAYS_DISTANCE, -0.5);
                drivingSystem.driveStraight(13, -0.5);
                break;
        }
        new Thread(()->{
            secureTotem(floor);
        }).start();
        TimeUtils.sleep(200);
    }

    public void secureTotem(ArmSystem.Floors floor) {
        setAltitudeSlow(0.7, 200);
        setAzimuthSlow(AZIMUTH_SO_ALTITUDE_CAN_GET_LARGE, 600);
        setAltitudeSlow(ALTITUDE_MAX + 0.05, 500);
        extend(-0.7);
        TimeUtils.sleep(1000);
        stop();
    }

    public void setAzimuthSlow(double pos, long time) {
        moveServoSlow(azimuth, pos, time);
    }

    public void setAltitudeSlow(double pos, long time) {
        moveServoSlow(altitude, pos, time);
    }

    private static void moveServoSlow(Servo servo, double targetPos, long time) {
        final int NUM_STEPS = 100;
        double startPos = servo.getPosition();
        for (int i = 0; i < NUM_STEPS; i++) {
            double partComplete = ((double) i) / NUM_STEPS;
            double currentPos = startPos * (1 - partComplete) + targetPos * partComplete;
            servo.setPosition(currentPos);
            TimeUtils.sleep(time / NUM_STEPS);
        }
    }

}
