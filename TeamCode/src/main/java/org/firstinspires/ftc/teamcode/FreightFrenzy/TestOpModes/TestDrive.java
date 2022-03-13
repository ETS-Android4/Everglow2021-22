package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Crater;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;

@TeleOp(name = "Test Drive", group = "Test")
@Disabled
public class TestDrive extends LinearOpMode {
    Crater crater;

    @Override
    public void runOpMode() {
        DrivingSystem drivingSystem = new DrivingSystem(this);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad2.b) {
                drivingSystem.driveStraight(75, -0.5);
            }
            if (gamepad2.x) {
                drivingSystem.driveSideways(75, 0.5);
            }
            if (gamepad2.y) {
                drivingSystem.driveSideways(75, -0.5);
            }
            if (gamepad2.dpad_up) {
                drivingSystem.turn(90, 150);
            }
            if (gamepad2.dpad_down) {
                drivingSystem.turn(-90, 150);
            }
        }
    }
}


