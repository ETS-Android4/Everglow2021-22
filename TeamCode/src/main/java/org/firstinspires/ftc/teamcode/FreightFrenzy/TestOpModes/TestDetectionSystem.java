package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

@TeleOp(name = "Test Detection System", group = "Test")
public class TestDetectionSystem extends LinearOpMode {

    // when the right stick is pressed on the controller, make the rotation slower by this factor.
    DrivingSystem   drivingSystem;
    ArmSystem       armSystem;
    DuckSystem      duckSystem;
    EverglowGamepad ourGamepad1;
    EverglowGamepad ourGamepad2;
    TouchSensor     touch;
    DetectionSystem detectionSystem;

    @Override
    public void runOpMode() {
        drivingSystem = new DrivingSystem(this);
        armSystem     = new ArmSystem(this);
        duckSystem    = new DuckSystem(this);
        detectionSystem = new DetectionSystem(this, armSystem);
        ourGamepad1   = new EverglowGamepad(gamepad1);
        ourGamepad2   = new EverglowGamepad(gamepad2);
        touch         = hardwareMap.get(TouchSensor.class, "touch");

        waitForStart();
        int i = 1;
        int numFirst =0;
        int numSecond = 0;
        int numThird = 0;
        while (opModeIsActive()) {
            ArmSystem.Floors floor = detectionSystem.findTargetFloor2(1);
            switch (floor){
                case FIRST:
                    numFirst++;
                    break;
                case SECOND:
                    numSecond++;
                    break;
                case THIRD:
                    numThird++;
                    break;
            }
            telemetry.addData("Run: ", i);
            telemetry.addData("First: ", numFirst);
            telemetry.addData("Second: ", numSecond);
            telemetry.addData("Third: ", numThird);
            telemetry.update();
            i++;
            TimeUtils.sleep(50);
        }
    }

    private void testDriveUntilObstacle() {
        drivingSystem.driveUntilObstacle(60, 0.4);
    }

    private void testSimpleDetection(){
        detectionSystem.reset();
        ArmSystem.Floors targetFloor = detectionSystem.findTargetFloor2(1);
        telemetry.addData("targetFloor", targetFloor);
        telemetry.update();

        TimeUtils.sleep(1000);
    }

    private void testAdvancedDetection(){
        detectionSystem.reset();
        ArmSystem.Floors targetFloor = detectionSystem.smartFindTargetFloor(0.2, 2, drivingSystem);
        telemetry.addData("targetFloor", targetFloor);
        telemetry.update();

        TimeUtils.sleep(1000);
    }
}
