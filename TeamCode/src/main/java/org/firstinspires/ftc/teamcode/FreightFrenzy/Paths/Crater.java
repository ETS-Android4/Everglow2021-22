package org.firstinspires.ftc.teamcode.FreightFrenzy.Paths;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AllSystems;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class Crater {
    DrivingSystem drivingSystem;
    ArmSystem     armSystem;
    LinearOpMode  opMode;
    public        DetectionSystem detectionSystem;
    private final DuckSystem      duckSystem;
    ArmSystem.Floors floor;


    public Crater(LinearOpMode opMode) {
        this.opMode     = opMode;
        drivingSystem   = new DrivingSystem(opMode);
        armSystem       = new ArmSystem(opMode);
        detectionSystem = new DetectionSystem(opMode, armSystem);
        duckSystem      = new DuckSystem(opMode);
    }

    public Crater(AllSystems systems) {
        this.opMode          = systems.opMode;
        this.drivingSystem   = systems.drivingSystem;
        this.armSystem       = systems.armSystem;
        this.detectionSystem = systems.detectionSystem;
        this.duckSystem      = systems.duckSystem;
    }


    /**
     * Goes to alliance shipping hub and places the loaded freight there.
     */
    public void placeFreight(int mirror) {
        drivingSystem.resetDistance();
        floor = detectionSystem.findTargetFloor2(mirror);
        opMode.telemetry.addData("Floor: ", floor);
        opMode.telemetry.update();

        // avoid totem
        switch (floor) {
            case FIRST:
                drivingSystem.driveSideways(12, 0.6 * mirror);
                drivingSystem.driveStraight(95, 0.6);
                drivingSystem.driveSideways(11, -0.6 * mirror);
                TimeUtils.sleep(200);
                drivingSystem.turn(mirror * 90, 200);
                break;
            case SECOND:
                drivingSystem.driveStraight(5, 0.5);
                TimeUtils.sleep(200);
                drivingSystem.turn(30 * mirror, 100);
                drivingSystem.driveStraight(51, 0.5);
                TimeUtils.sleep(200);
                drivingSystem.turn(-60 * mirror, 100);
                drivingSystem.driveStraight(50, 0.5);
                TimeUtils.sleep(200);
                drivingSystem.turn(120 * mirror, 100);
                break;
            case THIRD:
                drivingSystem.driveStraight(95, 0.6);
                drivingSystem.driveSideways(4, mirror * 0.6);
                TimeUtils.sleep(200);
                drivingSystem.turn(mirror * 90, 200);
                break;
        }
        drivingSystem.driveStraight(3, -0.6);
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

    public void goToCarouselB(int mirror) {
        drivingSystem.driveSideways(50, 0.6 * mirror);
        drivingSystem.driveStraight(183, 0.6);
        drivingSystem.driveSideways(110, -0.7 * mirror);
        drivingSystem.driveSideways(43, -0.6 * mirror);
    }

    public void dodgeToFront(int firstTurnDirection, int mirror) {
        switch (floor) {
            case FIRST:
                armSystem.autonomousMoveArm(ArmSystem.Floors.SECOND);
                drivingSystem.driveStraight(3, -0.6);
                drivingSystem.driveSideways(77, -0.6 * mirror);
                armSystem.autonomousReload();
                drivingSystem.driveStraight(3, 0.6);
                break;
            case SECOND:
                drivingSystem.turn(-90 * mirror * firstTurnDirection, 50);
                drivingSystem.driveSideways(18, 0.6 * mirror);
                drivingSystem.driveStraight(65, 0.6);
                drivingSystem.turn(90 * mirror * firstTurnDirection, 50);
                drivingSystem.driveStraight(15, -0.6 * mirror);
                break;
            case THIRD:
                drivingSystem.driveStraight(3, 0.6);
                drivingSystem.driveSideways(65, -0.6);
                break;
        }
    }

    /**
     * Goes to crater. Rams through obstacle.
     */
    public void RZNCO(int mirror) {
        placeFreight(mirror);
        // go to crater through obstacle
        dodgeToFront(-1, mirror);
        drivingSystem.turn(180, 200);
        armSystem.autonomousMoveArm(ArmSystem.Floors.SECOND);
        TimeUtils.sleep(500);
        drivingSystem.driveStraight(100, 1);
    }

    /**
     * Goes to crater. Enters crater through path.
     */
    public void RZNCP(int mirror) {
        placeFreight(mirror);
        // go to crater through path
        dodgeToFront(-1, mirror);
        drivingSystem.turn(180, 200);
        drivingSystem.driveSideways(60, 0.6);
        drivingSystem.driveStraight(100, 0.6);
    }

    /**
     * Goes to carousel behind SH, then to crater. Rams through obstacle.
     */
    public void RBYCO(int mirror) {
        placeFreight(mirror);
        // go to carousel
        goToCarouselB(mirror);
        // drop duck
        duckSystem.runFor(4000);
        // go to crater through obstacle
        drivingSystem.driveSideways(20, 0.6 * mirror);
        drivingSystem.turn(180, 200);
        armSystem.autonomousMoveArm(ArmSystem.Floors.SECOND);
        TimeUtils.sleep(500);
        drivingSystem.driveStraight(200, 1);
    }

    /**
     * Goes to carousel behind SH, then to crater. Through Pass.
     */
    public void RBYCP(int mirror) {
        placeFreight(mirror);
        // go to carousel
        goToCarouselB(mirror);
        // drop duck
        duckSystem.runFor(4000);
        // go to crater through obstacle
        drivingSystem.driveSideways(35, 0.6 * mirror);
        drivingSystem.turn(180, 200);
        drivingSystem.driveStraight(50, 0.6);
        drivingSystem.driveSideways(70, 0.4);
        drivingSystem.driveStraight(200, 0.6);
    }

    /**
     * Goes to carousel behind SH, then to warehouse.
     */
    public void RBYW(int mirror) {
        placeFreight(mirror);
        goToCarouselB(mirror);
        // drop duck
        duckSystem.runFor(3000);
        // go to warehouse
        drivingSystem.driveSideways(65, 0.6 * mirror);
    }

    /**
     * Goes to warehouse behind SH.
     */
    public void RBNW(int mirror) {
        placeFreight(mirror);
        // go to warehouse
        drivingSystem.driveSideways(50, 0.6 * mirror);
        drivingSystem.driveStraight(185, 0.6);
        drivingSystem.driveSideways(90, -0.6 * mirror);
    }

    /**
     * Goes to carouse in front of SH, then to warehouse.
     */
    public void RFYW(int mirror) {
        placeFreight(mirror);
        armSystem.autonomousReload();
        //DODGE
        dodgeToFront(-1, mirror);
        drivingSystem.driveSideways(20, -0.6 * mirror);
        drivingSystem.driveStraight(185, 0.6);
        // drop duck
        duckSystem.runFor(3000);
        // go to warehouse
        drivingSystem.driveSideways(65, 0.6 * mirror);
        drivingSystem.driveStraight(5, 0.6);
    }

    /**
     * Goes to warehouse in front of SH.
     */
    public void RFNW(int mirror) {
        placeFreight(mirror);
        armSystem.autonomousReload();
        //DODGE
        dodgeToFront(-1, mirror);
        // go to warehouse
        drivingSystem.driveStraight(180, 0.6);
        drivingSystem.driveSideways(40, 0.6 * mirror);
    }

    /**
     * Goes to carousel, then to crater from pass.
     */
    public void RFYCO(int mirror) {
        placeFreight(mirror);
        armSystem.autonomousReload();
        dodgeToFront(-1, mirror);
        // go to warehouse
        drivingSystem.driveStraight(180, 0.6);
        drivingSystem.driveSideways(15, -0.6 * mirror);
        duckSystem.runFor(3000);
        drivingSystem.driveSideways(20, 0.6 * mirror);
        drivingSystem.turn(180, 50);
        armSystem.autonomousMoveArm(ArmSystem.Floors.SECOND);
        drivingSystem.driveSideways(10, -0.6);
        drivingSystem.driveStraight(250, 1);
    }

    /**
     * Goes to carousel, then to crater from obstacle.
     */
    public void RFYCP(int mirror) {
        placeFreight(mirror);
        armSystem.autonomousReload();
        dodgeToFront(-1, mirror);
        // go to warehouse
        drivingSystem.driveStraight(180, 0.6);
        drivingSystem.driveSideways(15, -0.6 * mirror);
        duckSystem.runFor(3000);
        drivingSystem.driveStraight(50, -0.6);
        drivingSystem.turn(180, 200);
        drivingSystem.driveSideways(25, 0.6);
        drivingSystem.driveStraight(150, 0.8);
    }
}
