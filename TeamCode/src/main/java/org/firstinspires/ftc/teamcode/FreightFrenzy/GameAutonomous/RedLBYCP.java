package org.firstinspires.ftc.teamcode.FreightFrenzy.GameAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Carousel;

@Autonomous(name = "RedLBYCP", group = "Linear Opmode")
public class RedLBYCP extends LinearOpMode {
    Carousel carousel;

    @Override
    public void runOpMode() {
        carousel = new Carousel(this);

        waitForStart();

        while (opModeIsActive()) {
            carousel.LBYCP(1);
            stop();
        }
    }
}


