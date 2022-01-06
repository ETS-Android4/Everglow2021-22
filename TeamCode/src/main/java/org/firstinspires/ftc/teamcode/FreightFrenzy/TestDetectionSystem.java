package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

@Autonomous(name = "Advanced Detection System Test", group = "Test")
public class TestDetectionSystem extends LinearOpMode {

    // when the right stick is pressed on the controller, make the rotation slower by this factor.
    DrivingSystem   drivingSystem;
    ArmSystem       armSystem;
    DuckSystem      duckSystem;
    EverglowGamepad ourGamepad1;
    EverglowGamepad ourGamepad2;
    TouchSensor     touch;
    DetectionSystem detectionSystem;
    int             counter = 0;

    @Override
    public void runOpMode() {
        drivingSystem = new DrivingSystem(this);
        armSystem     = new ArmSystem(this);
        duckSystem    = new DuckSystem(this);
        detectionSystem = new DetectionSystem(this);
        ourGamepad1   = new EverglowGamepad(gamepad1);
        ourGamepad2   = new EverglowGamepad(gamepad2);
        touch         = hardwareMap.get(TouchSensor.class, "touch");

        waitForStart();

        while (opModeIsActive()) {
            detectionSystem.reset();
            armSystem.autonomousMoveArm(ArmSystem.Floors.FIRST);
            TimeUtils.sleep(1000);
            drivingSystem.driveSidewaysAndScan(3, 0.1, detectionSystem);
            drivingSystem.driveSidewaysAndScan(6, -0.1, detectionSystem);
            drivingSystem.driveSidewaysAndScan(3, 0.1, detectionSystem);
            ArmSystem.Floors targetFloor = detectionSystem.findTargetFloorAfterScan();
            telemetry.addData("targetFloor", targetFloor);
            stop();
        }
    }
}
