package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem.Floors;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.AndroidUtils;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils.Side;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;
import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class CameraSystem {

    private static final int RECT_X_INCREASE = 0;
    private static final int RECT_Y_INCREASE = 0;

    class NewPipeline extends OpenCvPipeline {
        private ImageProcessor imageProcessor;

        @Override
        public void init(Mat mat) {
            imageProcessor = new ImageProcessor(mat, side, opMode);
        }

        @Override
        public Mat processFrame(Mat input) {
            imageProcessor.drawOnPreviewAndDetect(input);
            return input;
        }
    }

    class CameraPipeline extends OpenCvPipeline {

        private ImageProcessor imageProcessor;
        private final Side side;

        private boolean isCapturingImage = false;

        private final AtomicReference<Floors> floorShared = new AtomicReference<>(null);

        CameraPipeline(Side side) {
            this.side = side;
        }


        public void captureImage() {
            isCapturingImage = true;
        }

        public Floors detectTotem() {
            Floors floor = null;
            while (!opMode.isStopRequested()) {
                floor = floorShared.get();
                if (floor != null){
                    break;
                }
                TimeUtils.sleep(1);
            }
            return floor;
        }

        @Override
        public void init(Mat mat) {
            imageProcessor = new ImageProcessor(mat, side, opMode);
        }

        @Override
        public Mat processFrame(Mat input) {
            if (isCapturingImage) {
                isCapturingImage = false;
                String timeStamp = AndroidUtils.timestampString();
                String filepath = new File(AppUtil.ROBOT_DATA_DIR, String.format("img_%s.png", timeStamp)).getAbsolutePath();
                saveMatToDiskFullPath(input, filepath);
            }
            Floors floor = imageProcessor.drawOnPreviewAndDetect(input);
            this.floorShared.set(floor);
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


    public void cleanup() {
        camera.closeCameraDeviceAsync(() -> {
        });
    }

    public void captureImage() {
        cameraPipeline.captureImage();
    }

    public Floors detectTotem() {
        Floors floor = cameraPipeline.detectTotem();
        new Thread(()-> {
            camera.stopStreaming();
            camera.closeCameraDevice();
        }).start();
        return floor;
    }
}
