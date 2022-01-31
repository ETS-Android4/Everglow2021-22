package org.firstinspires.ftc.teamcode.FreightFrenzy.GameAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Crater;

@Autonomous(name = "RedRFYCO", group = "Autonomous")
public class RedRFYCO extends LinearOpMode {
    Crater crater;

    @Override
    public void runOpMode() {
        crater = new Crater(this);

        waitForStart();

        while (opModeIsActive()) {
            crater.RFYCO(1);
            stop();
        }
    }
}


