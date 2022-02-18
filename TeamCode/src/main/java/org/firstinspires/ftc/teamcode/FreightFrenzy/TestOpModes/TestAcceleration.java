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

    @Override
    public void runOpMode() {
        drivingSystem = new DrivingSystem(this);

        waitForStart();
        while (opModeIsActive()) {
            Acceleration acceleration = drivingSystem.getAcceleration();
            telemetry.addData("acceleration", acceleration.toString());
            telemetry.addData("acceleration magnitude", drivingSystem.getAccelerationMagnitude());
            telemetry.addData("Current Angle: ", drivingSystem.getCurrentAngle());
            telemetry.addData("Current Angle zxy: ", drivingSystem.getFullAngle());
        }
    }

}
