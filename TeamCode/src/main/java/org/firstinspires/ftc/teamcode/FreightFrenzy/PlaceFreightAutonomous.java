package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Carousel;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Crater;

@TeleOp(name = "Place Freight Left", group = "LinearOpMode")
public class PlaceFreightAutonomous extends LinearOpMode {
    Carousel car;
    Crater crater;
    EverglowGamepad g;
    DrivingSystem d;

    @Override
    public void runOpMode() {
        car = new Carousel(this);
        crater = new Crater(this);
        g = new EverglowGamepad(gamepad1);
        d = new DrivingSystem(this);

        waitForStart();

        while(opModeIsActive()){
            if (g.buttonPress("a")) {
                car.placeFreight();
                stop();
            }
            if (g.buttonPress("b")) {
                crater.placeFreight();
                stop();
            }
            if (g.buttonPress("y")) {
                d.driveStraight(100, 0.4);
                stop();
            }
        }
    }
}
