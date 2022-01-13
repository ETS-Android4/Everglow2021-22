package org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

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
                drivingSystem.driveStraight(100, 0.5);
                drivingSystem.turn(mirror * 90, 200);
                break;
            case SECOND:
                drivingSystem.driveSideways(7, mirror * 0.5);
                drivingSystem.driveStraight(10, 0.5);
                drivingSystem.turn(mirror * 180, 200);
                drivingSystem.driveStraight(125, -0.5);
                drivingSystem.driveStraight(30, 0.5);
                drivingSystem.driveSideways(7, mirror * -0.5);
                drivingSystem.turn(mirror * -90, 200);
                drivingSystem.driveStraight(5, -0.4);
                break;
            case THIRD:
                drivingSystem.driveSideways(7, mirror * 0.5);
                drivingSystem.driveStraight(100, 0.5);
                drivingSystem.turn(mirror * 90, 200);
                break;
        }

        // place freight on SH
        armSystem.autonomousMoveArm(floor);
        TimeUtils.sleep(500);
        drivingSystem.driveStraight(10, 0.5);
        armSystem.spit();
        armSystem.stop();
        if (floor == ArmSystem.Floors.FIRST) {
            drivingSystem.driveStraight(15, -0.5);
        } else {
            drivingSystem.driveStraight(10, -0.5);
        }
        armSystem.moveArm(-300);
    }

    public void goToCarouselB(int mirror) {
        drivingSystem.driveSideways(50, 0.6 * mirror);
        drivingSystem.driveStraight(180, 0.5);
        drivingSystem.driveSideways(120, -0.7 * mirror);
        drivingSystem.driveSideways(45, -0.4 * mirror);
    }

    /**
     * Goes to crater. Rams through obstacle.
     */
    public void RZNCO(int mirror) {
        placeFreight(mirror);
        // go to crater through obstacle
        drivingSystem.turn(180, 200);
        if (floor == ArmSystem.Floors.THIRD) {
            drivingSystem.driveStraight(7, -0.4); // avoid totem
        }
        drivingSystem.driveSideways(60, mirror * 0.4);
        drivingSystem.driveStraight(40, -0.6);
        drivingSystem.driveStraight(180, 1);
    }

    /**
     * Goes to crater. Enters crater through path.
     */
    public void RZNCP(int mirror) {
        placeFreight(mirror);
        // go to crater through path
        drivingSystem.turn(180, 200);
        if (floor == ArmSystem.Floors.THIRD) {
            drivingSystem.driveStraight(7, -0.4); // avoid totem
        }
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
        drivingSystem.driveSideways(40, 0.6 * mirror);
        drivingSystem.turn(180, 200);
        drivingSystem.driveStraight(250, 1);
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
}
