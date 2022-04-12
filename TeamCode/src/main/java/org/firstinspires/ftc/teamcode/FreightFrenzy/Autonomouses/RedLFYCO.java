package org.firstinspires.ftc.teamcode.FreightFrenzy.Autonomouses;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.AllSystems;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.Routes;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;

@Autonomous(name = "RedLFYCO", group = "RedAuto")
public class RedLFYCO extends LinearOpMode {
    Routes routes;

    @Override
    public void runOpMode() {
        routes = new Routes(AllSystems.init(this, MathUtils.Side.RED));
        waitForStart();

        routes.LFYCO();
    }
}