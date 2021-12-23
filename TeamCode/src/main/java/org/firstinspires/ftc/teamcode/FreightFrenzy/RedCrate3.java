package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Crate;

import java.nio.file.Paths;

@Autonomous(name = "RedCrate3", group = "Linear Opmode")
public class RedCrate3 extends LinearOpMode {
    DrivingSystem   drivingSystem;
    EverglowGamepad ourGamepad1;
    EverglowGamepad ourGamepad2;
    ArmSystem       armSystem;
    Crate           crate;
    @Override
    public void runOpMode() {
        drivingSystem = new DrivingSystem(this);
        ourGamepad1   = new EverglowGamepad(gamepad1);
        ourGamepad2   = new EverglowGamepad(gamepad2);
        armSystem     = new ArmSystem(this);
        crate         = new Crate(this);
        boolean toggle = false;
        waitForStart();

        while (opModeIsActive()) {
            if (ourGamepad1.buttonPress("a")) {
                crate.R3();
            }
        }
    }
}

