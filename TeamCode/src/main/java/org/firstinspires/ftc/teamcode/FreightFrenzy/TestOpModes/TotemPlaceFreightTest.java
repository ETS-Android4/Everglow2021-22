package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Carousel;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Crater;

@TeleOp(name = "TotemPlaceFreightTest", group = "LinearOpMode")
@Disabled
public class TotemPlaceFreightTest extends LinearOpMode {
    Carousel        car;
    Crater          crater;

    @Override
    public void runOpMode() {
        car    = new Carousel(this);
        crater = new Crater(this);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                car.placeFreightAndCollectTotem(1);
                stop();
            }
            if (gamepad1.b) {
                crater.placeFreightAndCollectTotem(1);
                stop();
            }
            if (gamepad1.x) {
                car.placeFreightAndCollectTotem(-1);
                stop();
            }
            if (gamepad1.y) {
                crater.placeFreightAndCollectTotem(-1);
                stop();
            }
        }
    }
}
