package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Crater;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.CameraSystem2;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.CameraSystem3;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

@TeleOp(name = "Test Webcam", group = "Test")
public class TestWebcam extends LinearOpMode {

    @Override
    public void runOpMode() {
        EverglowGamepad gamepad = new EverglowGamepad(gamepad1);
        CameraSystem3 cameraSystem = new CameraSystem3(this);
        waitForStart();
        int frameNum = 0;
        while (opModeIsActive()){
            if(gamepad.square()){
                Bitmap bmp = cameraSystem.getFrame();
                telemetry.addLine("Saving Frame: " + frameNum++);
                if (bmp != null){
                    telemetry.addLine("Bmp is not null");
                    cameraSystem.saveBitmap(bmp);
                }
                telemetry.update();
            }
            gamepad.update();

        }

    }
}


