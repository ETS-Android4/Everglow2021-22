package org.firstinspires.ftc.teamcode.FreightFrenzy.GameAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Carousel;

@Autonomous(name = "RedLBYCO", group = "Linear Opmode")
public class RedLBYCO extends LinearOpMode {
    Carousel carousel;

    @Override
    public void runOpMode() {
        carousel = new Carousel(this);

        waitForStart();

        while (opModeIsActive()) {
            carousel.LBYCO(1);
            stop();
        }
    }
}
