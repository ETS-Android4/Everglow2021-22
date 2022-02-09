package org.firstinspires.ftc.teamcode.FreightFrenzy.Paths;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AllSystems;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class Carousel {
    DrivingSystem drivingSystem;
    ArmSystem     armSystem;
    DuckSystem    duckSystem;
    public DetectionSystem detectionSystem;
    LinearOpMode     opMode;
    ElapsedTime      timer;
    ArmSystem.Floors floor;

    public Carousel(LinearOpMode opMode) {
        this.opMode     = opMode;
        drivingSystem   = new DrivingSystem(opMode);
        armSystem       = new ArmSystem(opMode);
        duckSystem      = new DuckSystem(opMode);
        detectionSystem = new DetectionSystem(opMode, armSystem);
        timer           = new ElapsedTime();
    }

    public Carousel(AllSystems systems) {
        this.opMode          = systems.opMode;
        this.drivingSystem   = systems.drivingSystem;
        this.armSystem       = systems.armSystem;
        this.duckSystem      = systems.duckSystem;
        this.detectionSystem = systems.detectionSystem;
        timer                = new ElapsedTime();
    }

    /**
     * Goes to alliance shipping hub and places the loaded freight there.
     */
    public void placeFreight(int mirror) {
        drivingSystem.resetDistance();
        drivingSystem.driveStraight(20, 0.6);
        floor = detectionSystem.findTargetFloor2(mirror);

        this.opMode.telemetry.addData("floor: ", floor);
        this.opMode.telemetry.update();

        //avoid totem
        switch (floor) {
            case FIRST:
                drivingSystem.driveSideways(15, 0.6 * mirror);
                break;
            case SECOND:
                drivingSystem.driveSideways(25, -0.6 * mirror);
                break;
        }
        // move to SH
        drivingSystem.driveStraight(80, 0.6);
        switch (floor) {
            case FIRST:
                drivingSystem.driveSideways(15, -0.6 * mirror);
                break;
            case SECOND:
                drivingSystem.driveSideways(25, 0.6 * mirror);
                break;
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

    public void dodgeToFront(int firstTurnDirection, int mirror) {
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

    public void dodgeOtherTotem(int firstTurnDirection, int mirror) {
        AllSystems systems = new AllSystems(opMode, armSystem, detectionSystem, drivingSystem, duckSystem);
        Crater crater = new Crater(systems);
        crater.floor = floor;
        crater.dodgeToFront(firstTurnDirection, mirror);
    }

    public void goToCarousel(int mirror) {
        drivingSystem.driveStraight(30, -0.6);
        drivingSystem.turn(180, 200);
        drivingSystem.driveSideways(100, -0.6 * mirror);
    }

    /**
     * Goes to carousel, and then to crater behind SH. Rams through obstacle.
     */
    public void LBYCO(int mirror) {
        placeFreight(mirror);
        goToCarousel(mirror);
        // spin duck
        duckSystem.runFor(3000);
        // go to crater through obstacle
        drivingSystem.driveSideways(150, 0.6 * mirror);
        drivingSystem.turn(180, 200);
        drivingSystem.driveStraight(125, 0.6);
        drivingSystem.driveSideways(120, 0.6 * mirror);
        armSystem.autonomousMoveArm(ArmSystem.Floors.SECOND);
        drivingSystem.driveStraight(30, -0.7);
        drivingSystem.driveStraight(100, 1);
    }

    /**
     * Goes to carousel, and then to crater behind SH. Rams through obstacle.
     */
    public void LBYCP(int mirror) {
        placeFreight(mirror);
        goToCarousel(mirror);
        // spin duck
        duckSystem.runFor(3000);
        // go to crater through obstacle
        drivingSystem.driveSideways(150, 0.6 * mirror);
        drivingSystem.turn(180, 200);
        drivingSystem.driveStraight(125, 0.6);
        drivingSystem.driveSideways(15, 0.6 * mirror);
        dodgeToFront(1, mirror);
        drivingSystem.driveSideways(55, 0.6 * mirror);
        drivingSystem.driveStraight(50, 0.6);
    }

    /**
     * Goes to carousel, and then to warehouse.
     */
    public void LZYW(int mirror) {
        placeFreight(mirror);
        goToCarousel(mirror);
        // spin duck
        duckSystem.runFor(3000);
        // go to alliance storage unit
        drivingSystem.driveSideways(65, 0.6 * mirror);
    }

    /**
     * Goes to carousel, then to crater behind SH. Rams through obstacle.
     */
    public void LBNCO(int mirror) {
        placeFreight(mirror);
        // go to right of the shipping hub and dodge
        drivingSystem.driveSideways(50, -0.6 * mirror);
        drivingSystem.driveStraight(125, 0.6);
        drivingSystem.driveSideways(45, 0.6 * mirror);
        drivingSystem.turn(180, 200);
        dodgeOtherTotem(-1, mirror);
        drivingSystem.turn(180, 200);
        // drives through barrier, using max power
        drivingSystem.driveSideways(10, -0.6 * mirror);
        drivingSystem.driveStraight(30, -0.6);
        armSystem.autonomousMoveArm(ArmSystem.Floors.SECOND);
        TimeUtils.sleep(500);
        drivingSystem.driveStraight(100, 1);
    }

    /**
     * Goes to carousel, then to crater behind SH. Goes through path.
     */
    public void LBNCP(int mirror) {
        placeFreight(mirror);
        // go to right of the shipping hub and dodge
        drivingSystem.driveSideways(50, -0.6 * mirror);
        drivingSystem.driveStraight(125, 0.6);
        drivingSystem.driveSideways(45, 0.6 * mirror);
        drivingSystem.turn(180, 200);
        dodgeOtherTotem(-1, mirror);
        drivingSystem.turn(180, 200);
        // go through path
        drivingSystem.driveSideways(50, 0.6 * mirror);
        drivingSystem.driveStraight(100, 0.6);
    }

    /**
     * Goes to warehouse.
     */
    public void LZNW(int mirror) {
        placeFreight(mirror);
        //drive to storage unit
        drivingSystem.driveStraight(55, -0.6);
        drivingSystem.driveSideways(30, 0.6 * mirror);
    }

    /**
     * Goes to carousel, and then to crater in front of SH. Enters crater through path.
     */
    public void LFYCP(int mirror) {
        placeFreight(mirror);
        goToCarousel(mirror);
        // spin duck
        duckSystem.runFor(3000);
        // go to crater through path
        drivingSystem.driveSideways(30, 0.7 * mirror);
        drivingSystem.turn(180, 150);
        drivingSystem.driveStraight(50, 0.7);
        drivingSystem.driveSideways(70, 0.7 * mirror);
        drivingSystem.driveStraight(155, 1);
    }

    /**
     * Goes to carousel, and then to crater in front of SH. Enters crater through obstacle.
     */
    public void LFYCO(int mirror) {
        placeFreight(mirror);
        goToCarousel(mirror);
        // spin duck
        duckSystem.runFor(3000);
        // go to crater through obstacle
        drivingSystem.driveSideways(30, 0.6 * mirror);
        drivingSystem.turn(180, 150);
        armSystem.autonomousMoveArm(ArmSystem.Floors.SECOND);
        TimeUtils.sleep(500);
        drivingSystem.driveStraight(250, 1);
    }
}
