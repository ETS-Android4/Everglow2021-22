package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class TotemSystem {
    public static final double THIRD_FLOOR_SIDEWAYS_DISTANCE = 18;

    private final LinearOpMode opMode;
    public static final double AZIMUTH_START = 0.10;
    public static final double ALTITUDE_START = 1;
    public static final double AZIMUTH_ZERO = 0.2;
    public static final double ALTITUDE_ZERO = 0.9;
    private static final double TOTEM_ALTITUDE_INCREASE = 0;
    public Servo azimuth;
    public Servo altitude;
    public CRServo meter;
    DrivingSystem drivingSystem;

    public TotemSystem(LinearOpMode opMode, boolean startOpMode) {
        this.opMode = opMode;
        azimuth = opMode.hardwareMap.get(Servo.class, "azimuth");
        altitude = opMode.hardwareMap.get(Servo.class, "altitude");
        meter = opMode.hardwareMap.get(CRServo.class, "extend");
        drivingSystem = new DrivingSystem(opMode);
        if (startOpMode) {
            setAzimuth(AZIMUTH_ZERO);
            TimeUtils.sleep(250);
            altitude.setPosition(ALTITUDE_ZERO);
        } else {
            setAzimuth(AZIMUTH_START);
            TimeUtils.sleep(1000);
            setAltitude(ALTITUDE_START);

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
                    setAzimuth(0.13);
                    setAltitude(0.61 + TOTEM_ALTITUDE_INCREASE);
                } else {
                    setAzimuth(0.33);
                    setAltitude(0.59 + TOTEM_ALTITUDE_INCREASE);
                }
                TimeUtils.sleep(150);
                drivingSystem.driveStraight(driveStraightDistanceForFloor(floor,mirror), -0.5);
                break;
            case SECOND:
                if (mirror == 1) {
                    setAzimuth(0.34);
                    setAltitude(0.61 + TOTEM_ALTITUDE_INCREASE);
                } else {
                    setAzimuth(0.144);
                    setAltitude(0.62 + TOTEM_ALTITUDE_INCREASE);
                }
                TimeUtils.sleep(250);
                drivingSystem.driveStraight(driveStraightDistanceForFloor(floor,mirror), -0.5);
                break;
            case THIRD:
                if (mirror == 1) {
                    setAzimuth(0.38);
                    setAltitude(0.61 + TOTEM_ALTITUDE_INCREASE);

                } else {
                    setAzimuth(0.1);
                    setAltitude(0.62 + TOTEM_ALTITUDE_INCREASE);
                }
                TimeUtils.sleep(250);
                drivingSystem.driveSideways(THIRD_FLOOR_SIDEWAYS_DISTANCE, -0.5 * mirror);
                drivingSystem.driveStraight(driveStraightDistanceForFloor(floor,mirror), -0.5);
                break;
        }


        secureTotem();
    }

    private static final boolean RETRACT_TOTEM = false;

    public void secureTotem() {
        setAltitudeSlow(ALTITUDE_ZERO, 200);
        setAzimuthSlow(AZIMUTH_ZERO, 100);

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

    public static double driveStraightDistanceForFloor(ArmSystem.Floors floor,int mirror) {
        switch (floor) {
            case FIRST:
                return 24;
            case SECOND:
                return 24 - MathUtils.isMirrored(mirror);
            case THIRD:
                return 26;
            default:
                throw new IllegalArgumentException("Floor for driveStraightDistanceForFloor must be FIRST, SECOND, or THIRD.");
        }
    }

}
