package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import static org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils.isMirrored;
import static org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils.sleep;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.checkerframework.checker.units.qual.C;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;
import org.opencv.core.Mat;

public class Routes {
    private final AllSystems systems;
    private final int mirror;
    private ArmSystem.Floors floor;

    // the distances the robot drove in the X and Y directions in order to pickup the totem
    private double pickupTotemX = 0;
    private double pickupTotemY = 0;

    public Routes(AllSystems systems) {
        this.systems = systems;
        this.mirror = systems.side.mirror;
    }

    /**
     * Picks up the totem and drives INITIAL_DRIVE_STRAIGHT_DISTANCE centimeters backwards.
     */
    public void pickupTotem() {
        systems.opMode.telemetry.addLine("Detecting Totem...");
        systems.opMode.telemetry.update();
        ElapsedTime elapsedTime = new ElapsedTime();
        floor = systems.cameraSystem.detectTotem();
        systems.opMode.telemetry.addData("Floor: ", floor);
        systems.opMode.telemetry.addData("Camera Time: ", elapsedTime.seconds());
        systems.opMode.telemetry.update();
        systems.totemSystem.setAltitude(TotemSystem.ALTITUDE_PICKUP);
        ArmSystem.Floors floorForPickup = floor.switchIfMirrored(mirror);
        new Thread(()->{
            TimeUtils.sleep(500);
            if (floor == ArmSystem.Floors.THIRD) {
                systems.armSystem.moveArm(ArmSystem.Floors.THIRD);
            } else {
                systems.armSystem.autonomousMoveArm(floor);
            }
        }).start();
        switch (floorForPickup) {
            case FIRST:
//                if (mirror == 1) {
                    pickupTotemX = 14;
                    pickupTotemY = -25;

                    systems.drivingSystem.driveSideways(pickupTotemX*mirror, 0.5);
                    systems.drivingSystem.driveStraight(pickupTotemY, 0.5);
//                }
                break;
            case SECOND:
//                if (mirror == 1) {
                    pickupTotemX = -4;
                    pickupTotemY = -25;
                    systems.drivingSystem.driveSideways(pickupTotemX*mirror, 0.5);
                    systems.drivingSystem.driveStraight(pickupTotemY, 0.5);
//                }
                break;
            case THIRD:
//                if (mirror == 1) {
                    pickupTotemX = -9;
                    pickupTotemY = -25;
                    systems.drivingSystem.driveToPoint(pickupTotemX*mirror, pickupTotemY, 21*mirror, 0.5, 1.2);
//                }
                break;
            default:
                throw new IllegalArgumentException("Floor Must be FIRST, SECOND, or THIRD");
        }
        new Thread(() -> {
            systems.totemSystem.setAltitudeSlowly(TotemSystem.ALTITUDE_AFTER_PICKUP, 500);
            systems.totemSystem.extendLeft(1);
            systems.totemSystem.extendLeft(1);
            TimeUtils.sleep(500);
            systems.totemSystem.stopLeft();
            systems.totemSystem.stopRight();
        }).start();
    }



    private void goToCarouselB() {
        //
    }

    public void craterPlaceFreight(boolean returnToWall) {
        pickupTotem();
        if (floor == ArmSystem.Floors.THIRD) {
            systems.drivingSystem.driveToPoint((10 - pickupTotemX) * mirror, -47 - pickupTotemY, -57 * mirror, 0.5, 1.75);
            systems.armSystem.spitCargo();
            sleep(200);
            systems.armSystem.stop();
            if (returnToWall) {
                systems.armSystem.autonomousReload();
                systems.drivingSystem.driveToPoint(-2 * mirror, 85, -90 * mirror, 0.9, 1);
            }
        } else {
            systems.drivingSystem.driveToPoint((10 - pickupTotemX) * mirror, -40 - pickupTotemY, 135 * mirror, 0.5, 1.75);
            systems.armSystem.spitCargo();
            sleep(200);
            systems.armSystem.stop();
            systems.drivingSystem.turn(-45, 200);
            systems.armSystem.autonomousReload();
            if (returnToWall) {
                systems.drivingSystem.driveToPoint(-2 * mirror, 85, -90 * mirror, 0.9, 2);
            }
        }
    }

    private void RZNCXLoop(int i) {
        systems.drivingSystem.driveUntilWhite(0.6,125, false);
        systems.drivingSystem.driveStraight(i * 10 + 5, 0.9, false);
        double distance = systems.drivingSystem.driveUntilCollect(200, 0.2);
//        systems.drivingSystem.driveToPoint((distance / 2) * mirror, 15, -90 * mirror, 0.9, 1);
        systems.drivingSystem.driveStraight(i * 10, -0.9, false);
        systems.drivingSystem.driveSideways(10*mirror, 0.9, false);
        systems.drivingSystem.driveUntilWhite(-0.6,150,false);
        systems.drivingSystem.driveStraight(35 + 10 * isMirrored(mirror), -0.9, false);
        systems.armSystem.moveArm(ArmSystem.Floors.THIRD);
        systems.drivingSystem.driveToPoint(15 * mirror, -60, -55 * mirror, 0.9, 1);
        systems.armSystem.spit();
        sleep(200);
        systems.armSystem.autonomousReload();
    }


    public void RZNCX() {
        craterPlaceFreight(true);
        for (int i = 0; i < 2; i++) {
            RZNCXLoop(i);
            systems.drivingSystem.driveToPoint(-20 * mirror, 60, -90 * mirror, 0.9, 1);
        }
        RZNCXLoop(3);
        systems.drivingSystem.turn(45*mirror, 150);
        systems.drivingSystem.driveStraight(100, 1);
    }

    /**
     * Goes to carousel behind SH, then to crater. Rams through obstacle.
     */
    public void RBYCO(int mirror) {
        craterPlaceFreight(true);
        goToCarouselB();
        sleep(500);
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
        sleep(500);
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
        sleep(500);
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
        sleep(500);
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
        sleep(500);
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
        sleep(500);
        systems.duckSystem.runFor(3000);

        systems.drivingSystem.driveStraight(50, -0.8);
        systems.drivingSystem.turn(180, 200);
        systems.drivingSystem.driveSideways(25, 0.6 * mirror);
        systems.drivingSystem.driveStraight(150, 1);
    }

    public void LZYW(int mirror) {
//        newPlaceFreightAndCarousel(mirror);
        systems.drivingSystem.driveSideways(20, -0.5 * mirror);
        systems.drivingSystem.driveStraight(40, -0.5);
        systems.drivingSystem.turn(-90 * mirror, 200);
        systems.drivingSystem.driveStraight(25, 0.5);
    }

    /**
     * Goes to Storage Unit.
     */
    public void LZNW(int mirror) {
//        placeFreight(mirror);
        systems.drivingSystem.driveStraight(55, -0.6);
        systems.drivingSystem.driveSideways(30, 0.6 * mirror);
    }

    /**
     * Goes to Carousel, and then to Crater in front of SH. Enters Crater through path.
     */
    public void LFYCP(int mirror) {
//        newPlaceFreightAndCarousel(mirror);
        // Go to Crater through path
        systems.drivingSystem.driveSideways(50, -0.6 * mirror);
        systems.drivingSystem.turn(90 * mirror, 150);
        systems.drivingSystem.driveSidewaysUntilBumping(0.6 * mirror, 10);
        systems.drivingSystem.driveStraight(230, 0.7);
    }

    /**
     * Goes to Carousel, and then to Crater in front of SH. Rams through obstacle.
     */
    public void LFYCO(int mirror) {
//        newPlaceFreightAndCarousel(mirror);

        // Ram through obstacle
        systems.drivingSystem.driveSideways(30, -0.6 * mirror);
        systems.drivingSystem.driveStraight(20, -0.6);
        systems.drivingSystem.turn(90 * mirror, 150);
        systems.armSystem.moveArm(ArmSystem.Floors.OBSTACLE);
        systems.drivingSystem.driveStraight(300, 1);
    }

    /**
     * Goes to Carousel, then to Crater behind SH. Rams through obstacle.
     */
    public void LBNCO(int mirror) {
//        placeFreight(mirror);

        // Go to the right of the shipping hub and dodge
        systems.drivingSystem.driveSideways(50, -0.6 * mirror);
        systems.drivingSystem.driveStraight(125, 0.6);
        systems.drivingSystem.driveSideways(45, 0.6 * mirror);
        systems.drivingSystem.turn(180, 200);
//        dodgeOtherTotem(mirror);

        // Ram through obstacle
        systems.drivingSystem.turn(180, 200);
        systems.armSystem.moveArm(-200);
        systems.drivingSystem.driveStraight(30, -0.6);
        systems.drivingSystem.driveStraight(150, 1);
    }

    /**
     * Goes to Carousel, then to Crater behind SH. Goes through path.
     */
    public void LBNCP(int mirror) {
//        placeFreight(mirror);

        // Go to right of the shipping hub and dodge
        systems.drivingSystem.driveSideways(50, -0.6 * mirror);
        systems.drivingSystem.driveStraight(125, 0.6);
        systems.drivingSystem.driveSideways(45, 0.6 * mirror);
        systems.drivingSystem.turn(180, 200);
//        dodgeOtherTotem(mirror);

        // Go through path
        systems.drivingSystem.turn(180, 200);
        systems.drivingSystem.driveSideways(50, 0.6 * mirror);
        systems.drivingSystem.driveStraight(100, 0.6);
    }
}
