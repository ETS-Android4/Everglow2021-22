package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes.webcam;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.CameraPreview;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils.Side;

@TeleOp(name = "Webcam Capture", group = "Webcam")
public class WebcamCapture extends LinearOpMode {


    @Override
    public void runOpMode() {
        CameraPreview cameraPreview = new CameraPreview(this, Side.RED);
        EverglowGamepad g1 = new EverglowGamepad(gamepad1);
        EverglowGamepad g2 = new EverglowGamepad(gamepad2);
        waitForStart();
        int i = 0;
        while (opModeIsActive()) {
            g1.update();
            g2.update();
            if (g1.cross() || g2.cross()) {
                cameraPreview.captureImage();
                i++;
            }
            telemetry.addData("Captures: ", i);
            telemetry.update();
        }
    }
}


