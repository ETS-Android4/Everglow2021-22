package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Routes;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.AllSystems;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;

@TeleOp(name = "Test craterPlaceFreight", group = "Test")
public class TestCraterPlaceFreight extends LinearOpMode {
    Routes routes;
    AllSystems allSystems;

    @Override
    public void runOpMode() {
        allSystems = new AllSystems(this, MathUtils.Side.RED);
        routes = new Routes(allSystems);

        waitForStart();

        while (opModeIsActive()) {
            routes.craterPlaceFreight(true);
            stop();
        }
    }
}
