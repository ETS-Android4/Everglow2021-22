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
        drivingSystem.driveStraight(25,0.6);
        floor = detectionSystem.findTargetFloor2(mirror);
        opMode.telemetry.addData("Floor: ", floor);
        opMode.telemetry.update();
        drivingSystem.driveStraight(25,-0.6);

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
                drivingSystem.driveStraight(15, -0.6 * mirror);
                break;
            case THIRD:
                drivingSystem.driveStraight(3, 0.6);
                drivingSystem.driveSideways(65, -0.6 * mirror);
                break;
        }
        drivingSystem.driveSideways(5, 0.6 * mirror);
        armSystem.autonomousReload();
    }

    /**
     * Goes to crater. Rams through obstacle.
     */
    public void RZNCO(int mirror) {
        placeFreight(mirror);
        dodgeToFront(mirror);
        
        drivingSystem.turn(180, 200);
        armSystem.moveArm(-200);
        drivingSystem.driveStraight(30, -0.6);
        drivingSystem.driveStraight(80, 1);
    }

    /**
     * Goes to crater. Enters through path.
     */
    public void RZNCP(int mirror) {
        placeFreight(mirror);
        dodgeToFront(mirror);
        
        drivingSystem.turn(180, 200);
        drivingSystem.driveSideways(60, 0.6 * mirror);
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
}
