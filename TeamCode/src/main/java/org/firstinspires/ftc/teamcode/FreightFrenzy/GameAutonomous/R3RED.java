package org.firstinspires.ftc.teamcode.FreightFrenzy.GameAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Crater;

@Autonomous(name = "RedRBYW", group = "Linear Opmode")
public class R3RED extends LinearOpMode {
    Crater crater;

    @Override
    public void runOpMode() {
        crater = new Crater(this);

        waitForStart();

        while (opModeIsActive()) {
            crater.R3(1);
            stop();
        }
    }
}

