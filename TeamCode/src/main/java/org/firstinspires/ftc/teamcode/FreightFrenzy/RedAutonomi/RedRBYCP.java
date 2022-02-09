package org.firstinspires.ftc.teamcode.FreightFrenzy.RedAutonomi;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Crater;

@Autonomous(name = "RedRBYCP", group = "RedAutonomousR")
public class RedRBYCP extends LinearOpMode {
    Crater crater;

    @Override
    public void runOpMode() {
        crater = new Crater(this);

        waitForStart();

        while (opModeIsActive()) {
            crater.RBYCP(1);
            stop();
        }
    }
}


