package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

public class CameraSystem3 {
     static class CameraPipeline extends OpenCvPipeline{
         Mat grey = new Mat();
         @Override
         public Mat processFrame(Mat input) {
             Imgproc.cvtColor(input, grey, Imgproc.COLOR_RGB2GRAY);
             return grey;
         }
     }

    private final OpMode opMode;

    public CameraSystem3(OpMode opMode) {
        this.opMode = opMode;
        startCamera();
    }

    private void startCamera(){
        int cameraMonitorViewId = opMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", opMode.hardwareMap.appContext.getPackageName());
        WebcamName webcamName = opMode.hardwareMap.get(WebcamName.class, "webcam");
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        camera.openCameraDevice();
        camera.startStreaming(1920, 1080, OpenCvCameraRotation.UPRIGHT);
        camera.setPipeline(new CameraPipeline());


    }
}
