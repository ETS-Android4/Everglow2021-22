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
                drivingSystem.driveSideways(10, mirror * 0.4);
                drivingSystem.driveStraight(95, 0.5);
                drivingSystem.driveSideways(6,-0.4 * mirror);
                drivingSystem.turn(mirror * 90, 200);
                break;
            case SECOND:
                drivingSystem.turn(30*mirror,200);
                drivingSystem.driveStraight(55,0.4);
                drivingSystem.turn(-60*mirror,200);
                drivingSystem.driveStraight(65,0.4);
                drivingSystem.turn(120*mirror,200);
                break;
            case THIRD:
                drivingSystem.driveStraight(95, 0.5);
                drivingSystem.driveSideways(4,mirror*0.4);
                drivingSystem.turn(mirror * 90, 200);

                break;
        }
        drivingSystem.driveStraight(3,-0.5);
        // place freight on SH
        armSystem.autonomousMoveArm(floor);
        TimeUtils.sleep(500);
        drivingSystem.driveStraight(5, 0.5);
        armSystem.spit();
        TimeUtils.sleep(1000);
        armSystem.stop();
        drivingSystem.driveStraight(5, -0.5);
        armSystem.autonomousReload();
    }

    public void goToCarouselB(int mirror) {
        drivingSystem.driveSideways(50, 0.6 * mirror);
        drivingSystem.driveStraight(183, 0.5);
        drivingSystem.driveSideways(110, -0.7 * mirror);
        drivingSystem.driveSideways(43, -0.4 * mirror);
    }

    public void dodgeToFront(int firstTurnDirection,int mirror){
        drivingSystem.turn(90*mirror*firstTurnDirection,200);
        switch(floor){
            case FIRST:
                drivingSystem.driveSideways(-10*mirror,0.5);
                drivingSystem.driveStraight(30,0.5);
                drivingSystem.driveSideways(10*mirror,-0.5);
                break;
            case SECOND:
                drivingSystem.driveSideways(10*mirror,0.5);
                drivingSystem.driveStraight(30,0.5);
                drivingSystem.driveSideways(-10*mirror,-0.5);
                break;
            case THIRD:
                drivingSystem.driveSideways(10*mirror,0.5);
                drivingSystem.driveStraight(30,0.5);
                drivingSystem.driveSideways(-10*mirror,-0.5);
                break;
        }
        drivingSystem.turn(-90*mirror*firstTurnDirection,200);
    }
    /**
     * Goes to crater. Rams through obstacle.
     */
    public void RZNCO(int mirror) {
        placeFreight(mirror);
        // go to crater through obstacle
        drivingSystem.turn(180, 200);
        //DODGE
        drivingSystem.driveSideways(60, mirror * 0.4);
        drivingSystem.driveStraight(40, -0.6);
        armSystem.autonomousMoveArm(ArmSystem.Floors.FIRST);
        TimeUtils.sleep(500);
        drivingSystem.driveUntilObstacle(50, 1);
    }

    /**
     * Goes to crater. Enters crater through path.
     */
    public void RZNCP(int mirror) {
        placeFreight(mirror);
        // go to crater through path
        drivingSystem.turn(180, 200);
        //DODGE
        drivingSystem.driveSideways(120, mirror * 0.4);
        drivingSystem.driveStraight(100, 0.4);
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
        armSystem.autonomousMoveArm(ArmSystem.Floors.FIRST);
        TimeUtils.sleep(500);
        drivingSystem.driveUntilObstacle(50, 0.6);
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
        armSystem.autonomousMoveArm(ArmSystem.Floors.FIRST);
        TimeUtils.sleep(500);
        drivingSystem.driveStraight(50, 0.6);
        drivingSystem.driveSideways(70,0.4);
        drivingSystem.driveUntilObstacle(50,0.6);
    }
    /**
     * Goes to carousel behind SH, then to warehouse.
     */
    public void RBYW(int mirror) {
        placeFreight(mirror);
        goToCarouselB(mirror);
        // drop duck
        duckSystem.runFor(5000);
        // go to warehouse
        drivingSystem.driveSideways(65, 0.4 * mirror);
    }

    /**
     * Goes to warehouse behind SH.
     */
    public void RBNW(int mirror) {
        placeFreight(mirror);
        // go to warehouse
        drivingSystem.driveSideways(50, 0.4 * mirror);
        drivingSystem.driveStraight(185, 0.4);
        drivingSystem.driveSideways(90, -0.4 * mirror);
    }

    /**
     * Goes to carouse in front of SH, then to warehouse.
     */
    public void RFYW(int mirror){
        placeFreight(mirror);
        armSystem.autonomousReload();
        //DODGE
        goToCarouselB(mirror);
        // drop duck
        duckSystem.runFor(5000);
        // go to warehouse
        drivingSystem.driveSideways(65, 0.4 * mirror);
        drivingSystem.driveStraight(5, 0.5);
    }

    /**
     * Goes to warehouse in front of SH.
     */
    public void RFNW(int mirror){
        placeFreight(mirror);
        armSystem.autonomousReload();
        //DODGE
        goToCarouselB(mirror);
        // go to warehouse
        drivingSystem.driveSideways(40, -0.4 * mirror);
        drivingSystem.driveStraight(185, 0.4);
        drivingSystem.driveSideways(10, 0.4 * mirror);
    }

    /**
     * Goes to carousel, then to crater from pass.
     */
    public void RFYCO(int mirror){
        placeFreight(mirror);
        armSystem.autonomousReload();
        //DODGE
        // go to warehouse
        goToCarouselB(mirror);
        duckSystem.runFor(5000);
        drivingSystem.driveSideways(50, 0.4 * mirror);
        drivingSystem.turn(180,200);
        armSystem.autonomousMoveArm(ArmSystem.Floors.FIRST);
        TimeUtils.sleep(500);
        drivingSystem.driveUntilObstacle(50, 0.6);
    }

    /**
     * Goes to carousel, then to crater from obstacle.
     */
    public void RFYCP(int mirror){
        placeFreight(mirror);
        armSystem.autonomousReload();
        //DODGE
        // go to warehouse
        goToCarouselB(mirror);
        duckSystem.runFor(5000);
        drivingSystem.driveSideways(50, 0.4 * mirror);
        drivingSystem.turn(180,200);
        armSystem.autonomousMoveArm(ArmSystem.Floors.FIRST);
        TimeUtils.sleep(500);
        drivingSystem.driveStraight(50, 0.6);
        drivingSystem.driveSideways(70,0.4);
        drivingSystem.driveUntilObstacle(50,0.6);
    }
}
