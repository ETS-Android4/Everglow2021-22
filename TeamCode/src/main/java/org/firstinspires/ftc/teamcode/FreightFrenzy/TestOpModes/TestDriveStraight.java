package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AllSystems;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

@Autonomous(name = "TestInterupt", group = "Test")
@Disabled
public class TestDriveStraight extends LinearOpMode {
    @Override
    public void runOpMode() {
        AllSystems systems = AllSystems.init(this);

        waitForStart();

        systems.duckSystem.run();
        TimeUtils.sleep(10000);
        systems.duckSystem.stop();
        stop();
        systems.cleanup();
    }
}
