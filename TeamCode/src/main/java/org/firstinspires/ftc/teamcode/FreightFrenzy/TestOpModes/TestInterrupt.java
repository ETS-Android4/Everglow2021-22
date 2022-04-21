package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;
import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name = "Test Interrupt", group = "Test")
@Disabled
public class TestInterrupt extends LinearOpMode {

    class MockCameraPipeline extends OpenCvPipeline{
        private final LinearOpMode opMode;

        MockCameraPipeline(LinearOpMode opMode) {
            this.opMode = opMode;
        }

        @Override
        public Mat processFrame(Mat input) {
            if (!opMode.opModeIsActive()){
                Thread.currentThread().interrupt();
            }
            return input;
        }
    }

    @Override
    public void runOpMode() {
        DuckSystem duckSystem = new DuckSystem(this);

        MockCameraPipeline pipeline = new MockCameraPipeline(this);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");
        OpenCvWebcam camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        camera.setPipeline(pipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(320,240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                throw new RuntimeException("Camera failed with code: " + errorCode);
            }
        });

        waitForStart();
        camera.stopStreaming();
        camera.closeCameraDevice();
        duckSystem.run();
        sleep(15000);
        duckSystem.stop();
    }
}
