package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

@TeleOp(name = "Totem Test", group = "Linear Opmode")
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

            totemSystem.setAzimuth(azi + gamepad1.right_stick_x / 600);
            totemSystem.setAltitude(alt - gamepad1.left_stick_y / 600);
            if (gamepad1.dpad_right) {
                totemSystem.extend(meter + 0.001);
            }
            if (gamepad1.dpad_left) {
                totemSystem.extend(meter - 0.001);
            }

            telemetry.addData("Azimuth: ", azi);
            telemetry.addData("Altitude: ", alt);
            telemetry.addData("Meter: ", meter);
            telemetry.update();
        }
    }
}
