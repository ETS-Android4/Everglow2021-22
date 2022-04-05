package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.collections.EvictingBlockingQueue;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.Utils;
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
        private static Rect createRect(int x, int y, int width, int height){
            return new Rect(x - rectXIncrease / 2, y - rectYIncrease, width + rectXIncrease, height + rectYIncrease);
        }
        private static final Rect leftArea = createRect(503, 491, 229, 234);
        private static final Rect centerArea = createRect(903, 484, 300, 218);
        private static final Rect rightArea = createRect(1376, 463, 249, 232);

        private static final Scalar low_blue = new Scalar(94, 80, 2);
        private static final Scalar high_blue = new Scalar(126, 255, 255);

        private static final Scalar low_red = new Scalar(161, 155, 84);
        private static final Scalar high_red = new Scalar(179, 255, 255);

        private static final int PIXEL_COUNT_THRESHOLD = 2000;

        private boolean isCapturingImage = false;
        private boolean isDetectingTotem = false;
        private final OpMode opMode;

        private final EvictingBlockingQueue<ArmSystem.Floors> floorResult = new EvictingBlockingQueue<ArmSystem.Floors>(new ArrayBlockingQueue<ArmSystem.Floors>(1));

        private final Mat hsvAll = new Mat();
        private final Mat redMask = new Mat();
        private final Mat blueMask = new Mat();
        private final Mat coloredMask = new Mat();


        CameraPipeline(OpMode opMode) {
            this.opMode = opMode;
        }

        public void captureImage() {
            isCapturingImage = true;
        }

        public ArmSystem.Floors detectTotem(){
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
        public Mat processFrame(Mat input) {
            if (isCapturingImage) {
                isCapturingImage = false;
                String timeStamp = Utils.timestampString();
                String filepath = new File(AppUtil.ROBOT_DATA_DIR, String.format("img_%s.png", timeStamp)).getAbsolutePath();
                saveMatToDiskFullPath(input, filepath);
            }

            Imgproc.cvtColor(input, hsvAll, Imgproc.COLOR_RGB2HSV);
            Core.inRange(hsvAll, low_blue, high_blue, redMask);
            Core.inRange(hsvAll, low_red, high_red, blueMask);
            Core.bitwise_or(redMask, blueMask, coloredMask);


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
                if(leftPixels < rightPixels && leftPixels < centerPixels){
                    floor = ArmSystem.Floors.FIRST;
                }else if (centerPixels < leftPixels && centerPixels < rightPixels){
                    floor = ArmSystem.Floors.SECOND;
                }else {
                    floor = ArmSystem.Floors.THIRD;
                }
                floorResult.offer(floor);
            }

            input.setTo(new Scalar(251, 72, 196), coloredMask);

            Imgproc.rectangle(input, leftArea, new Scalar(0,255,0), 7);
            Imgproc.rectangle(input, centerArea, new Scalar(0,255,0), 7);
            Imgproc.rectangle(input, rightArea, new Scalar(0,255,0), 7);

            return input;


//            Imgproc.cvtColor(input, grey, Imgproc.COLOR_RGB2GRAY);
//            Imgproc.blur(grey, blurred, new Size(5, 5));
//            Imgproc.Canny(blurred, edges, 25, 50);
//            Core.hconcat(Arrays.asList(grey, edges), combined);
//            Imgproc.resize(combined, combinedResized, input.size(), 0, 0, Imgproc.INTER_CUBIC);
//            return combinedResized;
        }

//        private boolean hasTotemHsv(Mat input){
//            Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);
//            Core.inRange(hsv, low_blue, high_blue, redMask);
//            Core.inRange(hsv, low_red, high_red, blueMask);
//            int numPixels = Core.countNonZero(redMask) + Core.countNonZero(blueMask);
//            return numPixels < PIXEL_COUNT_THRESHOLD;
//        }

//        public boolean hasTotem(Mat input) {
//            Imgproc.blur(input, blurred, new Size(5, 5));
//            Imgproc.Canny(blurred, edges, 50, 100);
//            Imgproc.blur(edges, edgesBlurred, new Size(5, 5));
//            Imgproc.findContours(edgesBlurred, countours, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
//            for (MatOfPoint contour: countours){
//                MatOfPoint2f contour2f = new MatOfPoint2f(contour);
//                MatOfPoint2f aproxCurve = new MatOfPoint2f();
//                double perimeter = Imgproc.arcLength(contour2f, true);
//                Imgproc.approxPolyDP(contour2f, aproxCurve, 0.075 * perimeter, true);
//                double contourArea = Imgproc.contourArea(aproxCurve);
//                if (contourArea > 4000){
//                    return true;
//                }
//            }
//            return false;
//        }


    }

    private final OpMode opMode;
    private final CameraPipeline cameraPipeline;

    public CameraSystem3(OpMode opMode) {
        this.opMode = opMode;
        cameraPipeline = new CameraPipeline(opMode);
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

    public ArmSystem.Floors detectTotem(){
        return cameraPipeline.detectTotem();
    }
}
