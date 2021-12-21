package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

@TeleOp(name = "FirstAutonomous", group = "Linear Opmode")
public class Autonomous extends LinearOpMode {
    DrivingSystem   drivingSystem;
    EverglowGamepad ourGamepad1;
    EverglowGamepad ourGamepad2;
    ArmSystem       armSystem;

    @Override
    public void runOpMode() {
        drivingSystem = new DrivingSystem(this);
        ourGamepad1       = new EverglowGamepad(gamepad1);
        ourGamepad2       = new EverglowGamepad(gamepad2);
        armSystem     = new ArmSystem(this);
        boolean toggle = false;
        waitForStart();

        while (opModeIsActive()) {
            if (ourGamepad1.buttonPress("a")) {
                drivingSystem.turn(45, 200);
            }
            if (ourGamepad1.buttonPress("x")) {
                drivingSystem.turn(45, 100);
            }

//            if (ourGamepad1.buttonPress("b")) {
//                drivingSystem.rotateInPlace(90, 0.2, 0, false);
//            }
            if (ourGamepad1.buttonPress("b")) {
                drivingSystem.turn(90, 200);
            }
            if (ourGamepad1.buttonPress("y")) {
                drivingSystem.turn(90, 100);
            }
            if (ourGamepad1.buttonPress("y")) {
//                if (!toggle) {
//                    armSystem.collect();
//                    toggle = true;
//                } else {
//                    armSystem.spit();
//                    toggle = false;
//                }
            }
            if (ourGamepad2.buttonPress("a")) {
//                armSystem.moveArm(ArmSystem.Floors.FIRST);
//                while (armSystem.arm.isBusy()) {
//                }
                if (!toggle) {
                    armSystem.arm.setPower(0.5);
                    toggle = true;
                } else {
                    armSystem.arm.setPower(0);
                    toggle = false;
                }
            }
            if (ourGamepad2.buttonPress("b")) {
                armSystem.moveArm(ArmSystem.Floors.SECOND);
//                while (armSystem.arm.isBusy()) {
//                }
            }
            if (ourGamepad2.buttonPress("x")) {
                armSystem.moveArm(ArmSystem.Floors.THIRD);
            }
            if (ourGamepad2.buttonPress("y")) {
                armSystem.moveArm(0);
//                while (armSystem.arm.isBusy()) {
//                }
            }

            ourGamepad1.update();
            ourGamepad2.update();
            telemetry.addData("Arm pos:", armSystem.arm.getCurrentPosition());
            telemetry.addData("Arm power:", armSystem.arm.getPower());
            telemetry.addData("Arm mode:", armSystem.arm.getMode());
            telemetry.update();
        }
    }
}
