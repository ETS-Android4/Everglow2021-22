package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.TouchSensor;

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
    TouchSensor     touch;
    int             counter = 0;

    @Override
    public void runOpMode() {
        drivingSystem = new DrivingSystem(this);
        armSystem     = new ArmSystem(this);
        duckSystem    = new DuckSystem(this);
        ourGamepad1   = new EverglowGamepad(gamepad1);
        ourGamepad2   = new EverglowGamepad(gamepad2);
        touch         = hardwareMap.get(TouchSensor.class, "touch");

        boolean collecting = false;

        boolean prevTouchSensorPressed = false;


        waitForStart();

        while (opModeIsActive()) {
            drivingSystem.driveByJoystick(gamepad2.left_stick_x, gamepad2.left_stick_y,
                    gamepad2.right_stick_x);

            if (gamepad2.x) {
                armSystem.reload();
            }
            if (gamepad2.a) {
                armSystem.moveArm(ArmSystem.Floors.FIRST);
            }
            if (gamepad2.b) {
                armSystem.moveArm(ArmSystem.Floors.SECOND);
            }
            if (gamepad2.y) {
                armSystem.moveArm(ArmSystem.Floors.THIRD);
            }

            if (gamepad2.right_trigger > 0.1) {
                armSystem.collect();
                collecting = true;
            }
            if (gamepad2.left_trigger > 0.1) {
                armSystem.spit();
            }
            if (gamepad2.right_bumper || gamepad2.left_bumper) {
                armSystem.stop();
            }

            if (gamepad2.dpad_up) {
                duckSystem.run();
            }
            if (gamepad2.dpad_down) {
                duckSystem.stöp();
            }

            if (collecting && touch.isPressed()) {
                armSystem.stop();
                collecting = false;
            }

            // rumble controller if touchSensor was just pressed
            if (touch.isPressed()) {
                if (!prevTouchSensorPressed) {
                    gamepad1.rumble(1000);
                }
                prevTouchSensorPressed = true;
            } else {
                prevTouchSensorPressed = false;
                gamepad1.stopRumble();
            }

            armSystem.restOnLoad();
            armSystem.restOnFirstFloor();

            telemetry.addData("counter: ", counter++);
            telemetry.addData("arm position: ", armSystem.arm.getCurrentPosition());
            telemetry.update();
        }
    }
}
