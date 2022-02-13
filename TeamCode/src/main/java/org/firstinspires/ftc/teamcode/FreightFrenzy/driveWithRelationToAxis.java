package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

@TeleOp(name = "drive", group = "Linear Opmode")
public class driveWithRelationToAxis extends LinearOpMode {
    DrivingSystem drivingSystem;
    ArmSystem armSystem;
    DuckSystem duckSystem;
    EverglowGamepad ourGamepad1;
    TouchSensor touch;

    boolean passingObstacle = false;

    @Override
    public void runOpMode() {
        drivingSystem = new DrivingSystem(this);
        armSystem = new ArmSystem(this);
        duckSystem = new DuckSystem(this);
        ourGamepad1 = new EverglowGamepad(gamepad1);
        touch = hardwareMap.get(TouchSensor.class, "touch");

        boolean prevTouchSensorPressed = false;


        waitForStart();

        while (opModeIsActive()) {
            ourGamepad1.update();
            {
                drivingSystem.driveByJoystickWithRelationToAxis(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            }

            if (ourGamepad1.x()) {
                armSystem.reload();
                passingObstacle = false;
            }
            if (ourGamepad1.a()) {
                armSystem.moveArm(ArmSystem.Floors.FIRST);
                passingObstacle = false;
            }
            if (ourGamepad1.b()) {
                armSystem.moveArm(ArmSystem.Floors.SECOND);
                passingObstacle = false;
            }
            if (ourGamepad1.y()) {
                armSystem.moveArm(ArmSystem.Floors.THIRD);
                passingObstacle = false;
            }

            if (ourGamepad1.rt()) {
                armSystem.toggleCollecting();
            }

            if (ourGamepad1.lt()) {
                armSystem.toggleSpitting();
            }

            if (ourGamepad1.rb()) {
                armSystem.moveArmWithoutWobble(armSystem.arm.getTargetPosition() + 50);
            }
            if (ourGamepad1.lb()) {
                armSystem.moveArmWithoutWobble(armSystem.arm.getTargetPosition() - 50);
            }

            if (armSystem.getCollectState() == ArmSystem.CollectState.COLLECTING && touch.isPressed()) {
                armSystem.stop();
            }

            if (ourGamepad1.dpad_left() || ourGamepad1.dpad_right()) {
                duckSystem.toggle();
            }

            if (ourGamepad1.dpad_up()) {
                armSystem.moveArm(ArmSystem.Floors.TOTEM);
            }

            if (ourGamepad1.dpad_down() && !passingObstacle) {
                armSystem.moveArmWithoutWobble(-300);
                passingObstacle = true;
            }
            if (ourGamepad1.dpad_down() && passingObstacle) {
                armSystem.reload();
                passingObstacle = false;
            }

            // rumble controller if touchSensor was just pressed
            if (touch.isPressed()) {
                if (!prevTouchSensorPressed) {
                    gamepad1.rumble(1000);
                }
                prevTouchSensorPressed = true;
            } else {
                prevTouchSensorPressed = false;
            }
            if (!passingObstacle) {
                armSystem.restOnLoad();
            }

            if (gamepad1.options) {
                drivingSystem.CS(armSystem);
            }

            armSystem.restOnFirstFloor();
            armSystem.slowArm();
        }
    }
}