package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

@TeleOp(name = "FirstOpMode", group = "Linear Opmode")
public class OpMode1 extends LinearOpMode {
    DrivingSystem   drivingSystem;
    EverglowGamepad gamepad;
    ArmSystem armSystem;

    @Override
    public void runOpMode() {
        drivingSystem = new DrivingSystem(this);
        armSystem = new ArmSystem(this);
        gamepad       = new EverglowGamepad(gamepad1);


        waitForStart();

        while (opModeIsActive()) {
            drivingSystem.driveByJoystick(gamepad1.left_stick_x, gamepad1.left_stick_y,
                    gamepad1.right_stick_x);

            if(gamepad.buttonPress("a")){
                armSystem.moveArm(50);
            }
            if(gamepad.buttonPress("b")){
                armSystem.moveArm(250);
            }

            gamepad.update();
            telemetry.update();
        }
    }
}
