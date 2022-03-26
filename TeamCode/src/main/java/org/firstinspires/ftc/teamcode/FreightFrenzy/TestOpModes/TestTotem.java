package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem;

@TeleOp(name = "Totem Test", group = "Linear Opmode")
@Disabled
public class TestTotem extends LinearOpMode {

    private TotemSystem totemSystem;

    @Override
    public void runOpMode() {
        totemSystem = new TotemSystem(this,false);

        waitForStart();

        while (opModeIsActive()) {
            double aziPower = gamepad1.right_stick_x / 1000;
            double altPower = -gamepad1.left_stick_y / 1000;

            if (gamepad1.right_stick_button) {
                aziPower /= 2;
            }
            if (gamepad1.left_stick_button) {
                altPower /= 2;
            }

            totemSystem.moveAzimuth(aziPower);
            totemSystem.moveAltitude(altPower);

            if (gamepad1.dpad_right) {
                totemSystem.extend(-1);
            } else if (gamepad1.dpad_left) {
                totemSystem.extend(1);
            } else {
                totemSystem.stop();
            }

            telemetry.addData("Azimuth: ", totemSystem.azimuth.getPosition());
            telemetry.addData("Altitude: ", totemSystem.altitude.getPosition());
            telemetry.update();
        }
    }
}
