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
            colorSystem.overWhite();
            colorSystem.isCargo();
            telemetry.update();
        }
    }
}
