package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

public class TestTotem  extends LinearOpMode {

    private TotemSystem totemSystem;

    @Override
    public void runOpMode() {
        totemSystem = new TotemSystem(this);
        double azi;
        double alt;
        double meter;

        waitForStart();

        while (opModeIsActive()) {
            azi = totemSystem.azimuth.getPosition();
            alt = totemSystem.altitude.getPosition();
            meter = totemSystem.meter.getPosition();

            totemSystem.setAzimuth(azi + gamepad1.right_stick_x / 10);
            totemSystem.setAltitude(alt - gamepad1.left_stick_y / 10);
            if (gamepad1.dpad_right) {
                totemSystem.extend(meter + 0.1);
            }

            telemetry.addData("Azimuth: ", azi);
            telemetry.addData("Altitude: ", alt);
            telemetry.addData("Meter: ", meter);
            telemetry.update();
        }
    }
}
