package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

@TeleOp(name = "Totem Test", group = "Linear Opmode")
public class TestTotem  extends LinearOpMode {

    private TotemSystem totemSystem;
    private EverglowGamepad gamepad;

    @Override
    public void runOpMode() {
        totemSystem = new TotemSystem(this);
        gamepad = new EverglowGamepad(gamepad1);

        waitForStart();

        while (opModeIsActive()) {
            gamepad.update();
            totemSystem.moveAzimuth(gamepad1.right_stick_x / 3000);
            totemSystem.moveAltitude(-gamepad1.left_stick_y / 3000);
            if (gamepad.dpad_right()) {
                totemSystem.meter.setPosition(0);
            }
            if (gamepad.dpad_left()) {
                totemSystem.extend(0.1);
            }

            if (gamepad1.a) {
                totemSystem.azimuth.setPosition(1);
            }
            if (gamepad1.b) {
                totemSystem.altitude.setPosition(1);
            }
            if (gamepad1.x) {
                totemSystem.meter.setPosition(1);
            }

            telemetry.addData("Azimuth: ", totemSystem.azimuth.getPosition());
            telemetry.addData("Altitude: ", totemSystem.altitude.getPosition());
            telemetry.addData("Meter: ", totemSystem.meter.getPosition());
            telemetry.update();
        }
    }
}
