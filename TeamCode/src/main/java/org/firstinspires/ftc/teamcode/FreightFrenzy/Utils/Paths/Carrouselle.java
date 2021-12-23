package org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;

public class Carrouselle {
    // not used
    DrivingSystem drivingSystem;
    ArmSystem     armSystem;
    DuckSystem    duckSystem;
    DetectionSystem detectionSystem;
    LinearOpMode  opMode;
    ElapsedTime   timer;

    public Carrouselle(LinearOpMode opMode) {
        this.opMode   = opMode;
        drivingSystem = new DrivingSystem(opMode);
        armSystem     = new ArmSystem(opMode);
        duckSystem    = new DuckSystem(opMode);
        detectionSystem = new DetectionSystem(opMode);
        timer         = new ElapsedTime();
    }

    /**
     * Goes To Caursel, and then to duck, and then to left parking spot.
     */
    public void L1() {
        ArmSystem.Floors floor = detectionSystem.findTargetFloor2();
        // move to caursel
        drivingSystem.driveStraight(121, 0.4);
        drivingSystem.turn(90, 200);
        drivingSystem.driveStraight(20, 0.4);
        // place duck on caursel
        armSystem.moveArm(floor);
        armSystem.spit();
        armSystem.reload();
        // drive to duck
        drivingSystem.driveSideways(121, 0.4);
        drivingSystem.driveStraight(80, -0.4);
        // Spin Duck
        duckSystem.run();
        sleep(5000);
        duckSystem.stöp();
        // Go to left parking spot
        drivingSystem.driveStraight(100, 0.4);
        drivingSystem.driveSideways(10, 0.1);
        drivingSystem.driveStraight(250, 0.4);
    }

    /**
     * Goes To Caursel, and then to duck, and then to right parking spot.
     */
    public void L2() {
        ArmSystem.Floors floor = detectionSystem.findTargetFloor2();
        // go to carsel
        drivingSystem.driveStraight(121, 0.4);
        drivingSystem.turn(90, 200);
        drivingSystem.driveStraight(20, 0.4);
        // place duck on caursel
        armSystem.moveArm(floor);
        armSystem.spit();
        armSystem.reload();
        // go to duck
        drivingSystem.driveSideways(121, 0.4);
        drivingSystem.driveStraight(80, -0.4);
        // spin duck
        duckSystem.run();
        sleep(5000);
        duckSystem.stöp();
        // go to right parking spot
        drivingSystem.driveSideways(50, -0.4);
        drivingSystem.driveStraight(20, -0.4);
    }

    /**
     * Goes to Caursel, then to right parking spot.
     */
    public void L3(){
        ArmSystem.Floors floor = detectionSystem.findTargetFloor2();
        // move to carousel
        drivingSystem.driveStraight(121, 0.4);
        drivingSystem.turn(90, 200);
        drivingSystem.driveStraight(40, 0.4);
        // place on caursel
        armSystem.moveArm(floor);
        armSystem.spit();
        armSystem.reload();
        // go to right of the shipping hub
        drivingSystem.driveSideways(50, -0.4);
        drivingSystem.driveStraight(130, 0.4);
        drivingSystem.driveSideways(70, 0.4);
        // drives through barrier, using max power
        drivingSystem.driveStraight(75, 1);
    }

    void sleep(int time) {
        timer.reset();
        while (timer.milliseconds() < time) {
            // ahhhhhhhhhhhhhhhhhhhh
        }
    }

}
