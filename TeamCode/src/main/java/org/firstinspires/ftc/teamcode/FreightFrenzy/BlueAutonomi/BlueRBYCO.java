package org.firstinspires.ftc.teamcode.FreightFrenzy.BlueAutonomi;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Carousel;

@Autonomous(name = "BlueRBYCO", group = "BlueAutonomousR")
public class BlueRBYCO extends LinearOpMode {
    Carousel carousel;

    @Override
    public void runOpMode() {
        carousel = new Carousel(this);

        waitForStart();

        while (opModeIsActive()) {
            carousel.LBYCO(-1);
            stop();
        }
    }
}
