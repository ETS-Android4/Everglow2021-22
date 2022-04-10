package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.AndroidUtils;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils.Side;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.io.File;

public class CameraPreview {

    private static final int RECT_X_INCREASE = 0;
    private static final int RECT_Y_INCREASE = 0;

    class NewPipeline extends OpenCvPipeline{
        private ImageProcessor imageProcessor;

        @Override
        public void init(Mat mat) {
            imageProcessor = new ImageProcessor(mat, side);
        }

        @Override
        public Mat processFrame(Mat input) {
            imageProcessor.drawOnPreview(input);
            return input;
        }
    }

    class CameraPipeline extends OpenCvPipeline {

        private ImageProcessor imageProcessor;
        private final Side side;

        private boolean isCapturingImage = false;
        private boolean isDetectingTotem = false;

        @Nullable
        private ArmSystem.Floors floor = null;

        private Mat hsvAll;
        private Mat mask1;
        private Mat mask2;
        private Mat coloredMask;

        CameraPipeline(Side side) {
            this.side = side;
        }


        public void captureImage() {
            isCapturingImage = true;
        }

        public ArmSystem.Floors detectTotem() {
            floor = null;
            isDetectingTotem = true;
            while (opMode.opModeIsActive() && floor == null) {
                // sample
            }
            return floor;
        }

        @Override
        public void init(Mat mat) {
            imageProcessor = new ImageProcessor(mat, side);
        }

        @Override
        public Mat processFrame(Mat input) {
            if (isCapturingImage) {
                isCapturingImage = false;
                String timeStamp = AndroidUtils.timestampString();
                String filepath = new File(AppUtil.ROBOT_DATA_DIR, String.format("img_%s.png", timeStamp)).getAbsolutePath();
                saveMatToDiskFullPath(input, filepath);
            }
            if (isDetectingTotem) {
                this.floor = imageProcessor.findFloor(input);
            }
            imageProcessor.drawOnPreview(input);

            return input;
        }
    }

    private final LinearOpMode opMode;
    private final Side side;
    private final CameraPipeline cameraPipeline;
    private final OpenCvCamera camera;

    public CameraPreview(LinearOpMode opMode, Side side) {
        this.side = side;
        this.opMode = opMode;
        cameraPipeline = new CameraPipeline(side);
        int cameraMonitorViewId = this.opMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", this.opMode.hardwareMap.appContext.getPackageName());
        WebcamName webcamName = this.opMode.hardwareMap.get(WebcamName.class, "webcam");
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        camera.setPipeline(cameraPipeline);
        startCamera();
    }

    private void startCamera() {
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(1920, 1080, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                throw new RuntimeException("Camera failed with code: " + errorCode);
            }
        });
    }


    public void cleanup(){
        camera.closeCameraDeviceAsync(() -> {});
    }

    public void captureImage() {
        cameraPipeline.captureImage();
    }

    public ArmSystem.Floors detectTotem() {
        ArmSystem.Floors floor = cameraPipeline.detectTotem();
        return floor;
    }
}
