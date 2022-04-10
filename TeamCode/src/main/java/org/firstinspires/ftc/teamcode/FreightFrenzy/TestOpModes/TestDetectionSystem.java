package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.CameraSystem2;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;
@Disabled
@TeleOp(name = "Test Detection System", group = "Test")
public class TestDetectionSystem extends LinearOpMode {


    @Override
    public void runOpMode() {

        CameraSystem2 cameraSystem2 = new CameraSystem2(this, MathUtils.Side.BLUE);

        waitForStart();
        int i = 1;
        int numFirst =0;
        int numSecond = 0;
        int numThird = 0;
        while (opModeIsActive()) {
            ArmSystem.Floors floor = cameraSystem2.findFloor();
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
}
