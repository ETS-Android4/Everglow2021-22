package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

@TeleOp(name = "Test Acceleration", group = "Test")
public class TestAcceleration extends LinearOpMode {

    // when the right stick is pressed on the controller, make the rotation slower by this factor.
    DrivingSystem   drivingSystem;
    EverglowGamepad gamepad;

    @Override
    public void runOpMode() {
        drivingSystem = new DrivingSystem(this);
        gamepad = new EverglowGamepad(gamepad1);

        waitForStart();
        while (opModeIsActive()) {
            gamepad.update();
            drivingSystem.driveByJoystick(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

            if (gamepad.a()) {
                telemetry.addLine("a");
                drivingSystem.driveStraight(50, 0.5, false);
                drivingSystem.driveUntilBumping(0.5);
            }

            Acceleration acceleration = drivingSystem.getAcceleration();
            telemetry.addData("acceleration", acceleration.toString());
            telemetry.addData("acceleration magnitude", drivingSystem.getAccelerationMagnitude());
            telemetry.addData("Current Angle: ", drivingSystem.getCurrentAngle());
            telemetry.addData("Angle Deviation: ", drivingSystem.getAngleDeviation());
            telemetry.addData("Current Angle zxy: ", drivingSystem.getFullAngle());
            telemetry.update();
        }
    }

}
