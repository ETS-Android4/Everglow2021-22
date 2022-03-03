package org.firstinspires.ftc.teamcode.FreightFrenzy.new_autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Carousel;

@Autonomous(name = "RedCarousel", group = "RedAutonomousL")
@Disabled
public class RedCarousel extends LinearOpMode {
    Carousel carousel;

    @Override
    public void runOpMode() {
        carousel = new Carousel(this);

        waitForStart();

        while (opModeIsActive()) {
            carousel.newPlaceFreightAndCaursel(1);
            stop();
        }
    }
}


