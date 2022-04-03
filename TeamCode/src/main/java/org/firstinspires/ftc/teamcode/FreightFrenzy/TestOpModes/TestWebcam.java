package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Crater;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.CameraSystem2;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.CameraSystem3;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

@TeleOp(name = "Test Webcam", group = "Test")
public class TestWebcam extends LinearOpMode {

    @Override
    public void runOpMode() {
        EverglowGamepad gamepad = new EverglowGamepad(gamepad1);
        CameraSystem3 cameraSystem = new CameraSystem3(this);
        int frameNum = 1;
        waitForStart();
        while (opModeIsActive()){
            gamepad.update();
            if (gamepad.square()){
                cameraSystem.captureImage();
                telemetry.addLine("Captured Image");
                telemetry.update();
            }
            ArmSystem.Floors floor = cameraSystem.detectTotem();
            telemetry.addData("Floor: ", floor);
            telemetry.addData("frameNum", frameNum++);
            telemetry.update();
            TimeUtils.sleep(500);
        }

    }
}


