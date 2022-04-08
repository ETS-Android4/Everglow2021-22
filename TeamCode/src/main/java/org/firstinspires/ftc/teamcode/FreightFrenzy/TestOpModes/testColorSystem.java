package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ColorSystem;

@TeleOp(name = "Test Color System", group = "Test Opmode")
public class testColorSystem extends LinearOpMode {
    ColorSystem colorSystem;

    @Override
    public void runOpMode() {
        // Get the color sensor from hardwareMap
        colorSystem = new ColorSystem(this);

        // Wait for the Play button to be pressed
        waitForStart();

        while (opModeIsActive()) {
            float lightness = colorSystem.bottomSensorLightness();
            boolean overWhite = colorSystem.overWhite();
            boolean isCargo = colorSystem.isCargo();
            telemetry.addData("overWhite", overWhite);
            telemetry.addData("isCargo", isCargo);
            telemetry.addData("lightness", lightness);
            telemetry.update();
        }
    }
}
