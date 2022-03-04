package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Carousel;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Crater;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;

@TeleOp(name = "TestDriveStraight", group = "LinearOpMode")
public class TestDriveStraight extends LinearOpMode {
    Carousel        car;
    Crater          crater;

    @Override
    public void runOpMode() {
        DrivingSystem drivingSystem = new DrivingSystem(this);

        waitForStart();

        while (opModeIsActive()) {
            drivingSystem.driveStraightUntilBumping(0.5, 20);
            stop();
        }
    }
}
