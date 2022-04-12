package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import static org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils.isMirrored;
import static org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils.sleep;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem.Floors;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class Routes {
    public static final int TIME_TO_STOP = 26;
    private final AllSystems systems;
    private final int mirror;
    private Floors floor;

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
    public void pickupTotemBlue(boolean isCrater) {
        systems.opMode.telemetry.addLine("Detecting Totem...");
        systems.opMode.telemetry.update();
        ElapsedTime elapsedTime = new ElapsedTime();
        floor = systems.cameraSystem.detectTotem();
        systems.opMode.telemetry.addData("Floor: ", floor);
        systems.opMode.telemetry.addData("Camera Time: ", elapsedTime.seconds());
        systems.opMode.telemetry.update();
        systems.totemSystem.setAltitude(TotemSystem.ALTITUDE_PICKUP);
        Floors floorForPickup = floor.switchIfMirrored(mirror);
        if (isCrater) {
            new Thread(() -> {
                TimeUtils.sleep(500);
                if (floor == Floors.THIRD) {
                    systems.armSystem.moveArm(Floors.THIRD);
                } else {
                    systems.armSystem.autonomousMoveArm(floor);
                }
            }).start();
        }
        switch (floorForPickup) {
            case FIRST:
                pickupTotemX = 16;
                pickupTotemY = -30;
                systems.drivingSystem.driveSideways(pickupTotemX * mirror, 0.5);
                systems.drivingSystem.driveStraight(pickupTotemY, 0.5);
                break;
            case SECOND:
                pickupTotemX = -4;
                pickupTotemY = -30;
                systems.drivingSystem.driveSideways(pickupTotemX * mirror, 0.5);
                systems.drivingSystem.driveStraight(pickupTotemY, 0.5);
                break;
            case THIRD:
                pickupTotemX = -9;
                pickupTotemY = -(30 - 10 * isMirrored(mirror));
                systems.drivingSystem.driveToPoint((pickupTotemX) * mirror, pickupTotemY, 21 * mirror, 0.5, 1.2);
                // in THIRD floor on blue we need to move a bit more
                if (isCrater) {
                    // if moving the arm, the location sent needs to be different.
                    pickupTotemY -= 7 * isMirrored(mirror);
                    pickupTotemX += 7 * isMirrored(mirror);
                }
                break;
            default:
                throw new IllegalArgumentException("Floor Must be FIRST, SECOND, or THIRD");
        }
        if (isCrater) {
            new Thread(() -> {
                systems.totemSystem.setAltitudeSlowly(TotemSystem.ALTITUDE_AFTER_PICKUP, 500);
            }).start();
        }else {
            TimeUtils.sleep(250);
            systems.totemSystem.setAltitudeSlowly(TotemSystem.ALTITUDE_AFTER_PICKUP, 1000);
        }
    }

    public void pickupTotemRed(boolean isCrater) {
        systems.opMode.telemetry.addLine("Detecting Totem...");
        systems.opMode.telemetry.update();
        ElapsedTime elapsedTime = new ElapsedTime();
        floor = systems.cameraSystem.detectTotem();
        systems.opMode.telemetry.addData("Floor: ", floor);
        systems.opMode.telemetry.addData("Camera Time: ", elapsedTime.seconds());
        systems.opMode.telemetry.update();
        systems.totemSystem.setAltitude(TotemSystem.ALTITUDE_PICKUP);
        Floors floorForPickup = floor.switchIfMirrored(mirror);
        if (isCrater) {
            new Thread(() -> {
                TimeUtils.sleep(500);
                if (floor == Floors.THIRD) {
                    systems.armSystem.moveArm(Floors.THIRD);
                } else {
                    systems.armSystem.autonomousMoveArm(floor);
                }
            }).start();
        }
        switch (floorForPickup) {
            case FIRST:
                pickupTotemX = 17;
                pickupTotemY = -27;
                systems.drivingSystem.driveSideways(pickupTotemX * mirror, 0.5);
                systems.drivingSystem.driveStraight(pickupTotemY, 0.5);
                break;
            case SECOND:
                pickupTotemX = -4;
                pickupTotemY = -27;
                systems.drivingSystem.driveSideways(pickupTotemX * mirror, 0.5);
                systems.drivingSystem.driveStraight(pickupTotemY, 0.5);
                break;
            case THIRD:
                pickupTotemX = -9;
                pickupTotemY = -(25 - 10 * isMirrored(mirror));
                systems.drivingSystem.driveToPoint((pickupTotemX) * mirror, pickupTotemY, 21 * mirror, 0.5, 1.2);
                // in THIRD floor on blue we need to move a bit more
                if (isCrater) {
                    // if moving the arm, the location sent needs to be different.
                    pickupTotemY -= 7 * isMirrored(mirror);
                    pickupTotemX += 7 * isMirrored(mirror);
                }
                break;
            default:
                throw new IllegalArgumentException("Floor Must be FIRST, SECOND, or THIRD");
        }
        if (isCrater) {
            new Thread(() -> {
                systems.totemSystem.setAltitudeSlowly(TotemSystem.ALTITUDE_AFTER_PICKUP, 500);
            }).start();
        }else {
            systems.totemSystem.setAltitudeSlowly(TotemSystem.ALTITUDE_AFTER_PICKUP, 400);
        }
    }

    private void pickupTotem(boolean isCrater){
        if (mirror == 1){
            pickupTotemRed(isCrater);
        }else {
            pickupTotemBlue(isCrater);
        }
    }

    private void goToCarouselB() {
        //
    }

    public void craterPlaceFreightBlue(boolean returnToWall) {
        pickupTotem(true);
        if (floor == Floors.THIRD) {
            systems.drivingSystem.driveToPoint((10 + 5*isMirrored(mirror) - pickupTotemX) * mirror, -47 - 5*isMirrored(mirror) - pickupTotemY, -57 * mirror, 0.5, 1.75);
            systems.armSystem.spitCargo();
            sleep(200);
            systems.armSystem.stop();
            if (returnToWall) {
                systems.armSystem.autonomousReload();
                systems.drivingSystem.driveToPoint(-2 * mirror, 85, -90 * mirror, 0.9, 1);
            }
        } else {
            systems.drivingSystem.driveToPoint((13 - pickupTotemX) * mirror, -43 - pickupTotemY, (135 + 10 * isMirrored(mirror)) * mirror, 0.5, 1.75);
            systems.armSystem.spit();
            sleep(200);
            systems.armSystem.stop();
            systems.drivingSystem.turnAbsolute(0, 50);
            systems.armSystem.autonomousReload();
            if (returnToWall) {
                systems.drivingSystem.driveToPoint(-2 * mirror, 55, -90 * mirror, 0.9, 2);
            }
        }
    }

    private boolean RZNCXLoopBlue(int i, boolean canInterrupt) {
        systems.drivingSystem.driveUntilWhite(0.55, 300, false);
        if (i > 0) {
            systems.drivingSystem.driveStraight(10, 0.9, false);
        }
        double distance = systems.drivingSystem.driveUntilCollect(150, 0.2);
//        systems.drivingSystem.driveToPoint((distance / 2) * mirror, 15, -90 * mirror, 0.9, 1);
        systems.drivingSystem.driveStraight(10, -0.45, false);
        systems.drivingSystem.driveSideways(10 * mirror, 0.45, false);
        systems.drivingSystem.driveUntilWhite(-0.55, 90, false);
        if (canInterrupt && systems.opMode.getRuntime() > TIME_TO_STOP){
            return false;
        }
        systems.drivingSystem.driveStraight(35 + 0 * isMirrored(mirror), -0.9, false);
        systems.armSystem.moveArm(Floors.THIRD);
        systems.drivingSystem.driveToPoint((20) * mirror, -60 - 5 * isMirrored(mirror), (-45 + 0 * isMirrored(mirror)) * mirror, 0.9, 1);
        systems.armSystem.spitWithVelocity(2300);
        sleep(200);
        systems.armSystem.autonomousReload();
        return true;
    }

    public void craterPlaceFreightRed(boolean returnToWall) {
        pickupTotem(true);
        if (floor == Floors.THIRD) {
            systems.drivingSystem.driveToPoint((10 + 5*isMirrored(mirror) - pickupTotemX) * mirror, -47 - 5*isMirrored(mirror) - pickupTotemY, -57 * mirror, 0.5, 1.75);
            systems.armSystem.spitCargo();
            sleep(200);
            systems.armSystem.stop();
            if (returnToWall) {
                systems.armSystem.autonomousReload();
                systems.drivingSystem.driveToPoint(-2 * mirror, 85, -90 * mirror, 0.9, 1);
            }
        } else {
            if (floor == Floors.SECOND) {
                systems.drivingSystem.driveToPoint((13 - pickupTotemX) * mirror, -38 - pickupTotemY, (150 + 10 * isMirrored(mirror)) * mirror, 0.5, 1.75);
            }else {
                systems.drivingSystem.driveToPoint((10 - pickupTotemX) * mirror, -42 - pickupTotemY, (135 + 10 * isMirrored(mirror)) * mirror, 0.5, 1.75);
            }
            systems.armSystem.spit();
            sleep(200);
            systems.armSystem.stop();
            systems.drivingSystem.turnAbsolute(0, 50);
            systems.armSystem.autonomousReload();
            if (returnToWall) {
                systems.drivingSystem.driveToPoint(-2 * mirror, 55, -90 * mirror, 0.9, 2);
            }
        }
    }

    private boolean RZNCXLoopRed(int i, boolean canInterrupt) {
        systems.drivingSystem.driveUntilWhite(0.55, 300, false);
        if (i > 0) {
            systems.drivingSystem.driveStraight(10, 0.9, false);
        }
        double distance = systems.drivingSystem.driveUntilCollect(150, 0.2);
//        systems.drivingSystem.driveToPoint((distance / 2) * mirror, 15, -90 * mirror, 0.9, 1);
        systems.drivingSystem.driveStraight(10, -0.45, false);
        systems.drivingSystem.driveSideways(10 * mirror, 0.45, false);
        systems.drivingSystem.driveUntilWhite(-0.55, 90, false);
        if (canInterrupt && systems.opMode.getRuntime() > TIME_TO_STOP){
            return false;
        }
        systems.drivingSystem.driveStraight(35 + 0 * isMirrored(mirror), -0.9, false);
        systems.armSystem.moveArm(Floors.THIRD);
        systems.drivingSystem.driveToPoint((20) * mirror, -60 - 5 * isMirrored(mirror), (-45 + 0 * isMirrored(mirror)) * mirror, 0.9, 1);
        sleep(200);
        systems.armSystem.spitWithVelocity(2000);
        sleep(200);
        systems.armSystem.autonomousReload();
        return true;
    }


    public void RZNCX() {
        if (mirror == 1){
            craterPlaceFreightRed(true);
            for (int i = 0; i < 2; i++) {
                RZNCXLoopRed(i, false);
                systems.drivingSystem.driveToPoint((-20 + 10 * isMirrored(mirror)) * mirror, 48, -90 * mirror, 0.9, 1);
            }
            if(RZNCXLoopRed(3, false)) {
                systems.drivingSystem.turnAbsolute(80 * mirror, 100);
                systems.drivingSystem.driveStraight(160, 1);
            }else {
                systems.drivingSystem.driveStraight(70, 0.5);
            }
            systems.totemSystem.setAltitude(TotemSystem.ALTITUDE1_MAX);
            TimeUtils.sleep(2000);
        }else {
            craterPlaceFreightBlue(true);
            for (int i = 0; i < 2; i++) {
                RZNCXLoopBlue(i, false);
                systems.drivingSystem.driveToPoint((-20 + 10 * isMirrored(mirror)) * mirror, 55, -90 * mirror, 0.9, 1);
            }
            if(RZNCXLoopBlue(3, true)){
                systems.drivingSystem.turnAbsolute(80 * mirror, 100);
                systems.drivingSystem.driveStraight(160, 1);
            }else {
                systems.drivingSystem.driveStraight(70, 0.5);
            }
            systems.totemSystem.setAltitude(TotemSystem.ALTITUDE1_MAX);
            TimeUtils.sleep(2000);
        }
    }

    /**
     * Goes to carousel behind SH, then to crater. Rams through obstacle.
     */
    public void RBYCO(int mirror) {
        craterPlaceFreightBlue(true);
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
        craterPlaceFreightBlue(true);
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
        craterPlaceFreightBlue(true);
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
        craterPlaceFreightBlue(true);

        systems.drivingSystem.driveSideways(50, 0.6 * mirror);
        systems.drivingSystem.driveStraight(185, 0.6);
        systems.drivingSystem.driveSideways(90, -0.6 * mirror);
    }

    /**
     * Goes to carousel in front of SH, then to warehouse.
     */
    public void RFYW(int mirror) {
        craterPlaceFreightBlue(true);

        if (floor == Floors.FIRST) {
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
        craterPlaceFreightBlue(true);

        if (floor == Floors.FIRST) {
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
        craterPlaceFreightBlue(true);

        if (floor == Floors.FIRST) {
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
        craterPlaceFreightBlue(true);

        if (floor == Floors.FIRST) {
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




    ///////////////////////// LEFT /////////////////////////////



    public void carouselPlaceFreightAndCarousel() {
        carouselPlaceFreight();
        systems.drivingSystem.turn(-90 * mirror, 100);
        systems.drivingSystem.driveSidewaysUntilBumping(0.5 * mirror, 20);
        systems.drivingSystem.driveStraightUntilBumping(0.3, 20);
        TimeUtils.sleep(250);
        systems.duckSystem.runFor(3000);
    }
    public void carouselPlaceFreight() {
        pickupTotemBlue(false);
        systems.drivingSystem.turnAbsolute(0, 150);
        systems.drivingSystem.driveStraight(-77 - pickupTotemY, 0.5);
        systems.armSystem.autonomousMoveArm(floor);
        systems.drivingSystem.turn(90 * mirror, 200);
        systems.drivingSystem.driveStraight(25 + pickupTotemX, 0.5);
        systems.armSystem.awaitArmArrival();
        TimeUtils.sleep(50);
        systems.armSystem.spit();
        TimeUtils.sleep(300);
        systems.armSystem.stop();
        systems.drivingSystem.driveStraight(20, -0.5);
        systems.armSystem.autonomousReload();
    }

    /**
     * Does Carousel, then parks in storage unit.
     */
    public void RZYW() {
        carouselPlaceFreightAndCarousel();
        systems.drivingSystem.driveSideways(20, -0.5 * mirror);
        systems.drivingSystem.driveStraight(40, -0.5);
        systems.drivingSystem.turn(-90 * mirror, 200);
        systems.drivingSystem.driveStraight(25, 0.5);
        systems.totemSystem.setAltitudeSlowly(TotemSystem.ALTITUDE1_MAX, 500);
    }

    /**
     * Goes to Carousel, and then to Crater in front of SH. Enters Crater through path.
     */
    public void LFYCP() {
        carouselPlaceFreightAndCarousel();
        // Go to Crater through path
        systems.drivingSystem.driveSideways(50, -0.6 * mirror);
        systems.drivingSystem.turn(90 * mirror, 150);
        systems.drivingSystem.driveSidewaysUntilBumping(0.6 * mirror, 10);
        systems.drivingSystem.driveStraight(230, 0.7);
        systems.totemSystem.setAltitudeSlowly(TotemSystem.ALTITUDE1_MAX, 500);
    }

    /**
     * Goes to Carousel, and then to Crater in front of SH. Rams through obstacle.
     */
    public void LFYCO() {
        carouselPlaceFreightAndCarousel();
        // Ram through obstacle
        systems.drivingSystem.driveSideways(30, -0.6 * mirror);
        systems.drivingSystem.driveStraight(20, -0.6);
        systems.drivingSystem.turn(90 * mirror, 150);
        systems.armSystem.moveArm(Floors.OBSTACLE);
        systems.drivingSystem.driveStraight(300, 1);
        systems.totemSystem.setAltitudeSlowly(TotemSystem.ALTITUDE1_MAX, 500);
    }

    /**
     * Goes to Carousel, then to Crater behind SH. Rams through obstacle.
     */
    public void LBNCO() {
        carouselPlaceFreightAndCarousel();
        // Go to the right of the shipping hub and dodge
        systems.drivingSystem.driveSideways(50, -0.6 * mirror);
        systems.drivingSystem.driveStraight(125, 0.6);
        systems.drivingSystem.driveSideways(45, 0.6 * mirror);
        systems.drivingSystem.turn(180, 200);
        // Ram through obstacle
        systems.drivingSystem.turn(180, 200);
        systems.armSystem.moveArm(-200);
        systems.drivingSystem.driveStraight(30, -0.6);
        systems.drivingSystem.driveStraight(150, 1);
        systems.totemSystem.setAltitudeSlowly(TotemSystem.ALTITUDE1_MAX, 500);
    }

    /**
     * Goes to Carousel, then to Crater behind SH. Goes through path.
     */
    public void LBNCP() {
        carouselPlaceFreightAndCarousel();
        // Go to right of the shipping hub and dodge
        systems.drivingSystem.driveSideways(50, -0.6 * mirror);
        systems.drivingSystem.driveStraight(125, 0.6);
        systems.drivingSystem.driveSideways(45, 0.6 * mirror);
        systems.drivingSystem.turn(180, 200);

        // Go through path
        systems.drivingSystem.turn(180, 200);
        systems.drivingSystem.driveSideways(50, 0.6 * mirror);
        systems.drivingSystem.driveStraight(100, 0.6);
        systems.totemSystem.setAltitudeSlowly(TotemSystem.ALTITUDE1_MAX, 500);
    }
}
