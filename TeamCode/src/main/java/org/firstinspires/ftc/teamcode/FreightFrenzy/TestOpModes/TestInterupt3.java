package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.CameraSystem2;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;
import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name = "Test Interupt 3", group = "Test")
public class TestInterupt3 extends LinearOpMode {

    @Override
    public void runOpMode() {
        TimeUtils.opMode = this;
        DuckSystem duckSystem = new DuckSystem(this);

        CameraSystem2 cameraSystem2 = new CameraSystem2(this, MathUtils.Side.RED);
        waitForStart();
        ArmSystem.Floors floor = cameraSystem2.findFloor();
        telemetry.addData("Floor: ", floor);
        duckSystem.run();
        sleep(15000);
        duckSystem.stop();
    }
}
