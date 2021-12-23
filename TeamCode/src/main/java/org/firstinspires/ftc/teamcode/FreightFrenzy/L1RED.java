package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Crate;

@Autonomous(name = "L1RED", group = "Linear Opmode")
public class L1RED  extends LinearOpMode {
    DrivingSystem drivingSystem;
    ArmSystem armSystem;
    EverglowGamepad ourGamepad1;
    Crate crate;

    @Override
    public void runOpMode() throws InterruptedException {
        drivingSystem = new DrivingSystem(this);
        armSystem     = new ArmSystem(this);
        ourGamepad1   = new EverglowGamepad(gamepad1);
        crate         = new Crate(this);
        waitForStart();

        while (opModeIsActive()) {
            if(ourGamepad1.buttonPress("a")) {
                crate.L1();
            }
        }

    }
}
