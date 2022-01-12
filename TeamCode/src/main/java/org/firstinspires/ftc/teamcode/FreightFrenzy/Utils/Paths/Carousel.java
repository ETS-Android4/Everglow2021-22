package org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AllSystems;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class Carousel {
    DrivingSystem   drivingSystem;
    ArmSystem       armSystem;
    DuckSystem      duckSystem;
    public DetectionSystem detectionSystem;
    LinearOpMode    opMode;
    ElapsedTime     timer;
    ArmSystem.Floors floor;

    public Carousel(LinearOpMode opMode) {
        this.opMode   = opMode;
        drivingSystem = new DrivingSystem(opMode);
        armSystem     = new ArmSystem(opMode);
        duckSystem    = new DuckSystem(opMode);
        detectionSystem = new DetectionSystem(opMode, armSystem);
        timer = new ElapsedTime();
    }

    public Carousel(AllSystems systems){
        this.opMode = systems.opMode;
        this.drivingSystem = systems.drivingSystem;
        this.armSystem = systems.armSystem;
        this.duckSystem = systems.duckSystem;
        this.detectionSystem = systems.detectionSystem;
        timer = new ElapsedTime();
    }



    /**
     * Goes to alliance shipping hub and places the loaded freight there.
     */
    public void placeFreight(int mirror) {
        drivingSystem.resetDistance();
        floor = detectionSystem.findTargetFloor2(mirror);

        this.opMode.telemetry.addData("floor: ", floor);
        this.opMode.telemetry.update();

        //avoid totem
        switch (floor){
            case FIRST:
                drivingSystem.driveSideways(15,0.4*mirror);
                break;
            case SECOND:
                drivingSystem.driveSideways(25,-0.4*mirror);
                break;
        }
        // move to carousel
        drivingSystem.driveStraight(100, 0.5);

        //get back to the same position for all
        switch (floor){
            case FIRST:
                drivingSystem.driveSideways(15,-0.4*mirror);
                break;
            case SECOND:
                drivingSystem.driveSideways(25,0.4*mirror);
                break;
        }
        drivingSystem.turn(-90*mirror, 200);
        // place duck on carousel
        armSystem.autonomousMoveArm(floor);
        TimeUtils.sleep(500);
        drivingSystem.driveStraight(25, 0.4);
        TimeUtils.sleep(500);
        armSystem.spit();
        TimeUtils.sleep(500);
        armSystem.stop();
        drivingSystem.driveStraight(20, -0.4);
        armSystem.autonomousReload();
    }
    /**
     * Goes to carousel, and then to duck, and then to crater.
     */
    public void LBYCO(int mirror) {
        placeFreight(mirror);
        drivingSystem.driveStraight(50, -0.4);
        drivingSystem.turn(180, 150);
        drivingSystem.driveSideways(100, -0.6*mirror);
        // spin duck
        duckSystem.runFor(5000);
        // go to crater
        drivingSystem.driveSideways(38, 0.6*mirror);
        drivingSystem.turn(180,150);
        armSystem.moveArm(-300);
        TimeUtils.sleep(700);
        drivingSystem.driveStraight(240, 1);
//        drivingSystem.driveUntilObstacle(60, 0.9);
//        armSystem.autonomousReload();
    }

    /**
     * Goes to carousel, and then to duck, and then to alliance storage unit.
     */
    public void LZYW(int mirror) {
        placeFreight(mirror);
        // go to duck
        drivingSystem.driveStraight(50, -0.4);
        drivingSystem.turn(180, 200);
        drivingSystem.driveSideways(100, -0.4*mirror);
        // spin duck
        duckSystem.runFor(5000);
        // go to alliance storage unit
        drivingSystem.driveSideways(70, 0.4* mirror);
    }

    /**
     * Goes to carousel, then to crater.
     */
    public void LBNCO(int mirror) {
        placeFreight(mirror);
        // go to right of the shipping hub
        drivingSystem.driveSideways(50, -0.4*mirror);
        drivingSystem.driveStraight(125, 0.4);
        drivingSystem.driveSideways(120, 0.4*mirror);
        // drives through barrier, using max power
        armSystem.moveArm(-300);
        TimeUtils.sleep(700);
        drivingSystem.driveStraight(40, -0.6);
        drivingSystem.driveStraight(180, 1);
//        drivingSystem.driveUntilObstacle(60, 1);
//        armSystem.autonomousReload();
    }

    public void LZNW(int mirror){
        placeFreight(mirror);

        //drive to storage unit
        drivingSystem.driveStraight(55,-0.4);
        drivingSystem.driveSideways(30,0.4*mirror);
    }

    public void LFYCP(int mirror) {
        // Tomer add from email
    }
}
