package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes.webcam;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.CameraSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils.Side;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public abstract class TestWebcamBase extends LinearOpMode {

    private final Side side;
    public TestWebcamBase(Side side) {
        this.side = side;
    }

    @Override
    public void runOpMode() {
        TotemSystem totemSystem = new TotemSystem(this);
        CameraSystem cameraSystem = new CameraSystem(this, side);
        EverglowGamepad gamepad = new EverglowGamepad(gamepad1);
        int frameNum = 1;
        waitForStart();
        while (opModeIsActive()){
            gamepad.update();
            ElapsedTime elapsedTime = new ElapsedTime();
            ArmSystem.Floors floor = cameraSystem.detectTotem();
            telemetry.addData("Floor: ", floor);
            telemetry.addData("frameNum", frameNum++);
            telemetry.addData("Time", elapsedTime.seconds());
            telemetry.update();
            TimeUtils.sleep(100);
        }

    }
}


