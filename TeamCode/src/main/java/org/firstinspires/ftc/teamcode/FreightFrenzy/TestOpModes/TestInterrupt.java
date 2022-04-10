package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.AllSystems;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

@Autonomous(name = "Test Interrupt", group = "Test")
public class TestInterrupt extends LinearOpMode {
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
