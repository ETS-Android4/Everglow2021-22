package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

@TeleOp(name = "FirstAutonomous", group = "Linear Opmode")
public class Autonomous extends LinearOpMode {
    DrivingSystem   drivingSystem;
    EverglowGamepad gamepad;

    @Override
    public void runOpMode() {
        drivingSystem = new DrivingSystem(this);
        gamepad       = new EverglowGamepad(gamepad1);


        waitForStart();

        while (opModeIsActive()) {
            if (gamepad.buttonPress("a")) {
                drivingSystem.driveStraight(100,0.3);
            }
            if (gamepad.buttonPress("x")) {
                drivingSystem.driveStraight(100,-0.3);
            }

            if (gamepad.buttonPress("b")) {
                drivingSystem.rotateInPlace(90, 0.3, 0, true);
            }

            if (gamepad.buttonPress("y")) {
                drivingSystem.rotateInPlace(90, 0.3, 0, false);
            }




            gamepad.update();
            telemetry.update();
        }
    }
}
