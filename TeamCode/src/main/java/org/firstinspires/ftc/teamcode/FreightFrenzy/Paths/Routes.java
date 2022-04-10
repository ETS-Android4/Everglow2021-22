package org.firstinspires.ftc.teamcode.FreightFrenzy.Paths;

import static org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem.driveStraightDistanceForFloor;
import static org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils.isMirrored;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.AllSystems;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class Routes {
    private final AllSystems systems;
    private final int mirror;
    private ArmSystem.Floors floor;

    private static final double INITIAL_DRIVE_STRAIGHT_DISTANCE = 30;

    /**
     * At the start of the autonomous period, we need to take drive straight to pick up the totem.
     * This varries based on the floor we are going picking up and the floor we are going to.
     *
     * @param floor  The floor we need to go to in order to pick up the Totem. Should be switched if mirrored, call switchIfMirrored(mirror)
     * @param mirror 1 if mirrored, -1 if not
     * @return the distance, in centimeters, the robot should drive to pick up the totem.
     */
    private static double driveStraightDistanceForTotemPickup(ArmSystem.Floors floor, int mirror) {
        switch (floor) {
            case FIRST:
            case SECOND:
                return 19;
            case THIRD:
                return 19 + 6 * MathUtils.isMirrored(mirror);
            default:
                throw new IllegalArgumentException("Floor for driveStraightDistanceForFloor must be FIRST, SECOND, or THIRD.");
        }
    }

    public Routes(AllSystems systems) {
        this.systems = systems;
        this.mirror = systems.side.mirror;
    }

    /**
     * Picks up the totem and drives INITIAL_DRIVE_STRAIGHT_DISTANCE centimeters forward.
     */
    private void pickupTotem() {
        systems.opMode.telemetry.addLine("Detecting Totem...");
        systems.opMode.telemetry.update();
        ElapsedTime elapsedTime = new ElapsedTime();
        floor = systems.cameraSystem.detectTotem();
        systems.opMode.telemetry.addData("Floor: ", floor);
        systems.opMode.telemetry.addData("Camera Time: ", elapsedTime.seconds());
        systems.opMode.telemetry.update();

        double driveStraightDistanceForTotem = driveStraightDistanceForTotemPickup(floor.switchIfMirrored(mirror), mirror);
        systems.drivingSystem.driveStraight(driveStraightDistanceForTotem, -0.6);
        // pick up totem
        systems.drivingSystem.driveStraight(INITIAL_DRIVE_STRAIGHT_DISTANCE - driveStraightDistanceForTotem, -0.6);
    }

    public void craterPlaceFreight(boolean toPoint) {
        pickupTotem();
        if (floor == ArmSystem.Floors.FIRST) {
            // because the the totem system blocks the armSystem, we can't use the autonomousPlaceFreight, so we turn 180 degrees and use placeFreight instead.
            systems.armSystem.autonomousMoveArm(floor);
            systems.drivingSystem.driveToPoint(5 * mirror, -60 + driveStraightDistanceForFloor(floor.switchIfMirrored(mirror), mirror), -90 * mirror, 0.5, 0.7);
            systems.armSystem.awaitArmArrival();
            TimeUtils.sleep(50);
            systems.armSystem.spit();
            TimeUtils.sleep(300);
            if (toPoint) {
                systems.drivingSystem.driveToPoint((20 - 25 * isMirrored(mirror)) * mirror, 65, 90 * mirror, 0.5, 1);
                if (floor != ArmSystem.Floors.FIRST) {
                    systems.drivingSystem.driveStraight(8, 0.6);
                }
                systems.armSystem.moveArm(0);
            }
        } else {
            systems.armSystem.moveArm(floor);
            TimeUtils.sleep(700);
            systems.drivingSystem.driveToPoint(2 * mirror, -37 + driveStraightDistanceForFloor(floor.switchIfMirrored(mirror), mirror), 50 * mirror, 0.5, 0.7);
            systems.armSystem.awaitArmArrival();
            TimeUtils.sleep(50);
            systems.armSystem.spit();
            TimeUtils.sleep(300);
            systems.armSystem.autonomousReload();
            if (toPoint) {
                systems.drivingSystem.driveToPoint(0, 70, 90 * mirror, 0.5, 1);
            }
        }
    }

    private void goToCarouselB() {
        //
    }

    private void RZNCXLoop(int i) {
        systems.drivingSystem.driveUntilWhite(0.6, false);
        systems.drivingSystem.driveStraight(i * 10, 0.7, false);
        double distance = systems.drivingSystem.driveUntilCollect(200, 0.3);
        systems.drivingSystem.driveToPoint((distance / 2) * mirror, 15, -90 * mirror, 0.9, 1);
        systems.drivingSystem.driveUntilWhite(-0.6, false);
        systems.drivingSystem.driveStraight(50, -0.9, false);
        systems.armSystem.moveArm(ArmSystem.Floors.THIRD);
        systems.drivingSystem.driveToPoint(15 * mirror, -55, -45 * mirror - 5 * isMirrored(mirror), 0.9, 1);
        systems.armSystem.spit();
        TimeUtils.sleep(200);
        systems.armSystem.moveArm(0);
        systems.drivingSystem.driveToPoint(-20 * mirror, 55, -90 * mirror, 0.9, 1);
    }


    public void RZNCX() {
//        craterPlaceFreight(true);
        systems.armSystem.moveArm(ArmSystem.Floors.THIRD);
        systems.drivingSystem.driveToPoint(10 * mirror, -47, -45 * mirror, 0.9, 1);
        systems.armSystem.spit();
        TimeUtils.sleep(200);
        systems.armSystem.moveArm(0);
        systems.drivingSystem.driveToPoint(-20 * mirror, 60, -90 * mirror, 0.9, 1);
        for (int i = 0; i < 3; i++) {
            RZNCXLoop(i);
        }
        systems.drivingSystem.driveUntilWhite(0.9, false);
        systems.drivingSystem.driveStraight(15, 0.9);
    }

    /**
     * Goes to carousel behind SH, then to crater. Rams through obstacle.
     */
    public void RBYCO(int mirror) {
        craterPlaceFreight(true);
        goToCarouselB();
        TimeUtils.sleep(500);
        systems.duckSystem.runFor(3000);

        systems.drivingSystem.driveSideways(30, 0.7 * mirror);
        systems.drivingSystem.turn(180, 200);
        systems.armSystem.moveArm(-200);
        systems.drivingSystem.driveStraight(280, 1);
    }

    /**
     * Goes to carousel behind SH, then to crater. Enters through path.
     */
    public void RBYCP(int mirror) {
        craterPlaceFreight(true);
        goToCarouselB();
        TimeUtils.sleep(500);
        systems.duckSystem.runFor(3000);

        systems.drivingSystem.driveSideways(35, 0.7 * mirror);
        systems.drivingSystem.turn(180, 200);
        systems.drivingSystem.driveStraight(100, 0.7);
        systems.drivingSystem.driveSideways(70, 0.7 * mirror);
        systems.drivingSystem.driveStraight(150, 0.7);
    }

    /**
     * Goes to carousel behind SH, then to warehouse.
     */
    public void RBYW(int mirror) {
        craterPlaceFreight(true);
        goToCarouselB();
        TimeUtils.sleep(500);
        systems.duckSystem.runFor(3000);

        systems.drivingSystem.driveSideways(55, 0.6 * mirror);
        systems.drivingSystem.driveStraight(7, 0.6);
    }

    /**
     * Goes to warehouse behind SH.
     */
    public void RBNW(int mirror) {
        craterPlaceFreight(true);

        systems.drivingSystem.driveSideways(50, 0.6 * mirror);
        systems.drivingSystem.driveStraight(185, 0.6);
        systems.drivingSystem.driveSideways(90, -0.6 * mirror);
    }

    /**
     * Goes to carousel in front of SH, then to warehouse.
     */
    public void RFYW(int mirror) {
        craterPlaceFreight(true);

        if (floor == ArmSystem.Floors.FIRST) {
            systems.drivingSystem.driveSideways(20, -0.6 * mirror);
        } else {
            systems.drivingSystem.turn(-40, 200);
        }
        systems.drivingSystem.driveStraight(185, 0.6);
        systems.drivingSystem.driveSideways(40, -0.6 * mirror);
        TimeUtils.sleep(500);
        systems.duckSystem.runFor(3000);

        systems.drivingSystem.driveSideways(65, 0.6 * mirror);
        systems.drivingSystem.driveStraight(5, 0.6);
    }

    /**
     * Goes to warehouse in front of SH.
     */
    public void RFNW(int mirror) {
        craterPlaceFreight(true);

        if (floor == ArmSystem.Floors.FIRST) {
            systems.drivingSystem.driveSideways(20, -0.6 * mirror);
        } else {
            systems.drivingSystem.turn(-40, 200);
        }
        systems.drivingSystem.driveStraight(175, 0.6);
        systems.drivingSystem.driveSideways(40, 0.6 * mirror);
    }

    /**
     * Goes to carousel, then to crater through path.
     */
    public void RFYCO(int mirror) {
        craterPlaceFreight(true);

        if (floor == ArmSystem.Floors.FIRST) {
            systems.drivingSystem.driveSideways(20, -0.6 * mirror);
        } else {
            systems.drivingSystem.turn(-40, 200);
        }
        systems.drivingSystem.driveStraight(180, 0.6);
        systems.drivingSystem.driveSideways(40, -0.6 * mirror);
        TimeUtils.sleep(500);
        systems.duckSystem.runFor(3000);

        systems.drivingSystem.driveSideways(30, 0.6 * mirror);
        systems.drivingSystem.turn(180, 50);
        systems.armSystem.moveArm(-200);
        systems.drivingSystem.driveStraight(280, 1);
    }

    /**
     * Goes to carousel, then to crater from obstacle.
     */
    public void RFYCP(int mirror) {
        craterPlaceFreight(true);

        if (floor == ArmSystem.Floors.FIRST) {
            systems.drivingSystem.driveSideways(20, -0.6 * mirror);
        } else {
            systems.drivingSystem.turn(-40, 200);
        }
        systems.drivingSystem.driveStraight(180, 0.6);
        systems.drivingSystem.driveSideways(40, -0.6 * mirror);
        TimeUtils.sleep(500);
        systems.duckSystem.runFor(3000);

        systems.drivingSystem.driveStraight(50, -0.8);
        systems.drivingSystem.turn(180, 200);
        systems.drivingSystem.driveSideways(25, 0.6 * mirror);
        systems.drivingSystem.driveStraight(150, 1);
    }
}
