package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Carousel;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Crater;

@TeleOp(name = "Place Freight Left", group = "LinearOpMode")
@Disabled
public class PlaceFreightAutonomous extends LinearOpMode {
    Carousel        car;
    Crater          crater;

    @Override
    public void runOpMode() {
        car    = new Carousel(this);
        crater = new Crater(this);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                car.placeFreight(1);
                stop();
            }
            if (gamepad1.b) {
                crater.placeFreight(1);
                stop();
            }
            if (gamepad1.x) {
                car.placeFreight(-1);
                stop();
            }
            if (gamepad1.y) {
                crater.placeFreight(-1);
                stop();
            }
        }
    }
}
