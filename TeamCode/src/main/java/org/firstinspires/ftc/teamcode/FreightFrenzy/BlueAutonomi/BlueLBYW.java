package org.firstinspires.ftc.teamcode.FreightFrenzy.BlueAutonomi;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Crater;

@Autonomous(name = "BlueLBYW", group = "BlueAutonomousL")
public class BlueLBYW extends LinearOpMode {
    Crater crater;

    @Override
    public void runOpMode() {
        crater = new Crater(this);

        waitForStart();

        while (opModeIsActive()) {
            crater.RBYW(-1);
            stop();
        }
    }
}
