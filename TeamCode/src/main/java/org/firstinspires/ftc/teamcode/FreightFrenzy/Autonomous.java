package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

@TeleOp(name = "FirstAutonomous", group = "Linear Opmode")
public class Autonomous extends LinearOpMode {
    DrivingSystem   drivingSystem;
    EverglowGamepad gamepad;
    DuckSystem duckSystem;
    int SecondClickY = 0;

    @Override
    public void runOpMode() {
        drivingSystem = new DrivingSystem(this);
        gamepad       = new EverglowGamepad(gamepad1);
        duckSystem    = new DuckSystem(this);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad.buttonPress("a")) {
                drivingSystem.driveStraight(100,0.3);
            }
            if (gamepad.buttonPress("x")) {
                drivingSystem.driveStraight(100,-0.3);
            }

            if (gamepad.buttonPress("b")) {
                drivingSystem.rotateInPlace(90);
            }
            if (gamepad.buttonPress("y")){
                if(SecondClickY%2==0){
                    duckSystem.runDuckWheel();
                }
                else {
                    duckSystem.stopDuckWheel();
                }
            }

            gamepad.update();
            telemetry.update();
        }
    }
}
