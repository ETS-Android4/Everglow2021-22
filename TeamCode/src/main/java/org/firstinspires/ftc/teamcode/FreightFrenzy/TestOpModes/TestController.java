package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name = "Test Controller", group = "Test")
public class TestController extends LinearOpMode {
    EverglowGamepad everglowGamepad1 = new EverglowGamepad(gamepad1);
    EverglowGamepad everglowGamepad2 = new EverglowGamepad(gamepad2);

    @Override
    public void runOpMode() {
        waitForStart();
        boolean toggleX = false;
        boolean toggleUp = false;
        boolean toggleRt = false;
        boolean toggleLb = false;
        while (opModeIsActive()) {
            everglowGamepad1.update();
            everglowGamepad2.update();
            if (everglowGamepad2.x()){
                toggleX = !toggleX;
            }
            if (everglowGamepad2.dpad_left()){
                toggleUp = !toggleUp;
            }
            if (everglowGamepad2.rt()){
                toggleRt = !toggleRt;
            }
            if (everglowGamepad2.lb()){
                toggleLb = !toggleLb;
            }
            telemetry.addData("toggleX: ", toggleX);
            telemetry.addData("toggleUp: ", toggleUp);
            telemetry.addData("toggleRt: ", toggleRt);
            telemetry.addData("toggleLb: ", toggleLb);
            telemetry.addLine("Gamepad1:");
            printGamepadInfo(gamepad1);
            telemetry.addLine("Gamepad2:");
            printGamepadInfo(gamepad2);
            telemetry.update();
        }
    }

    private void printGamepadInfo(Gamepad gamepad) {
        telemetry.addLine(String.format("Left (%f, %f)", gamepad.left_stick_x, gamepad.left_stick_y));
        telemetry.addLine(String.format("Right (%f, %f)", gamepad.right_stick_x, gamepad.right_stick_y));

        List<String> pressedButtons = new ArrayList<>();
        if (gamepad.x) {
            pressedButtons.add("X");
        }
        if (gamepad.y) {
            pressedButtons.add("Y");
        }
        if (gamepad.a) {
            pressedButtons.add("A");
        }
        if (gamepad.b) {
            pressedButtons.add("B");
        }
        if (gamepad.left_bumper) {
            pressedButtons.add("Left Bumper");
        }
        if (gamepad.right_bumper) {
            pressedButtons.add("Right Bumper");
        }
        if (gamepad.left_trigger > 0.1) {
            pressedButtons.add("Left Trigger");
        }
        if (gamepad.right_trigger > 0.1) {
            pressedButtons.add("Right Trigger");
        }
        if (gamepad.left_stick_button) {
            pressedButtons.add("Left Stick Button");
        }
        if (gamepad.right_stick_button) {
            pressedButtons.add("Right Stick Button");
        }
        if (gamepad.dpad_up) {
            pressedButtons.add("Dpad Up");
        }
        if (gamepad.dpad_down) {
            pressedButtons.add("Dpad Down");
        }
        if (gamepad.dpad_left) {
            pressedButtons.add("Dpad Left");
        }
        if (gamepad.dpad_right) {
            pressedButtons.add("Dpad Right");
        }

        String pressedButtonNames = joinString(", ", pressedButtons);
        telemetry.addLine(String.format("Buttons Pressed: %s", pressedButtonNames));
    }

    private static String joinString(String separator, List<String> values) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String value : values) {
            if (!first) {
                sb.append(separator);
            }
            sb.append(value);
            first = false;
        }
        return sb.toString();
    }

}
