package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import android.graphics.ImageFormat;
import android.os.Handler;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSession;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCharacteristics;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraManager;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.network.CallbackLooper;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

public class CameraSystem {
    private OpMode opMode;

    private final Handler callbackHandler;
    private final CameraManager cameraManager;
    private final CameraName cameraName;
    private final Camera camera;
    private CameraCaptureSession cameraCaptureSession;

    public CameraSystem(OpMode opMode) {
        this.opMode = opMode;
        cameraManager = ClassFactory.getInstance().getCameraManager();
        callbackHandler = CallbackLooper.getDefault().getHandler();
        cameraName = opMode.hardwareMap.get(WebcamName.class, "webcam");
        camera = openCamera();

    }

    private Camera openCamera(){
            return cameraManager.requestPermissionAndOpenCamera(new Deadline(Integer.MAX_VALUE, TimeUnit.SECONDS), cameraName, null);
    }

    private void startCameraSession(){
        final int imageFormat = ImageFormat.YUY2;
        CameraCharacteristics cameraCharacteristics = cameraName.getCameraCharacteristics();
        final Size size = cameraCharacteristics.getDefaultSize(imageFormat);
        final int fps = cameraCharacteristics.getMaxFramesPerSecond(imageFormat, size);

    }

}
