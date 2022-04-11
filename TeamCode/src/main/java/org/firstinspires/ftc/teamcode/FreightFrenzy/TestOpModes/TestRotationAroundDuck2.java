package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

@TeleOp(name = "Test rotateAroundDuck 2", group = "Test")
public class TestRotationAroundDuck2 extends LinearOpMode {

    DrivingSystem drivingSystem;
    ArmSystem armSystem;
    DuckSystem duckSystem;
    EverglowGamepad gamepad;

    private static final double RIGHT_STICK_DOWN_MOVE_REDUCTION = 10;
    private static final double LEFT_STICK_DOWN_MOVE_REDUCTION = 5;

    @Override
    public void runOpMode() {
        drivingSystem = new DrivingSystem(this);
        armSystem = new ArmSystem(this);
        duckSystem = new DuckSystem(this);
        gamepad = new EverglowGamepad(gamepad1);

        waitForStart();
        while (opModeIsActive()) {
            gamepad.update();
            if (!gamepad1.y) {
                double left_stick_x = gamepad1.left_stick_x;
                double left_stick_y = gamepad1.left_stick_y;
                double right_stick_x = gamepad1.right_stick_x;

                if (gamepad1.right_stick_button) {
                    right_stick_x /= RIGHT_STICK_DOWN_MOVE_REDUCTION;
                }

                if (gamepad1.left_stick_button) {
                    left_stick_x /= LEFT_STICK_DOWN_MOVE_REDUCTION;
                    left_stick_y /= LEFT_STICK_DOWN_MOVE_REDUCTION;
                }

                drivingSystem.driveByJoystick(left_stick_x, left_stick_y, right_stick_x);
            } else {
                drivingSystem.rotateAroundDucks2(gamepad1.right_stick_x);
            }

            if (gamepad1.right_trigger > 0.1) {
                duckSystem.run();
            }
            if (gamepad1.left_trigger > 0.1) {
                duckSystem.stop();
            }

            armSystem.restOnLoad();
        }
    }
}

