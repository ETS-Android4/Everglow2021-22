package org.firstinspires.ftc.teamcode.FreightFrenzy.RedAutonomi;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Carousel;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Crater;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Routes;

@Autonomous(name = "RedRZNCX", group = "RedAutonomousR")
public class RedRZNCX extends LinearOpMode {
    Routes routes;

    @Override
    public void runOpMode() {
        routes = new Routes(this, 1);

        waitForStart();

        while (opModeIsActive()) {
            routes.RZNCX();
            stop();
        }
    }
}


