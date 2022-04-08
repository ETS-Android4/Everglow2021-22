package org.firstinspires.ftc.teamcode.FreightFrenzy.RedAutonomi;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Carousel;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Crater;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Routes;
import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AllSystems;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

@Autonomous(name = "RedRZNCX", group = "RedAutonomousR")
public class RedRZNCX extends LinearOpMode {
    Routes routes;

    @Override
    public void runOpMode() {
        TimeUtils.opMode = this;
        routes = new Routes(AllSystems.init(this, MathUtils.Side.RED));

        waitForStart();

        while (opModeIsActive()) {
            routes.RZNCX();
            stop();
        }
    }
}


