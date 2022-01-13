package org.firstinspires.ftc.teamcode.FreightFrenzy.GameAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Carousel;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Crater;

@Autonomous(name = "BlueRFYCO", group = "Autonomous")
public class BlueRFYCO extends LinearOpMode {
    Carousel carousel;

    @Override
    public void runOpMode() {
        carousel = new Carousel(this);

        waitForStart();

        while (opModeIsActive()) {
            carousel.LFYCO(-1);
            stop();
        }
    }
}