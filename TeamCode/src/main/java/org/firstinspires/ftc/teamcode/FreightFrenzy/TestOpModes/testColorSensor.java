package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;
import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class testColorSensor extends LinearOpMode {
    // Define a variable for our color sensor
    ColorSensor color;

    @Override
    public void runOpMode() {
        // Get the color sensor from hardwareMap
        color = hardwareMap.get(ColorSensor.class, "Color");

        // Wait for the Play button to be pressed
        waitForStart();
        color.enableLed(true);
        // While the Op Mode is running, update the telemetry values.
        while (opModeIsActive()) {
            float[] hsv = {0, 0, 0};
            Color.RGBToHSV(color.red(),color.green(),color.blue(),hsv);
            if(hsv[1] < 0.25){
                telemetry.addLine("WHITE!");
            }
            telemetry.addData("hue",hsv[0]);
            telemetry.addData("sat", hsv[1]);
            telemetry.addData("value", hsv[2]);
            telemetry.addData("blue",color.blue());
            telemetry.addData("green", color.green());
            telemetry.addData("red", color.red());
            telemetry.update();
        }
    }
}// yet to write a function to detect white.
