package org.firstinspires.ftc.teamcode.FreightFrenzy.BlueAutonomi;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Crater;

@Autonomous(name = "BlueLBYCP", group = "BlueAutonomousL")
@Disabled
public class BlueLBYCP extends LinearOpMode {
    Crater crater;

    @Override
    public void runOpMode() {
        crater = new Crater(this);

        waitForStart();

        while (opModeIsActive()) {
            crater.RBYCP(-1);
            stop();
        }
    }
}
