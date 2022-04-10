package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AllSystems;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.CameraSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

@TeleOp(name = "Test Webcam", group = "Test")
public class TestWebcam extends LinearOpMode {

    @Override
    public void runOpMode() {
        AllSystems systems = AllSystems.init(this, MathUtils.Side.BLUE);
        CameraSystem cameraSystem = systems.cameraSystem;
        TimeUtils.opMode = this;
        EverglowGamepad gamepad = new EverglowGamepad(gamepad1);
        int frameNum = 1;
        waitForStart();
        while (opModeIsActive()){
            gamepad.update();
            if (gamepad.square()){
                cameraSystem.captureImage();
                telemetry.addLine("Captured Image");
                telemetry.update();
            }
            ElapsedTime elapsedTime = new ElapsedTime();
            ArmSystem.Floors floor = cameraSystem.detectTotem();
            telemetry.addData("Floor: ", floor);
            telemetry.addData("frameNum", frameNum++);
            telemetry.addData("Time", elapsedTime.seconds());
            telemetry.update();
            TimeUtils.sleep(1000);
        }

    }
}


