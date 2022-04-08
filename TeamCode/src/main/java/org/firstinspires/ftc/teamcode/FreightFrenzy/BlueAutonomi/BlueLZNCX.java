package org.firstinspires.ftc.teamcode.FreightFrenzy.BlueAutonomi;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Crater;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Routes;

@Autonomous(name = "BlueLZNCX", group = "BlueAutonomousL")
public class BlueLZNCX extends LinearOpMode {
    Routes routes;

    @Override
    public void runOpMode() {
        routes = new Routes(this,-1);

        waitForStart();

        while (opModeIsActive()) {
            routes.RZNCX();
            stop();
        }
    }
}
