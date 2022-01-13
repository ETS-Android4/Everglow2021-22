package org.firstinspires.ftc.teamcode.FreightFrenzy.GameAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Carousel;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Crater;

@Autonomous(name = "BlueRFYCP", group = "Autonomous")
public class BlueRFYCP extends LinearOpMode {
    Carousel carousel;

    @Override
    public void runOpMode() {
        carousel = new Carousel(this);

        waitForStart();

        while (opModeIsActive()) {
            carousel.LFYCP(-1);
            stop();
        }
    }
}