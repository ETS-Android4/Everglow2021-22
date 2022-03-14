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
    public static final double ALTITUDE_MAX = 0.84;
    private static final double TOTEM_ALTITUDE_INCREASE = -0.045;
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
            setAltitude(ALTITUDE_ZERO);
            TimeUtils.sleep(350);
            setAzimuth(AZIMUTH_ZERO);
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

    public void collectTotem(ArmSystem.Floors floor, int mirror) {
        floor = floor.switchIfMirrored(mirror);
        switch (floor) {
            case FIRST:
                if (mirror == 1) {
                    setAzimuth(0.03);
                } else {
                    setAzimuth(0.18833);
                }
                setAltitude(0.64 + TOTEM_ALTITUDE_INCREASE);
                TimeUtils.sleep(50);
                drivingSystem.driveStraight(driveStraightDistanceForFloor(floor), -0.5);
                break;
            case SECOND:
                if (mirror == 1) {
                    setAzimuth(0.175);
                } else {
                    setAzimuth(0.0745);
                }
                setAltitude(0.64 + TOTEM_ALTITUDE_INCREASE);
                TimeUtils.sleep(50);
                drivingSystem.driveStraight(driveStraightDistanceForFloor(floor), -0.5);
                break;
            case THIRD:
                if (mirror == 1) {
                    setAzimuth(0.23);
                } else {
                    setAzimuth(0.01832);
                }
                setAltitude(0.65 + TOTEM_ALTITUDE_INCREASE);
                TimeUtils.sleep(100);
                drivingSystem.driveSideways(THIRD_FLOOR_SIDEWAYS_DISTANCE, -0.5 * mirror);
                drivingSystem.driveStraight(driveStraightDistanceForFloor(floor), -0.5);
                break;
        }
        secureTotem();
    }

    private static final boolean RETRACT_TOTEM = false;

    public void secureTotem() {
        setAltitudeSlow(0.7 + TOTEM_ALTITUDE_INCREASE, 200);
        setAzimuthSlow(AZIMUTH_ZERO, 500);
        setAltitudeSlow(ALTITUDE_MAX, 200);

        if (RETRACT_TOTEM) {
            new Thread(() -> {
                extend(-0.7);
                TimeUtils.sleep(1500);
                stop();
            }).start();
        }
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

    public static double driveStraightDistanceForFloor(ArmSystem.Floors floor) {
        switch (floor) {
            case FIRST:
                return 13;
            case SECOND:
                return 14;
            case THIRD:
                return 15;
            default:
                throw new IllegalArgumentException("Floor for driveStraightDistanceForFloor must be FIRST, SECOND, or THIRD.");
        }
    }

}
