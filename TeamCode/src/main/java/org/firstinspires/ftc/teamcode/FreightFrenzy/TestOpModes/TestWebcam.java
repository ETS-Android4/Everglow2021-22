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

@TeleOp(name = "Test Webcam", group = "Test")
public class TestWebcam extends LinearOpMode {

    @Override
    public void runOpMode() {
        EverglowGamepad gamepad = new EverglowGamepad(gamepad1);
        CameraSystem3 cameraSystem = new CameraSystem3(this);
        waitForStart();
        while (opModeIsActive()){
            gamepad.update();
            if (gamepad.square()){
                cameraSystem.captureImage();
                telemetry.addLine("Captured Image");
                telemetry.update();
            }
            if (gamepad.circle()){
                ArmSystem.Floors floor = cameraSystem.detectTotem();
                telemetry.addData("Floor", floor);
                telemetry.update();
            }

        }

    }
}


