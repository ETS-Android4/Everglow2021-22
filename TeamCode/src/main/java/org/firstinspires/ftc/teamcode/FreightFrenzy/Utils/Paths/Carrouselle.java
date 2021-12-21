package org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;

public class Carrouselle {
    DrivingSystem drivingSystem;
    ArmSystem armSystem;
    DuckSystem duckSystem;
    LinearOpMode opMode;
    ElapsedTime timer;

    public Carrouselle(LinearOpMode opMode){
        this.opMode = opMode;
        drivingSystem = new DrivingSystem(opMode);
        armSystem = new ArmSystem(opMode);
        duckSystem = new DuckSystem(opMode);
        timer = new ElapsedTime();
    }

    public void basic(){
        drivingSystem.driveStraight(121,0.4);
        drivingSystem.turn(90,200);
        drivingSystem.driveStraight(20,0.4);
        drivingSystem.driveSideways(121,0.4);
        drivingSystem.driveStraight(80,-0.4);
        duckSystem.run();
        sleep(5000);
        duckSystem.stöp();
        drivingSystem.driveStraight(100,0.4);
        drivingSystem.driveSideways(10,0.4);
        drivingSystem.driveStraight(250,0.4);
    }

    public void smallStorage(){
        drivingSystem.driveStraight(121,0.4);
        drivingSystem.turn(90,200);
        drivingSystem.driveStraight(20,0.4);
        drivingSystem.driveSideways(121,0.4);
        drivingSystem.driveStraight(80,-0.4);
        duckSystem.run();
        sleep(5000);
        duckSystem.stöp();
        drivingSystem.driveSideways(50,-0.4);
        drivingSystem.driveStraight(20,-0.4);
    }

    void sleep(int time) {
        timer.reset();
        while (timer.milliseconds() < time) {
            // ahhhhhhhhhhhhhhhhhhhh
        }
    }

}
