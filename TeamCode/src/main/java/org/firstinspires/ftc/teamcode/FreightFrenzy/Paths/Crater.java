package org.firstinspires.ftc.teamcode.FreightFrenzy.Paths;

import static org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem.THIRD_FLOOR_SIDEWAYS_DISTANCE;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AllSystems;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class Crater {
    DrivingSystem drivingSystem;
    ArmSystem armSystem;
    TotemSystem totemSystem;
    DuckSystem duckSystem;
    public DetectionSystem detectionSystem;
    LinearOpMode opMode;
    ArmSystem.Floors floor;
    private final SharedPaths sharedPaths;


    public Crater(LinearOpMode opMode) {
        this.opMode = opMode;
        drivingSystem = new DrivingSystem(opMode);
        armSystem = new ArmSystem(opMode);
        detectionSystem = new DetectionSystem(opMode, armSystem);
        duckSystem = new DuckSystem(opMode);
        this.totemSystem = new TotemSystem(opMode, false);
        this.sharedPaths = new SharedPaths(new AllSystems(opMode, armSystem, detectionSystem, drivingSystem, duckSystem, totemSystem));
    }

    public Crater(AllSystems systems) {
        this.opMode = systems.opMode;
        this.drivingSystem = systems.drivingSystem;
        this.armSystem = systems.armSystem;
        this.detectionSystem = systems.detectionSystem;
        this.duckSystem = systems.duckSystem;
        this.sharedPaths = new SharedPaths(systems);
    }

    /**
     * Goes to alliance shipping hub and places the loaded freight there.
     */
    public void placeFreight(int mirror) {
        drivingSystem.resetDistance();
        drivingSystem.driveStraight(25, 0.6);
        floor = detectionSystem.findTargetFloor2(mirror);
        drivingSystem.driveStraight(25, -0.6);

        // avoid totem
        if (mirror == 1) {
            switch (floor) {
                case FIRST:
                    drivingSystem.driveSideways(12, 0.6);
                    drivingSystem.driveStraight(95, 0.6);
                    drivingSystem.driveSideways(11, -0.6);
                    TimeUtils.sleep(200);
                    drivingSystem.turn(90, 200);
                    drivingSystem.driveStraight(3, -0.6);
                    break;
                case SECOND:
                    drivingSystem.driveStraight(5, 0.5);
                    TimeUtils.sleep(200);
                    drivingSystem.turn(30, 100);
                    drivingSystem.driveStraight(51, 0.5);
                    TimeUtils.sleep(200);
                    drivingSystem.turn(-60, 100);
                    drivingSystem.driveStraight(50, 0.5);
                    TimeUtils.sleep(200);
                    drivingSystem.turn(120, 100);
                    break;
                case THIRD:
                    drivingSystem.driveStraight(95, 0.6);
                    drivingSystem.driveSideways(4, 0.6);
                    TimeUtils.sleep(200);
                    drivingSystem.turn(90, 200);
                    break;
            }
        } else {
            switch (floor) {
                case THIRD:
                    drivingSystem.driveSideways(12, -0.6);
                    drivingSystem.driveStraight(95, 0.6);
                    drivingSystem.driveSideways(11, 0.6);
                    TimeUtils.sleep(200);
                    drivingSystem.turn(-90, 200);
                    drivingSystem.driveStraight(3, -0.6);
                    break;
                case SECOND:
                    drivingSystem.driveStraight(5, 0.5);
                    TimeUtils.sleep(200);
                    drivingSystem.turn(-30, 100);
                    drivingSystem.driveStraight(51, 0.5);
                    TimeUtils.sleep(200);
                    drivingSystem.turn(60, 100);
                    drivingSystem.driveStraight(50, 0.5);
                    TimeUtils.sleep(200);
                    drivingSystem.turn(-120, 100);
                    break;
                case FIRST:
                    drivingSystem.driveStraight(95, 0.6);
                    drivingSystem.driveSideways(4, -0.6);
                    TimeUtils.sleep(200);
                    drivingSystem.turn(90 * mirror, 200);
                    break;
            }
        }

        // place freight on SH
        armSystem.autonomousMoveArm(floor);
        TimeUtils.sleep(500);
        drivingSystem.driveStraight(7, 0.6);
        armSystem.spit();
        TimeUtils.sleep(1000);
        armSystem.stop();
        drivingSystem.driveStraight(7, -0.6);
        armSystem.autonomousReload();
    }

    public void DetectAndCollectTotem(int mirror) {
        floor = detectionSystem.findTargetFloor2(mirror);
        totemSystem.collectTotem(floor, mirror);
    }

    /**
     * After placing the freight on the SH, move to the carousel from behind the SH.
     *
     * @param mirror 1 is red side, -1 is blue side.
     */
    public void goToCarouselB(int mirror) {
        drivingSystem.driveSideways(50, 0.7 * mirror);
        drivingSystem.driveStraight(180, 0.7);
        drivingSystem.driveSideways(110, -0.7 * mirror);
        drivingSystem.driveSideways(43, -0.7 * mirror);
    }

    /**
     * After placing the freight on the SH, move in front of the Shipping Element.
     *
     * @param mirror 1 is red side, -1 is blue side.
     */
    public void dodgeToFront(int mirror) {
        armSystem.autonomousMoveArm(ArmSystem.Floors.SECOND);
        switch (floor) {
            case FIRST:
                drivingSystem.driveStraight(3, -0.6);
                drivingSystem.driveSideways(77, -0.6 * mirror);
                armSystem.autonomousReload();
                drivingSystem.driveStraight(3, 0.6);
                break;
            case SECOND:
                drivingSystem.turn(90 * mirror, 50);
                drivingSystem.driveSideways(18, 0.6 * mirror);
                drivingSystem.driveStraight(65, 0.6);
                drivingSystem.turn(-90 * mirror, 50);
                drivingSystem.driveStraight(15, -0.6);
                break;
            case THIRD:
                drivingSystem.driveStraight(3, 0.6);
                drivingSystem.driveSideways(65, -0.6 * mirror);
                break;
        }
        drivingSystem.driveSideways(5, 0.6 * mirror);
        armSystem.autonomousReload();
    }

    private void RZNCXLoop(int i, int mirror) {
        double extraDistance = 2;
        double initialDistance = 65 + 10 * i;
        drivingSystem.driveStraight(initialDistance, 0.5, false);
        double[] distance = drivingSystem.driveUntilCollect(100, 0.2);
        TimeUtils.sleep(200);
        armSystem.moveArm(ArmSystem.Floors.THIRD);
        drivingSystem.driveToPoint(distance[0] * mirror, 15 + distance[1], 90 * mirror, 0.5, 0.5);
        drivingSystem.driveStraight(initialDistance + extraDistance, -0.5);
        drivingSystem.driveToPoint(0 * mirror, -75, 60 * mirror - i * 2, 0.5, 0.5);
        TimeUtils.sleep(300);
        armSystem.spit();
        TimeUtils.sleep(600);
        armSystem.stop();
        armSystem.moveArm(0);
        drivingSystem.driveToPoint(0 * mirror, 80, 90 * mirror, 0.5, 0.5);
        drivingSystem.driveStraight(extraDistance, 0.5);
    }

    public void RZNCDeploy(ArmSystem.Floors floor, boolean toPoint, int mirror) {
        if (floor.switchIfMirrored(mirror) == ArmSystem.Floors.THIRD) {
            drivingSystem.driveSideways(THIRD_FLOOR_SIDEWAYS_DISTANCE, 0.5 * mirror);
        }

        if (floor.switchIfMirrored(mirror) == ArmSystem.Floors.FIRST) {
            // because the the totem system blocks the armSystem, we can't use the autonomousPlaceFreight, so we turn 180 degrees adn use placeFreight instead.
            armSystem.autonomousMoveArm(floor);
            drivingSystem.driveToPoint(10 * mirror, -50, -90 * mirror, 0.5, 0.7);
            armSystem.awaitArmArrival();
            TimeUtils.sleep(50);
            armSystem.spit();
            TimeUtils.sleep(300);
            if (toPoint) {
                drivingSystem.driveToPoint(20 * mirror, 65, 90 * mirror, 0.5, 1);
//                drivingSystem.driveSideways(10, 0.6);
            }
        } else {
            armSystem.moveArm(floor);
            TimeUtils.sleep(700);
            drivingSystem.driveToPoint(2 * mirror, -26, 50 * mirror, 0.5, 0.7);
            armSystem.awaitArmArrival();
            TimeUtils.sleep(50);
            armSystem.spit();
            TimeUtils.sleep(300);
            if (toPoint) {
                drivingSystem.driveToPoint(0 * mirror, 65, 90 * mirror, 0.5, 1);
//                drivingSystem.driveSideways(10, 0.6);
            }
        }
        armSystem.moveArm(0);
    }

    public void RZNCX(int mirror) {
        floor = detectionSystem.findTargetFloor2(mirror);
        //collect totem
        totemSystem.collectTotem(floor, mirror);

        RZNCDeploy(floor, true, mirror);

        for (int i = 0; i < 2; i++) {
            RZNCXLoop(i, mirror);
        }
        armSystem.collect();
        drivingSystem.driveStraight(100, 0.5);
    }


    /**
     * Goes to crater. Rams through obstacle.
     */
    public void RZNCO(int mirror) {
        floor = detectionSystem.findTargetFloor2(mirror);
        //collect totem
        totemSystem.collectTotem(floor, mirror);

        RZNCDeploy(floor, false, mirror);
        if (floor.switchIfMirrored(mirror) == ArmSystem.Floors.FIRST) {
            drivingSystem.driveToPoint(0 * mirror, 30, 90 * mirror, 0.5, 0.5);
        } else {
            drivingSystem.turn(40 * mirror, 200);
            drivingSystem.driveSideways(15, 0.6 * mirror);
        }

        armSystem.stop();
        armSystem.moveArm(ArmSystem.Floors.OBSTACLE);
        drivingSystem.driveStraight(120, 1);
    }

    /**
     * Goes to crater. Enters through path.
     */
    public void RZNCP(int mirror) {
        floor = detectionSystem.findTargetFloor2(mirror);
        //collect totem
        totemSystem.collectTotem(floor, mirror);

        RZNCDeploy(floor, true, mirror);

        drivingSystem.driveStraight(100, 0.6);
    }

    /**
     * Goes to carousel behind SH, then to crater. Rams through obstacle.
     */
    public void RBYCO(int mirror) {
        placeFreight(mirror);
        goToCarouselB(mirror);
        TimeUtils.sleep(500);
        duckSystem.runFor(3000);

        drivingSystem.driveSideways(30, 0.7 * mirror);
        drivingSystem.turn(180, 200);
        armSystem.moveArm(-200);
        drivingSystem.driveStraight(280, 1);
    }

    /**
     * Goes to carousel behind SH, then to crater. Enters through path.
     */
    public void RBYCP(int mirror) {
        placeFreight(mirror);
        goToCarouselB(mirror);
        TimeUtils.sleep(500);
        duckSystem.runFor(3000);

        drivingSystem.driveSideways(35, 0.7 * mirror);
        drivingSystem.turn(180, 200);
        drivingSystem.driveStraight(100, 0.7);
        drivingSystem.driveSideways(70, 0.7 * mirror);
        drivingSystem.driveStraight(150, 0.7);
    }

    /**
     * Goes to carousel behind SH, then to warehouse.
     */
    public void RBYW(int mirror) {
        placeFreight(mirror);
        goToCarouselB(mirror);
        TimeUtils.sleep(500);
        duckSystem.runFor(3000);

        drivingSystem.driveSideways(55, 0.6 * mirror);
        drivingSystem.driveStraight(7, 0.6);
    }

    /**
     * Goes to warehouse behind SH.
     */
    public void RBNW(int mirror) {
        placeFreight(mirror);

        drivingSystem.driveSideways(50, 0.6 * mirror);
        drivingSystem.driveStraight(185, 0.6);
        drivingSystem.driveSideways(90, -0.6 * mirror);
    }

    /**
     * Goes to carousel in front of SH, then to warehouse.
     */
    public void RFYW(int mirror) {
        placeFreight(mirror);
        dodgeToFront(mirror);

        drivingSystem.driveStraight(185, 0.6);
        drivingSystem.driveSideways(40, -0.6 * mirror);
        TimeUtils.sleep(500);
        duckSystem.runFor(3000);

        drivingSystem.driveSideways(65, 0.6 * mirror);
        drivingSystem.driveStraight(5, 0.6);
    }

    /**
     * Goes to warehouse in front of SH.
     */
    public void RFNW(int mirror) {
        placeFreight(mirror);
        dodgeToFront(mirror);

        drivingSystem.driveStraight(175, 0.6);
        drivingSystem.driveSideways(40, 0.6 * mirror);
    }

    /**
     * Goes to carousel, then to crater through path.
     */
    public void RFYCO(int mirror) {
        placeFreight(mirror);
        dodgeToFront(mirror);

        drivingSystem.driveStraight(180, 0.6);
        drivingSystem.driveSideways(40, -0.6 * mirror);
        TimeUtils.sleep(500);
        duckSystem.runFor(3000);

        drivingSystem.driveSideways(30, 0.6 * mirror);
        drivingSystem.turn(180, 50);
        armSystem.moveArm(-200);
        drivingSystem.driveStraight(280, 1);
    }

    /**
     * Goes to carousel, then to crater from obstacle.
     */
    public void RFYCP(int mirror) {
        placeFreight(mirror);
        dodgeToFront(mirror);

        drivingSystem.driveStraight(180, 0.6);
        drivingSystem.driveSideways(40, -0.6 * mirror);
        TimeUtils.sleep(500);
        duckSystem.runFor(3000);

        drivingSystem.driveStraight(50, -0.8);
        drivingSystem.turn(180, 200);
        drivingSystem.driveSideways(25, 0.6 * mirror);
        drivingSystem.driveStraight(150, 1);
    }

    public void newCrater(int mirror) {

    }
}
