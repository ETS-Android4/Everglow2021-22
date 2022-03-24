package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;

@TeleOp(name = "TestRotationAroundDuck", group = "Test Opmode")
public class TestRotationAroundDuck extends LinearOpMode {

    DrivingSystem drivingSystem;
    ArmSystem armSystem;

    @Override
    public void runOpMode() {
        drivingSystem = new DrivingSystem(this);
        armSystem = new ArmSystem(this);

        waitForStart();
        while (opModeIsActive()) {
            drivingSystem.rotateAroundArm(gamepad1.right_stick_x);
            if (gamepad1.a) {
                armSystem.moveArm(ArmSystem.Floors.CAROUSEL);
            }
        }
    }
}

