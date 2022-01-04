package org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class Crater {
    DrivingSystem   drivingSystem;
    ArmSystem       armSystem;
    LinearOpMode    opMode;
    DetectionSystem detectionSystem;
    private final DuckSystem duckSystem;


    public Crater(LinearOpMode opMode) {
        this.opMode   = opMode;
        drivingSystem = new DrivingSystem(opMode);
        armSystem     = new ArmSystem(opMode);
//        detectionSystem = new DetectionSystem(opMode);
        duckSystem = new DuckSystem(opMode);
    }

    /**
     * Goes to alliance shipping hub and places the loaded freight there.
     */
    public void placeFreight() {
//        ArmSystem.Floors floor = detectionSystem.findTargetFloor2();
        // drive to alliance shipping hub
        drivingSystem.driveSideways(15, 0.4);
        TimeUtils.sleep(500);
        drivingSystem.driveStraight(95, 0.4);
        TimeUtils.sleep(500);
        drivingSystem.turn(90, 200);
        TimeUtils.sleep(500);
        // place freight on alliance shipping hub
        armSystem.autonomousMoveArm(ArmSystem.Floors.THIRD);
        TimeUtils.sleep(500);
        drivingSystem.driveStraight(10, 0.4);
        TimeUtils.sleep(500);
        armSystem.spit();
        TimeUtils.sleep(500);
        armSystem.stop();
        drivingSystem.driveStraight(10, -0.4);
        TimeUtils.sleep(500);
        armSystem.reload();
        TimeUtils.sleep(500);
    }

    /**
     * Goes to crater.
     */
    public void R1() {
        placeFreight();
        // go to crater and collect
        drivingSystem.driveSideways(121, 0.4);
        drivingSystem.turn(180, 200);
        drivingSystem.driveStraight(80, 0.4);
        armSystem.collect();
    }

    /**
     * Goes to carousel, then to crater.
     */
    public void R2() {
        placeFreight();
        // move to carousel
        drivingSystem.driveSideways(50, -0.4);
        drivingSystem.turn(90, 200);
        drivingSystem.driveSideways(150, -0.4);
        drivingSystem.turn(90, 200);
        drivingSystem.driveSideways(71, 0.4);
        // drop duck
        duckSystem.runFor(1000);
        // move to crater
        drivingSystem.driveSideways(80, -0.4);
        drivingSystem.driveStraight(240, 0.4);
        armSystem.collect();
    }

    /**
     * Goes to carousel, then to alliance storage unit.
     */
    public void R3() {
        placeFreight();
        // move to carousel
        drivingSystem.driveSideways(35, 0.4);
        drivingSystem.turn(90, 200);
        drivingSystem.driveSideways(165, 0.4);
        drivingSystem.turn(90, 200);
        drivingSystem.driveSideways(130, 0.4);
        // drop duck
        duckSystem.runFor(1000);
        // move to alliance storage unit
        drivingSystem.driveSideways(70, 0.4);
    }
}
