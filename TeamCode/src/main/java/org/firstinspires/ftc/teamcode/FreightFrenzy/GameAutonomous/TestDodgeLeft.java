package org.firstinspires.ftc.teamcode.FreightFrenzy.GameAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Carousel;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Crater;

@Autonomous(name = "TestDodgeLeft", group = "Autonomous")
public class TestDodgeLeft extends LinearOpMode {
    Carousel car;

    @Override
    public void runOpMode() {
        car = new Carousel(this);

        waitForStart();

        while (opModeIsActive()) {
            car.placeFreight(1);
            car.dodgeToFront(-1,1);
            stop();
        }
    }
}


