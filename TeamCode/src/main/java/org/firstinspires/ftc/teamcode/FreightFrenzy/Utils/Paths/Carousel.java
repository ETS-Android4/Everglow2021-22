package org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

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

    public Carousel(LinearOpMode opMode) {
        this.opMode   = opMode;
        drivingSystem = new DrivingSystem(opMode);
        armSystem     = new ArmSystem(opMode);
        duckSystem    = new DuckSystem(opMode);
        detectionSystem = new DetectionSystem(opMode);
        timer = new ElapsedTime();
    }

    /**
     * Goes to alliance shipping hub and places the loaded freight there.
     */
    public void placeFreight() {
        ArmSystem.Floors floor = detectionSystem.findTargetFloor2();
        // move to carousel
        drivingSystem.driveStraight(95, 0.4);
        drivingSystem.turn(-90, 200);
        // place duck on carousel
        armSystem.autonomousMoveArm(floor);
        TimeUtils.sleep(500);
        drivingSystem.driveStraight(20, 0.4);
        TimeUtils.sleep(500);
        armSystem.spit();
        TimeUtils.sleep(500);
        armSystem.stop();
        drivingSystem.driveStraight(20, -0.4);
        TimeUtils.sleep(500);
        armSystem.autonomousReload();
    }

    /**
     * Goes to carousel, and then to duck, and then to crater.
     */
    public void L1() {
        placeFreight();
        // drive to carousel
        drivingSystem.driveSideways(121, 0.4);
        drivingSystem.driveStraight(80, -0.4);
        // drop duck
        duckSystem.runFor(1000);
        // Go to crater
        drivingSystem.driveStraight(100, 0.4);
        drivingSystem.driveSideways(10, 0.1);
        drivingSystem.driveStraight(250, 0.4);
    }

    /**
     * Goes to carousel, and then to duck, and then to alliance storage unit.
     */
    public void L2() {
        placeFreight();
        // go to duck
        drivingSystem.driveSideways(100, 0.4);
        drivingSystem.turn(180, 200);
        drivingSystem.driveStraight(50, 0.4);
        // spin duck
        duckSystem.runFor(5000);
        // go to alliance storage unit
        drivingSystem.driveSideways(70, 0.4);
    }

    /**
     * Goes to carousel, then to crater.
     */
    public void L3() {
        placeFreight();
        // go to right of the shipping hub
        drivingSystem.driveSideways(50, -0.4);
        drivingSystem.driveStraight(120, 0.4);
        drivingSystem.driveSideways(100, 0.4);
        // drives through barrier, using max power
        armSystem.moveArm(-300);
        TimeUtils.sleep(700);
        drivingSystem.driveUntilObstacle(60, 1);
        armSystem.autonomousReload();
    }
}
