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

        waitForStart();

        while (opModeIsActive()) {
            drivingSystem.driveByJoystick(gamepad2.left_stick_x, gamepad2.left_stick_y,
                    gamepad2.right_stick_x);

            if (ourGamepad2.buttonPress("x")) {
                armSystem.reload();
            }
            if (ourGamepad2.buttonPress("a")) {
                armSystem.moveArm(ArmSystem.Floors.FIRST);
            }
            if (ourGamepad2.buttonPress("b")) {
                armSystem.moveArm(ArmSystem.Floors.SECOND);
            }
            if (ourGamepad2.buttonPress("y")) {
                armSystem.moveArm(ArmSystem.Floors.THIRD);
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

            if (ourGamepad2.buttonPress("Rb")) {
                duckSystem.run();
            }
            if (ourGamepad2.buttonPress("Lb")) {
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
