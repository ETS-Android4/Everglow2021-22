package org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;

public class Crate {
    DrivingSystem drivingSystem;
    ArmSystem armSystem;
    LinearOpMode opMode;
    DetectionSystem detectionSystem;


    public Crate(LinearOpMode opMode){
        this.opMode = opMode;
        drivingSystem = new DrivingSystem(opMode);
        armSystem = new ArmSystem(opMode);
        detectionSystem = new DetectionSystem(opMode);
    }

    void R1(){
        ArmSystem.Floors floor = detectionSystem.findTargetFloor();
        drivingSystem.driveStraight(121,0.4);
        drivingSystem.turn(90,200);
        drivingSystem.driveStraight(10,0.4);
        armSystem.moveArm(floor);
        armSystem.spit();
        drivingSystem.driveSideways(121,0.4);
        drivingSystem.driveStraight(-80,0.4);
        armSystem.collect();
    }

    void R2(){
        ArmSystem.Floors floor = detectionSystem.findTargetFloor();
        drivingSystem.driveStraight(121,0.4);
        drivingSystem.turn(90,200);
        drivingSystem.driveStraight(10,0.4);
        armSystem.moveArm(floor);
        armSystem.spit();
        drivingSystem.driveSideways(50,0.4);
        drivingSystem.driveStraight(150,0.4);
        drivingSystem.turn(-90,200);
        drivingSystem.driveSideways(71,0.4);
        drivingSystem.driveStraight(240,-0.4);
    }
}
