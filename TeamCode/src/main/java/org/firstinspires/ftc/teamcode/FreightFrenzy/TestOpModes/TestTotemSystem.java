package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

@TeleOp(name = "Totem Test", group = "Test")
public class TestTotemSystem extends LinearOpMode {

    TotemSystem totemSystem;
    EverglowGamepad gamepad;
    DrivingSystem drivingSystem;
    int dirR = 1;
    int dirL = 1;

    @Override
    public void runOpMode() {
        totemSystem = new TotemSystem(this);
        gamepad = new EverglowGamepad(gamepad1);
        drivingSystem = new DrivingSystem(this);
        waitForStart();

        while (opModeIsActive()) {
            drivingSystem.rotateAroundDucks(gamepad1.right_stick_x, true);
            drivingSystem.rotateAroundDucks(gamepad1.left_stick_x, false);

            gamepad.update();
            if (gamepad1.dpad_up) {
                totemSystem.extendLeft(-1);
                totemSystem.extendRight(1);
            }
            if (gamepad1.dpad_down) {
                totemSystem.extendLeft(1);
                totemSystem.extendRight(-1);
            }

            if (gamepad.rb()) {
                dirR = -dirR;
            }
            if (gamepad.lb()) {
                dirL = -dirL;
            }

            if (gamepad1.left_trigger > 0) {
                totemSystem.extendLeft(dirL * Math.sqrt(
                        1 - (gamepad1.left_trigger - 1) * (gamepad1.left_trigger - 1)
                ));
            }

            if (gamepad1.right_trigger > 0) {
                totemSystem.extendRight(dirR * Math.sqrt(
                        1 - (gamepad1.right_trigger - 1) * (gamepad1.right_trigger - 1)
                ));
            }

            if (gamepad1.right_trigger == 0 && !gamepad1.dpad_down && !gamepad1.dpad_up) {
                totemSystem.stopRight();
            }
            if (gamepad1.left_trigger == 0 && !gamepad1.dpad_down && !gamepad1.dpad_up) {
                totemSystem.stopLeft();
            }

            totemSystem.moveAltitude(-gamepad1.right_stick_y / 1000);
            totemSystem.moveAltitude(gamepad1.left_stick_y / 1000);

            telemetry.addData("currPos1", totemSystem.altitude1.getPosition());
            telemetry.addData("currPos2", totemSystem.altitude2.getPosition());
            telemetry.update();
        }
    }
}
