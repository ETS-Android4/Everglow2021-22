package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ColorSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem2;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

@TeleOp(name = "EndGameCapping", group = "Linear Opmode")
public class EndGameCapping extends LinearOpMode {

    // when the right stick is pressed on the controller, make the rotation slower by this factor.
    private static final double RIGHT_STICK_DOWN_MOVE_REDUCTION = 10;
    private static final double LEFT_STICK_DOWN_MOVE_REDUCTION = 2;

    DrivingSystem drivingSystem;
    ArmSystem armSystem;
    DuckSystem duckSystem;
    TotemSystem2 totemSystem;
    ColorSystem colorSystem;
    EverglowGamepad ourGamepad1;
    EverglowGamepad ourGamepad2;

    boolean passingObstacle = false;
    boolean duckSpin = false;

    double aziPowerRight;
    double altPowerRight;
    double aziPowerLeft;
    double altPowerLeft;

    @Override
    public void runOpMode() {
        TimeUtils.opMode = this;
        drivingSystem = new DrivingSystem(this);
        armSystem = new ArmSystem(this);
        duckSystem = new DuckSystem(this);
        totemSystem = new TotemSystem2(this);
        colorSystem = new ColorSystem(this);
        ourGamepad1 = new EverglowGamepad(gamepad1);
        ourGamepad2 = new EverglowGamepad(gamepad2);

        boolean prevTouchPressed = false;
        boolean toggleReload = true;

        waitForStart();

        while (opModeIsActive()) {
            ourGamepad1.update();
            ourGamepad2.update();
            {
                double left_stick_x = gamepad1.left_stick_x;
                double left_stick_y = gamepad1.left_stick_y;
                double right_stick_x = gamepad1.right_stick_x;

                if (gamepad1.right_trigger > 0.1) {
                    left_stick_x /= LEFT_STICK_DOWN_MOVE_REDUCTION;
                    left_stick_y /= LEFT_STICK_DOWN_MOVE_REDUCTION;
                    right_stick_x /= RIGHT_STICK_DOWN_MOVE_REDUCTION;
                }

                telemetry.addData("left_x", left_stick_x);
                telemetry.addData("left_y", left_stick_y);
                telemetry.addData("right_x", right_stick_x);

                drivingSystem.driveByJoystick(left_stick_x, left_stick_y, right_stick_x);
            }

            if (ourGamepad2.x()) {
                if (!toggleReload) {
                    armSystem.reload();
                    toggleReload = true;
                } else {
                    armSystem.moveArm(ArmSystem.Floors.OBSTACLE);
                    toggleReload = false;
                }
                passingObstacle = false;
            }

            if (ourGamepad2.a()) {
                armSystem.autonomousMoveArm(ArmSystem.Floors.FIRST);
                passingObstacle = false;
            }

            if (ourGamepad2.b()) {
                armSystem.autonomousMoveArm(ArmSystem.Floors.THIRD);
                passingObstacle = false;
            }

            if (ourGamepad2.y()) {
                armSystem.moveArm(ArmSystem.Floors.THIRD);
                passingObstacle = false;
            }

            if (ourGamepad2.rt()) {
                armSystem.toggleCollecting();
            }

            if (ourGamepad2.lt()) {
                armSystem.toggleSpitting(colorSystem.isCargo());
            }

            if (ourGamepad2.rb()) {
                armSystem.moveArm(armSystem.arm.getTargetPosition() + 50);
            }

            if (ourGamepad2.lb()) {
                armSystem.moveArm(armSystem.arm.getTargetPosition() - 50);
            }

            if (armSystem.getCollectState() == ArmSystem.CollectState.COLLECTING && armSystem.touch.isPressed()) {
                armSystem.stop();
            }

            if (ourGamepad1.dpad_left()) {
                if (!duckSpin) {
                    duckSystem.speed = 0.7;
                    duckSystem.runRev();
                    duckSpin = true;
                } else {
                    duckSystem.speed = 1;
                    duckSystem.runRev();
                    duckSpin = false;
                }
            }

            if (ourGamepad1.dpad_right()) {
                duckSystem.stop();
                duckSpin = false;
            }

            aziPowerRight = gamepad2.right_stick_x;
            altPowerRight = -gamepad2.right_stick_y;
            aziPowerLeft = gamepad2.left_stick_x;
            altPowerLeft = -gamepad2.left_stick_y;

            if (gamepad2.right_stick_button) {
                aziPowerRight /= 2;
                altPowerRight /= 2;
            }
            if (gamepad2.left_stick_button) {
                aziPowerLeft /= 2;
                altPowerLeft /= 2;
            }

            if (Math.abs(aziPowerRight) > Math.abs(altPowerRight)) {
                drivingSystem.rotateAroundDucks(aziPowerRight, true);
            } else {
                totemSystem.moveAltitude(altPowerRight);
            }

            if (Math.abs(aziPowerLeft) > Math.abs(altPowerLeft)) {
                drivingSystem.rotateAroundDucks(aziPowerLeft, false);
            } else {
                totemSystem.moveAltitude(altPowerLeft);
            }

            // rumble controller if touchSensor was just pressed
            boolean touchPressed = armSystem.touch.isPressed();

            if (touchPressed && armSystem.getCollectState() == ArmSystem.CollectState.COLLECTING) {
                armSystem.stop();
            }

            if (touchPressed && !prevTouchPressed) {
                gamepad1.rumble(1000);
                gamepad2.rumble(1000);
            }

            prevTouchPressed = touchPressed;

            if (!passingObstacle) {
                armSystem.restOnLoad();
            }

            if (ourGamepad1.squareHold()) {
                armSystem.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }

            armSystem.restOnFirstFloor();
            telemetry.addData("is cargo sensor: ", colorSystem.isCargo());
            telemetry.update();
        }
    }
}
