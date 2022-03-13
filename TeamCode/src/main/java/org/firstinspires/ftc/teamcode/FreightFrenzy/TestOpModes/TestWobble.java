package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

@TeleOp(name = "TestWobble", group = "Test")
@Disabled
public class TestWobble extends LinearOpMode {

    DrivingSystem   drivingSystem;

    @Override
    public void runOpMode() {
        drivingSystem = new DrivingSystem(this);
        waitForStart();

        while (opModeIsActive()) {
            drivingSystem.combinedDriveUntilCollect(100, 0.3);
            TimeUtils.sleep(3000);
            stop();
        }
    }
}
