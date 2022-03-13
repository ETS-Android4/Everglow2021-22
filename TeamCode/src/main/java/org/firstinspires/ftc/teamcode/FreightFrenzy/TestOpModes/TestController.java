package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name = "Test Controller", group = "Test")
@Disabled
public class TestController extends LinearOpMode {
    @Override
    public void runOpMode() {
        EverglowGamepad everglowGamepad1 = new EverglowGamepad(gamepad1);
        EverglowGamepad everglowGamepad2 = new EverglowGamepad(gamepad2);

        TouchSensor touch = hardwareMap.get(TouchSensor.class, "touch");


        waitForStart();
        int xTimesPressed = 0;
        int upTimesPressed = 0;
        int rtTimesPressed = 0;
        int lbTimesPressed = 0;
        int touchTimesPressed = 0;
        boolean prevTouchPressed = false;
        while (opModeIsActive()) {

            boolean touchPressed = touch.isPressed();
            boolean touchPressedNow = !prevTouchPressed && touchPressed;
            prevTouchPressed = touchPressed;
            everglowGamepad1.update();
            everglowGamepad2.update();
            if (everglowGamepad2.x()){
                xTimesPressed++;
            }
            if (everglowGamepad2.dpad_up()){
                upTimesPressed++;
            }
            if (everglowGamepad2.rt()){
                rtTimesPressed++;
            }
            if (everglowGamepad2.lb()){
                lbTimesPressed++;
            }
            if (touchPressedNow){
                touchTimesPressed++;
            }

            telemetry.addLine("Counting button pressed for gamepad 2");
            telemetry.addData("xTimesPressed: ", xTimesPressed);
            telemetry.addData("upTimesPressed: ", upTimesPressed);
            telemetry.addData("rtTimesPressed: ", rtTimesPressed);
            telemetry.addData("lbTimesPressed: ", lbTimesPressed);
            telemetry.addData("Touch Sensor down: ", touchPressed);
            telemetry.addData("Touch sensor num pressed: ", touchTimesPressed);
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
