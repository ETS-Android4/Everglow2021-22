package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.collections.EvictingBlockingQueue;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.Utils;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils.Side;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;
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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class CameraSystem3 {
    static class CameraPipeline extends OpenCvPipeline {
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


        private boolean isCapturingImage = false;
        private boolean isDetectingTotem = false;
        private final OpMode opMode;

        private final EvictingBlockingQueue<ArmSystem.Floors> floorResult = new EvictingBlockingQueue<ArmSystem.Floors>(new ArrayBlockingQueue<ArmSystem.Floors>(1));

        private Mat hsvAll;
        private Mat mask1;
        private Mat mask2;
        private Mat coloredMask;

        private final Side side;

        CameraPipeline(OpMode opMode, Side side) {
            this.opMode = opMode;
            this.side = side;

        }

        public void captureImage() {
            isCapturingImage = true;
        }

        public ArmSystem.Floors detectTotem() {
            floorResult.clear();
            isDetectingTotem = true;
            try {
                return floorResult.poll(Integer.MAX_VALUE, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
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
                ArmSystem.Floors floor;
                if (leftPixels < rightPixels && leftPixels < centerPixels) {
                    floor = ArmSystem.Floors.FIRST;
                } else if (centerPixels < leftPixels && centerPixels < rightPixels) {
                    floor = ArmSystem.Floors.SECOND;
                } else {
                    floor = ArmSystem.Floors.THIRD;
                }
                floorResult.offer(floor);
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
            opMode.telemetry.addData("cvtColorTime", cvtColorTime);
            opMode.telemetry.addData("inRangeTime", inRangeTime);
            opMode.telemetry.addData("chooseLocationTime", chooseLocationTime);
            opMode.telemetry.addData("drawTime", drawTime);
            opMode.telemetry.update();
            TimeUtils.sleep(5000);
            return input;
        }
    }

    private final OpMode opMode;
    private final CameraPipeline cameraPipeline;

    public CameraSystem3(OpMode opMode, Side side) {
        this.opMode = opMode;
        cameraPipeline = new CameraPipeline(opMode, side);
        startCamera();
    }

    private void startCamera() {
        int cameraMonitorViewId = opMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", opMode.hardwareMap.appContext.getPackageName());
        WebcamName webcamName = opMode.hardwareMap.get(WebcamName.class, "webcam");
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
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

    public void captureImage() {
        cameraPipeline.captureImage();
    }

    public ArmSystem.Floors detectTotem() {
        ArmSystem.Floors floor = cameraPipeline.detectTotem();
        return floor;
    }
}
