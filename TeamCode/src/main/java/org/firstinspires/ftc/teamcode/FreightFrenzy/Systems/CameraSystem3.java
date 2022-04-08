package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AllSystems;
import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.Utils;
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

public class CameraSystem3 {

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


    class CameraPipeline extends OpenCvPipeline {

        private boolean isCapturingImage = false;
        private boolean isDetectingTotem = false;

        @Nullable
        private ArmSystem.Floors floor = null;

        private Mat hsvAll;
        private Mat mask1;
        private Mat mask2;
        private Mat coloredMask;


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
            hsvAll = new Mat(mat.rows(), mat.cols(), mat.type());
            mask1 = new Mat(mat.rows(), mat.cols(), mat.type());
            mask2 = new Mat(mat.rows(), mat.cols(), mat.type());
            coloredMask = new Mat(mat.rows(), mat.cols(), mat.type());
        }

        @Override
        public Mat processFrame(Mat input) {
            if (!opMode.opModeIsActive()){
                systems.cleanup();
                camera.closeCameraDeviceAsync(()->{});
                Thread.currentThread().interrupt();
                return null;
            }
            if (isCapturingImage) {
                isCapturingImage = false;
                String timeStamp = Utils.timestampString();
                String filepath = new File(AppUtil.ROBOT_DATA_DIR, String.format("img_%s.png", timeStamp)).getAbsolutePath();
                saveMatToDiskFullPath(input, filepath);
            }
            ElapsedTime elapsedTime = new ElapsedTime();
            Imgproc.cvtColor(input, hsvAll, Imgproc.COLOR_RGB2HSV);
            double cvtColorTime = elapsedTime.seconds();
            elapsedTime.reset();
            if (side == Side.RED) {
                Core.inRange(hsvAll, low_red1, high_red1, mask1);
                Core.inRange(hsvAll, low_red2, high_red2, mask2);
                Core.bitwise_or(mask1, mask2, coloredMask);
            } else {
                Core.inRange(hsvAll, low_blue, high_blue, coloredMask);
            }
            double inRangeTime = elapsedTime.seconds();
            elapsedTime.reset();


            if (isDetectingTotem) {
                isDetectingTotem = false;
                Mat left = coloredMask.submat(leftArea);
                Mat center = coloredMask.submat(centerArea);
                Mat right = coloredMask.submat(rightArea);

                int leftPixels = Core.countNonZero(left);
                int centerPixels = Core.countNonZero(center);
                int rightPixels = Core.countNonZero(right);

                // find the area with the fewest colored
                ArmSystem.Floors floorResult;
                if (leftPixels < rightPixels && leftPixels < centerPixels) {
                    floorResult = ArmSystem.Floors.FIRST;
                } else if (centerPixels < leftPixels && centerPixels < rightPixels) {
                    floorResult = ArmSystem.Floors.SECOND;
                } else {
                    floorResult = ArmSystem.Floors.THIRD;
                }
                this.floor = floorResult;
            }
            double chooseLocationTime = elapsedTime.seconds();
            elapsedTime.reset();

            if (side == Side.RED) {
                input.setTo(new Scalar(72, 224, 251), coloredMask);
            } else {
                input.setTo(new Scalar(251, 72, 196), coloredMask);
            }

            Imgproc.rectangle(input, leftArea, new Scalar(0, 255, 0), 7);
            Imgproc.rectangle(input, centerArea, new Scalar(0, 255, 0), 7);
            Imgproc.rectangle(input, rightArea, new Scalar(0, 255, 0), 7);

            double drawTime = elapsedTime.seconds();
//            opMode.telemetry.addData("cvtColorTime", cvtColorTime);
//            opMode.telemetry.addData("inRangeTime", inRangeTime);
//            opMode.telemetry.addData("chooseLocationTime", chooseLocationTime);
//            opMode.telemetry.addData("drawTime", drawTime);
//            opMode.telemetry.update();
            return input;
        }
    }

    private final LinearOpMode opMode;
    private final Side side;
    private final CameraPipeline cameraPipeline;
    private final OpenCvCamera camera;
    private final AllSystems systems;

    public CameraSystem3(LinearOpMode opMode, Side side, AllSystems systems) {
        this.side = side;
        this.opMode = opMode;
        this.systems = systems;
        cameraPipeline = new CameraPipeline();
        int cameraMonitorViewId = this.opMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", this.opMode.hardwareMap.appContext.getPackageName());
        WebcamName webcamName = this.opMode.hardwareMap.get(WebcamName.class, "webcam");
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        startCamera();
    }

    private void startCamera() {
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(1920, 1080, OpenCvCameraRotation.UPRIGHT);
                camera.setPipeline(cameraPipeline);
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
