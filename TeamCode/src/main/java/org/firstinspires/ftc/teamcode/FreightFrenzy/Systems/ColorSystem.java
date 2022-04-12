package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import android.graphics.Color;

import androidx.core.graphics.ColorUtils;

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

    public float bottomSensorLightness(){
        float[] hsl = {0, 0, 0};
        ColorUtils.RGBToHSL(color.red(), color.green(), color.blue(), hsl);
        return hsl[2];
    }

    public float cargoSensorLightness(){
        float[] hsl = {0, 0, 0};
        ColorUtils.RGBToHSL(arm.red(), color.green(), color.blue(), hsl);
        return hsl[2];
    }

    public boolean overWhite() {

        float lightness = bottomSensorLightness();

        return lightness > 0.25;
    }

    public boolean isCargo() {
        float[] hsv = {0, 0, 0};
        Color.RGBToHSV(arm.red(), arm.green(), arm.blue(), hsv);
        if (hsv[1] < 0.25) {
            return true;
        }
        return false;
    }
}
