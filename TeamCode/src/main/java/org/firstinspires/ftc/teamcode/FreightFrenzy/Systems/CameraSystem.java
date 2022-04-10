package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import android.media.Image;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.androidUtils;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils.Side;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.io.File;

public class CameraSystem {

    private static final int rectXIncrease = 20;
    private static final int rectYIncrease = 100;

    private static Rect createRect(int x, int y, int width, int height) {
        return new Rect(x - rectXIncrease / 2, y - rectYIncrease, width + rectXIncrease, height + rectYIncrease);
    }

    private static final Rect leftArea = createRect(503, 491, 229, 234);
    private static final Rect centerArea = createRect(903, 484, 300, 218);
    private static final Rect rightArea = createRect(1376, 463, 249, 232);

    private static final Scalar low_blue = new Scalar(94, 80, 2);
    private static final Scalar high_blue = new Scalar(126, 255, 255);

    private static final Scalar low_red1 = new Scalar(151, 80, 2);
    private static final Scalar high_red1 = new Scalar(180, 255, 255);
    private static final Scalar low_red2 = new Scalar(0, 80, 2);
    private static final Scalar high_red2 = new Scalar(15, 255, 255);

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
                String timeStamp = androidUtils.timestampString();
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

    public CameraSystem(LinearOpMode opMode, Side side) {
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
