package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Crater;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;

@Autonomous(name = "Test Drive", group = "Test")
public class TestDrive extends LinearOpMode {
    Crater crater;

    @Override
    public void runOpMode() {
        DrivingSystem drivingSystem = new DrivingSystem(this);

        waitForStart();

        while (opModeIsActive()) {
            drivingSystem.driveStraight(50, 0.5);
            drivingSystem.driveStraight(-50, 0.5);
            drivingSystem.driveSideways(50, 0.5);
            drivingSystem.driveSideways(-50, 0.5);
            drivingSystem.turn(90, 150);
            drivingSystem.turn(-90, 150);
            stop();
        }
    }
}


