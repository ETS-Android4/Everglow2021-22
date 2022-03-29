package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem2;

@TeleOp(name = "tOtemMMTEESsstttt", group = "Test Opmode")
public class TestNewTotemSystem extends LinearOpMode {

    TotemSystem2 totemSystem;

    @Override
    public void runOpMode(){
        totemSystem = new TotemSystem2(this);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.dpad_up) {
                totemSystem.extendLeft(1);
                totemSystem.extendRight(-1);
            }
            if (gamepad1.left_trigger > 0) {
                totemSystem.extendLeft(gamepad1.left_trigger);
            } else {
                totemSystem.stopLeft();
            }
            if (gamepad1.right_trigger > 0) {
                totemSystem.extendRight(-gamepad1.right_trigger);
            } else {
                totemSystem.stopRight();
            }

            totemSystem.moveAltitude(gamepad1.right_stick_y / 100);
        }
    }
}
