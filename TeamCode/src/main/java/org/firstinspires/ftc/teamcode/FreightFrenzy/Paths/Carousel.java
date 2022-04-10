package org.firstinspires.ftc.teamcode.FreightFrenzy.Paths;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.AllSystems;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.CameraSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class Carousel {
    DrivingSystem drivingSystem;
    ArmSystem armSystem;
    DuckSystem duckSystem;
    TotemSystem totemSystem;
    LinearOpMode opMode;
    ElapsedTime timer;
    ArmSystem.Floors floor;
    private final CameraSystem cameraSystem;

    public Carousel(LinearOpMode opMode) {
        this.opMode = opMode;
        drivingSystem = new DrivingSystem(opMode);
        armSystem = new ArmSystem(opMode);
        duckSystem = new DuckSystem(opMode);
        totemSystem = new TotemSystem(opMode, false);
        cameraSystem = new CameraSystem(opMode, MathUtils.Side.RED, null);
        timer = new ElapsedTime();
    }

    public Carousel(AllSystems systems) {
        this.opMode = systems.opMode;
        this.drivingSystem = systems.drivingSystem;
        this.armSystem = systems.armSystem;
        this.duckSystem = systems.duckSystem;
        this.cameraSystem = systems.cameraSystem;
        timer = new ElapsedTime();
    }

    /**
     * Goes to alliance shipping hub and places the loaded freight there.
     */
    public void placeFreight(int mirror) {
        drivingSystem.resetDistance();
        drivingSystem.driveStraight(25, -0.6);
        floor = ArmSystem.Floors.THIRD;
        drivingSystem.turn(180, 300);

        //avoid totem
        if (mirror == 1) {
            switch (floor) {
                case FIRST:
                    drivingSystem.driveSideways(15, 0.6 * mirror);
                    break;
                case SECOND:
                    drivingSystem.driveSideways(25, -0.6 * mirror);
                    break;
            }
        } else {
            switch (floor) {
                case THIRD:
                    drivingSystem.driveSideways(15, -0.6 * mirror);
                    break;
                case SECOND:
                    drivingSystem.driveSideways(25, 0.6 * mirror);
                    break;
            }
        }
        // move to SH
        drivingSystem.driveStraight(65, 0.6);
        if (mirror == 1) {
            switch (floor) {
                case FIRST:
                    drivingSystem.driveSideways(15, -0.6 * mirror);
                    break;
                case SECOND:
                    drivingSystem.driveSideways(25, 0.6 * mirror);
                    break;
            }
        } else {
            switch (floor) {
                case THIRD:
                    drivingSystem.driveSideways(15, 0.6 * mirror);
                    break;
                case SECOND:
                    drivingSystem.driveSideways(25, -0.6* mirror);
                    break;
            }
        }
        drivingSystem.turn(-90 * mirror, 200);

        // place freight on SH
        armSystem.autonomousMoveArm(floor);
        TimeUtils.sleep(500);
        drivingSystem.driveStraight(20, 0.6);
        armSystem.spit();
        TimeUtils.sleep(500);
        armSystem.stop();
        drivingSystem.driveStraight(20, -0.6);
        armSystem.autonomousReload();
    }

//    public void placeFreightAndCollectTotem(int mirror) {
//        drivingSystem.resetDistance();
//        totemSystem.moveAltitude(0.01);
//        floor = detectionSystem.findTargetFloor2(mirror);
//        switch (floor) {
//            case FIRST:
//                totemSystem.moveAzimuth(-0.04);
//                totemSystem.extend(0.5);
//                TimeUtils.sleep(2000);
//                break;
//            case SECOND:
//                totemSystem.moveAzimuth(0.05);
//                totemSystem.extend(0.5);
//                TimeUtils.sleep(2000);
//                break;
//            case THIRD:
//                totemSystem.moveAzimuth(0.145);
//                totemSystem.extend(0.5);
//                TimeUtils.sleep(2500);
//        }
//        totemSystem.stop();
//        totemSystem.setAltitude(TotemSystem.ALTITUDE_ZERO);
//        totemSystem.setAzimuth(TotemSystem.AZIMUTH_SO_ALTITUDE_CAN_GET_LARGE);
//        totemSystem.moveAltitude(0.1);
//        totemSystem.extend(-0.5);
//        switch (floor) {
//            case FIRST:
//            case SECOND:
//                TimeUtils.sleep(2000);
//                break;
//            case THIRD:
//                TimeUtils.sleep(2500);
//        }
//        totemSystem.stop();
//        totemSystem.setAltitude(TotemSystem.ALTITUDE_START);
//        totemSystem.setAzimuth(TotemSystem.AZIMUTH_START);

//        armSystem.autonomousMoveArm(floor);
//        drivingSystem.driveStraight(65, 0.6);
//        drivingSystem.turn(-90 * mirror, 200);
//        drivingSystem.driveStraight(20, 0.6);
//        armSystem.spit();
//        TimeUtils.sleep(500);
//        armSystem.stop();
//        drivingSystem.driveStraight(20, -0.6);
//        armSystem.autonomousReload();
//    }

    static final boolean USE_DETECTION = true;

    public void newPlaceFreightAndCollectTotem(int mirror) {
        drivingSystem.resetDistance();
        if (USE_DETECTION) {
            totemSystem.prePickupMove(mirror);
            floor = ArmSystem.Floors.FIRST;
        } else {
            floor = ArmSystem.Floors.FIRST;
        }
        opMode.telemetry.update();
        totemSystem.collectTotem(floor, mirror);

        drivingSystem.driveStraight(86-TotemSystem.driveStraightDistanceForFloor(floor.switchIfMirrored(mirror),mirror), -0.5);
        armSystem.autonomousMoveArm(floor);
        drivingSystem.turn(90 * mirror, 200);
        drivingSystem.driveStraight(20, 0.5);
        armSystem.awaitArmArrival();
        TimeUtils.sleep(50);
        armSystem.spit();
        TimeUtils.sleep(300);
        armSystem.stop();
        drivingSystem.driveStraight(20, -0.5);
        armSystem.autonomousReload();
    }

    public void newPlaceFreightAndCaursel(int mirror) {
        newPlaceFreightAndCollectTotem(mirror);
        drivingSystem.turn(-90 * mirror, 100);
        drivingSystem.driveSidewaysUntilBumping(0.5 * mirror, 20);
        drivingSystem.driveStraightUntilBumping(0.3, 20);
        TimeUtils.sleep(250);
        duckSystem.runFor(3000);
    }

    public void placeFreightAndArmCarousel(int mirror) {
        newPlaceFreightAndCollectTotem(mirror);
        drivingSystem.turn(90 * mirror, 100);
        drivingSystem.driveSidewaysUntilBumping(-0.5 * mirror, 20);
        armSystem.moveArm(ArmSystem.Floors.SECOND);
        drivingSystem.driveStraightUntilBumping(-0.3, 20);
        TimeUtils.sleep(250);
//        duckSystem.runFor(3000);
        armSystem.spit();
        TimeUtils.sleep(3000);
        armSystem.stop();
        armSystem.autonomousReload();
    }

    public void LZYW(int mirror) {
        newPlaceFreightAndCaursel(mirror);
        drivingSystem.driveSideways(20, -0.5 * mirror);
        drivingSystem.driveStraight(40, -0.5);
        drivingSystem.turn(-90 * mirror, 200);
        drivingSystem.driveStraight(25, 0.5);
    }

    public void LZYWArm(int mirror) {
        placeFreightAndArmCarousel(mirror);
        drivingSystem.driveSideways(20, 0.5 * mirror);
        drivingSystem.driveStraight(40, 0.5);
        drivingSystem.turn(90 * mirror, 200);
        drivingSystem.driveStraight(25, 0.5);
    }

    /**
     * After placing the freight on the SH, move in front of the Shipping Element.
     *
     * @param mirror 1 is red side, -1 is blue side.
     */
    public void dodgeToFront(int mirror) {
        switch (floor) {
            case FIRST:
                drivingSystem.driveSideways(65, 0.6 * mirror);
                break;
            case SECOND:
                drivingSystem.driveStraight(50, -0.6);
                drivingSystem.driveSideways(65, 0.6 * mirror);
                drivingSystem.driveStraight(50, 0.6);
                break;
            case THIRD:
                drivingSystem.driveStraight(10, -0.6);
                drivingSystem.driveSideways(65, 0.6 * mirror);
                drivingSystem.driveStraight(10, 0.6);
                break;
        }
    }

    /**
     * When going from behind the SH to the front of the right Shipping Element,
     * dodge the Shipping Element.
     *
     * @param mirror 1 is red side, -1 is blue side.
     */
    public void dodgeOtherTotem(int mirror) {
        // kept to not break old code that needs to be deleted.
    }

    /**
     * After placing the freight on the SH, move to the Carousel.
     *
     * @param mirror 1 is red side, -1 is blue side.
     */
    public void goToCarousel(int mirror) {
        drivingSystem.driveStraight(35, -0.6);
        drivingSystem.turn(180, 200);
        drivingSystem.driveSideways(110, -0.6 * mirror);
    }

    public void newGoToCarousel(int mirror) {
        drivingSystem.driveStraight(35, 0.6);
        drivingSystem.turn(90 * mirror, 200);
        drivingSystem.driveSideways(110, 0.6 * mirror);
    }

    /**
     * Goes to Carousel, then to Crater behind SH. Rams through obstacle.
     */
    public void LBNCO(int mirror) {
        placeFreight(mirror);

        // Go to the right of the shipping hub and dodge
        drivingSystem.driveSideways(50, -0.6 * mirror);
        drivingSystem.driveStraight(125, 0.6);
        drivingSystem.driveSideways(45, 0.6 * mirror);
        drivingSystem.turn(180, 200);
        dodgeOtherTotem(mirror);

        // Ram through obstacle
        drivingSystem.turn(180, 200);
        armSystem.moveArm(-200);
        drivingSystem.driveStraight(30, -0.6);
        drivingSystem.driveStraight(150, 1);
    }

    /**
     * Goes to Carousel, then to Crater behind SH. Goes through path.
     */
    public void LBNCP(int mirror) {
        placeFreight(mirror);

        // Go to right of the shipping hub and dodge
        drivingSystem.driveSideways(50, -0.6 * mirror);
        drivingSystem.driveStraight(125, 0.6);
        drivingSystem.driveSideways(45, 0.6 * mirror);
        drivingSystem.turn(180, 200);
        dodgeOtherTotem(mirror);

        // Go through path
        drivingSystem.turn(180, 200);
        drivingSystem.driveSideways(50, 0.6 * mirror);
        drivingSystem.driveStraight(100, 0.6);
    }

    /**
     * Goes to Storage Unit.
     */
    public void LZNW(int mirror) {
        placeFreight(mirror);
        drivingSystem.driveStraight(55, -0.6);
        drivingSystem.driveSideways(30, 0.6 * mirror);
    }

    /**
     * Goes to Carousel, and then to Crater in front of SH. Enters Crater through path.
     */
    public void LFYCP(int mirror) {
        newPlaceFreightAndCaursel(mirror);
        // Go to Crater through path
        drivingSystem.driveSideways(50, -0.6 * mirror);
        drivingSystem.turn(90 * mirror, 150);
        drivingSystem.driveSidewaysUntilBumping(0.6 * mirror, 10);
        drivingSystem.driveStraight(230, 0.7);
    }

    /**
     * Goes to Carousel, and then to Crater in front of SH. Rams through obstacle.
     */
    public void LFYCO(int mirror) {
        newPlaceFreightAndCaursel(mirror);

        // Ram through obstacle
        drivingSystem.driveSideways(30, -0.6 * mirror);
        drivingSystem.driveStraight(20, -0.6);
        drivingSystem.turn(90 * mirror, 150);
        armSystem.moveArm(ArmSystem.Floors.OBSTACLE);
        drivingSystem.driveStraight(300, 1);
    }

    public void LFYCX(int mirror){
        newPlaceFreightAndCaursel(mirror);
        // Go to Crater through path
        drivingSystem.driveSideways(30, -0.6 * mirror);
        drivingSystem.driveStraight(20, -0.6);
        drivingSystem.turn(90 * mirror, 150);
        drivingSystem.driveStraight(300, 1);
        armSystem.collect();
        drivingSystem.driveStraight(100, 0.8);
    }

    /**
     * Goes to Carousel, and then to Crater behind SH. Enters Crater through path.
     */
    public void LBYCP(int mirror) {
        newPlaceFreightAndCaursel(mirror);
        // Go to Crater through path
        drivingSystem.driveSideways(50, -0.7 * mirror);
        drivingSystem.turn(90 * mirror, 150);
        drivingSystem.driveSideways(147, -0.7 * mirror);
        drivingSystem.driveStraight(140, 0.7);
        drivingSystem.driveSidewaysUntilBumping(0.7 * mirror, 20);
        drivingSystem.driveStraight(80, 0.7);
    }

    /**
     * Goes to Carousel, and then to Crater behind SH. Rams through obstacle.
     */
    public void LBYCO(int mirror) {
        newPlaceFreightAndCaursel(mirror);

        // Ram through obstacle
        drivingSystem.driveSideways(50, -0.7 * mirror);
        drivingSystem.turn(90 * mirror, 150);
        armSystem.moveArm(ArmSystem.Floors.OBSTACLE);
        drivingSystem.driveSideways(147, -0.7 * mirror);
        drivingSystem.driveStraight(140, 0.7);
        drivingSystem.driveSideways(120, 0.7 * mirror);
        drivingSystem.driveStraight(120, 1);
    }
}
