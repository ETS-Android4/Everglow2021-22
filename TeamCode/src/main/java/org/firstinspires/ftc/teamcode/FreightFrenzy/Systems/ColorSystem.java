package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

public class ColorSystem {
    public ColorSensor color;
    public ColorSensor arm;
    LinearOpMode opMode;

    public ColorSystem(LinearOpMode opMode) {
        color = opMode.hardwareMap.get(ColorSensor.class, "color");
        arm = opMode.hardwareMap.get(ColorSensor.class, "arm_color_sensor");
        color.enableLed(true);
        arm.enableLed(true);
        this.opMode = opMode;
    }

    public boolean overWhite() {
        float[] hsv = {0, 0, 0};
        Color.RGBToHSV(color.red(), color.green(), color.blue(), hsv);
        if (hsv[1] < 0.25) {
            this.opMode.telemetry.addLine("WHITE!");
//            this.opMode.telemetry.update();
            return true;
        }
        return false;
    }

    public boolean isCargo() {
        float[] hsv = {0, 0, 0};
        Color.RGBToHSV(arm.red(), arm.green(), arm.blue(), hsv);
        if (hsv[1] < 0.25) {
            this.opMode.telemetry.addLine("CARGO!");
//            this.opMode.telemetry.update();
            return true;
        }
        return false;
    }
}
