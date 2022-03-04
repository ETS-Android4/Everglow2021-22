package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

@TeleOp(name = "TotemTwoDrivers", group = "Linear Opmode")
public class TotemTwoDrivers extends LinearOpMode {

    // when the right stick is pressed on the controller, make the rotation slower by this factor.
    private static final double RIGHT_STICK_DOWN_MOVE_REDUCTION = 10;
    private static final double LEFT_STICK_DOWN_MOVE_REDUCTION = 5;

    DrivingSystem drivingSystem;
    ArmSystem armSystem;
    DuckSystem duckSystem;
    TotemSystem totemSystem;
    EverglowGamepad ourGamepad1;
    EverglowGamepad ourGamepad2;
    TouchSensor touch;

    boolean passingObstacle = false;

    @Override
    public void runOpMode() {
        drivingSystem = new DrivingSystem(this);
        armSystem = new ArmSystem(this);
        duckSystem = new DuckSystem(this);
        ourGamepad1 = new EverglowGamepad(gamepad1);
        ourGamepad2 = new EverglowGamepad(gamepad2);
        totemSystem = new TotemSystem(this,true);
        touch = hardwareMap.get(TouchSensor.class, "touch");

        boolean prevTouchSensorPressed = false;


        waitForStart();

        while (opModeIsActive()) {
            ourGamepad1.update();
            ourGamepad2.update();
            {
                double left_stick_x = gamepad1.left_stick_x;
                double left_stick_y = gamepad1.left_stick_y;
                double right_stick_x = gamepad1.right_stick_x;
                if (gamepad1.right_stick_button) {
                    right_stick_x /= RIGHT_STICK_DOWN_MOVE_REDUCTION;
                }
                if (gamepad1.left_stick_button) {
                    left_stick_x /= LEFT_STICK_DOWN_MOVE_REDUCTION;
                    left_stick_y /= LEFT_STICK_DOWN_MOVE_REDUCTION;
                }

                telemetry.addData("left_x", left_stick_x);
                telemetry.addData("left_y", left_stick_y);
                telemetry.addData("right_x", right_stick_x);

                drivingSystem.driveByJoystick(left_stick_x, left_stick_y, right_stick_x);
            }
            totemSystem.moveAzimuth(totemSystem.azimuth.getPosition() + gamepad2.right_stick_x / 10);
            totemSystem.moveAltitude(totemSystem.altitude.getPosition() - gamepad2.left_stick_y / 10);

            if (ourGamepad2.x()) {
                armSystem.reload();
                passingObstacle = false;
            }
            if (ourGamepad2.a()) {
                armSystem.moveArm(ArmSystem.Floors.FIRST);
                passingObstacle = false;
            }
            if (ourGamepad2.b()) {
                armSystem.moveArm(ArmSystem.Floors.SECOND);
                passingObstacle = false;
            }
            if (ourGamepad2.y()) {
                armSystem.moveArm(ArmSystem.Floors.THIRD);
                passingObstacle = false;
            }
            if (ourGamepad2.dpad_right()) {
                armSystem.moveArm(ArmSystem.Floors.TOTEM);
            }

            if (ourGamepad2.rt()) {
                armSystem.toggleCollecting();
            }

            if (ourGamepad2.lt()) {
                armSystem.toggleSpitting();
            }

            if (ourGamepad2.rb()) {
                armSystem.moveArm(armSystem.arm.getTargetPosition() + 50);
            }
            if (ourGamepad2.lb()) {
                armSystem.moveArm(armSystem.arm.getTargetPosition() - 50);
            }

            if (armSystem.getCollectState() == ArmSystem.CollectState.COLLECTING && touch.isPressed()) {
                armSystem.stop();
            }

            if (ourGamepad1.dpad_left()) {
                duckSystem.toggle();
            }

            if(ourGamepad1.dpad_right()) {
                totemSystem.extend(0);
            }

            if (ourGamepad2.dpad_right()) {
                totemSystem.extend(0.1);
            }
            if (ourGamepad2.dpad_left()) {
                totemSystem.extend(-0.1);
            }

            if (ourGamepad2.dpad_up()) {
                armSystem.moveArm(ArmSystem.Floors.TOTEM);
            }

            if (ourGamepad2.dpad_down() && !passingObstacle) {
                armSystem.moveArm(-300);
                passingObstacle = true;
            }
            if (ourGamepad2.dpad_down() && passingObstacle) {
                armSystem.reload();
                passingObstacle = false;
            }

            // rumble controller if touchSensor was just pressed
            if (touch.isPressed()) {
                if (!prevTouchSensorPressed) {
                    gamepad1.rumble(1000);
                    gamepad2.rumble(1000);
                }
                prevTouchSensorPressed = true;
            } else {
                prevTouchSensorPressed = false;
            }

            if (!passingObstacle) {
                armSystem.restOnLoad();
            }
            armSystem.restOnFirstFloor();
        }
    }
}
