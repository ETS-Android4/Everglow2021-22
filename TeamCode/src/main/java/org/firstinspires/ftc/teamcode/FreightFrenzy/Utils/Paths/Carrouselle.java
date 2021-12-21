package org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;

public class Carrouselle {
    // not used
    DrivingSystem drivingSystem;
    ArmSystem armSystem;
    DetectionSystem detectionSystem;
    LinearOpMode opMode;

    public Carrouselle(LinearOpMode opMode, DetectionSystem detectionSystem){
        this.opMode = opMode;
        drivingSystem = new DrivingSystem(opMode);
        armSystem = new ArmSystem(opMode);
        this.detectionSystem = detectionSystem;
    }

    void basic(){
        ArmSystem.Floors targetFloor = detectionSystem.findTargetFloor();
        drivingSystem.driveStraight(121,0.4);
        drivingSystem.turn(90,200);
        drivingSystem.driveStraight(10,0.4);
        armSystem.moveArm(targetFloor);
        armSystem.
        drivingSystem.driveSideways(121,0.4);
        drivingSystem.driveStraight(80,0.4);
    }

    static void smallStorage(){

    }
}
