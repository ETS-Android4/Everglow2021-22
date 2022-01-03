package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

@TeleOp(name = "GameTeleOp", group = "Linear Opmode")
public class GameTeleOp extends LinearOpMode {
    DrivingSystem   drivingSystem;
    ArmSystem       armSystem;
    DuckSystem      duckSystem;
    EverglowGamepad ourGamepad1;
    EverglowGamepad ourGamepad2;
    int counter = 0;

    @Override
    public void runOpMode() {
        drivingSystem = new DrivingSystem(this);
        armSystem     = new ArmSystem(this);
        duckSystem    = new DuckSystem(this);
        ourGamepad1   = new EverglowGamepad(gamepad1);
        ourGamepad2   = new EverglowGamepad(gamepad2);

        boolean collecting = false;
        boolean spitting = false;
        boolean onFloor1 = false;
        boolean onFloor2 = false;
        boolean onFloor3 = false;

        waitForStart();

        while (opModeIsActive()) {
            drivingSystem.driveByJoystick(gamepad2.left_stick_x, gamepad2.left_stick_y,
                    gamepad2.right_stick_x);

            if (ourGamepad2.buttonPress("x")) {
                armSystem.reload();
                onFloor1 = false;
                onFloor2 = false;
                onFloor3 = false;
            }
            if (ourGamepad2.buttonPress("a")) {
                onFloor2 = false;
                onFloor3 = false;
                if (!onFloor1) {
                    armSystem.moveArm(ArmSystem.Floors.FIRST);
                    onFloor1 = true;
                } else {
                    armSystem.reload();
                    onFloor1 = false;
                }
            }
            if (ourGamepad2.buttonPress("b")) {
                onFloor1 = false;
                onFloor3 = false;
                if (!onFloor2) {
                    armSystem.moveArm(ArmSystem.Floors.SECOND);
                    onFloor2 = true;
                } else {
                    armSystem.reload();
                    onFloor2 = false;
                }
            }
            if (ourGamepad2.buttonPress("y")) {
                onFloor1 = false;
                onFloor2 = false;
                if (!onFloor3) {
                    armSystem.moveArm(ArmSystem.Floors.THIRD);
                    onFloor3 = true;
                } else {
                    armSystem.reload();
                    onFloor3 = false;
                }
            }

            if (ourGamepad2.buttonPress("Rt")) {
                spitting = false;
                if (!collecting) {
                    armSystem.collect();
                    collecting = true;
                } else {
                    armSystem.stop();
                    collecting = false;
                }
            }
            if (ourGamepad2.buttonPress("Lt")) {
                collecting = false;
                if (!spitting) {
                    armSystem.spit();
                    spitting = true;
                } else {
                    armSystem.stop();
                    spitting = false;
                }
            }

            if (gamepad2.right_bumper) {
                duckSystem.run();
            }
            if (gamepad2.left_bumper) {
                duckSystem.stöp();
            }

            armSystem.restOnLoad();

            ourGamepad1.update();
            ourGamepad2.update();
            telemetry.addData("counter: ", counter++);
            telemetry.addData("arm position: ", armSystem.arm.getCurrentPosition());
            telemetry.update();
        }
    }
}
