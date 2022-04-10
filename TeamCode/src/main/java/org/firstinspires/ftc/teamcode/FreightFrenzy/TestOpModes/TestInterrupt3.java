package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.CameraSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;

@Autonomous(name = "Test Interrupt 3", group = "Test")
public class TestInterrupt3 extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DuckSystem duckSystem = new DuckSystem(this);

        CameraSystem cameraSystem = new CameraSystem(this, MathUtils.Side.RED);
        waitForStart();
        ArmSystem.Floors floor = cameraSystem.detectTotem();
        telemetry.addData("Floor: ", floor);
        telemetry.update();
        cameraSystem.saveBitmap(cameraSystem.getFrame());
        duckSystem.run();
        sleep(15000);
        duckSystem.stop();
    }
}
