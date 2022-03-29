package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem2;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

@TeleOp(name = "tOtemMMTEESsstttt", group = "Test Opmode")
public class TestNewTotemSystem extends LinearOpMode {

    TotemSystem2    totemSystem;
    EverglowGamepad gamepad;
    int             dirR = 1;
    int             dirL = 1;

    @Override
    public void runOpMode() {
        totemSystem = new TotemSystem2(this);
        gamepad     = new EverglowGamepad(gamepad1);

        waitForStart();

        while (opModeIsActive()) {
            gamepad.update();
            if (gamepad1.dpad_up) {
                totemSystem.extendLeft(1);
                totemSystem.extendRight(-1);
            }
            if (gamepad1.dpad_down) {
                totemSystem.extendLeft(-1);
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
            } else {
                totemSystem.stopLeft();
            }

            if (gamepad1.right_trigger > 0) {
                totemSystem.extendRight(dirR * Math.sqrt(
                        1 - (gamepad1.right_trigger - 1) * (gamepad1.right_trigger - 1)
                ));
            } else {
                totemSystem.stopRight();
            }

            totemSystem.moveAltitude(gamepad1.right_stick_y / 100);
        }
    }
}
