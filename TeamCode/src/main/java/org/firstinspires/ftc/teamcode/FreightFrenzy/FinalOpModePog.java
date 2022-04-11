package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ColorSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

@TeleOp(name = "teleop shlayismhor", group = "Linear Opmode")
public class FinalOpModePog extends LinearOpMode {

    private static final double RIGHT_STICK_DOWN_MOVE_REDUCTION = 10;
    private static final double LEFT_STICK_DOWN_MOVE_REDUCTION = 2;

    DrivingSystem drivingSystem;
    ArmSystem armSystem;
    DuckSystem duckSystem;
    TotemSystem totemSystem;
    ColorSystem colorSystem;
    EverglowGamepad ourGamepad1;
    EverglowGamepad ourGamepad2;

    boolean passingObstacle = false;
    boolean duckSpin = false;

    @Override
    public void runOpMode() throws InterruptedException {
        drivingSystem = new DrivingSystem(this);
        armSystem = new ArmSystem(this);
        duckSystem = new DuckSystem(this);
        totemSystem = new TotemSystem(this);
        colorSystem = new ColorSystem(this);
        ourGamepad1   = new EverglowGamepad(gamepad1);
        ourGamepad2   = new EverglowGamepad(gamepad2);

        boolean prevTouchPressed = false;
        boolean toggleReload = true;

        boolean isEndgame = false;
        int dirR = 1;
        int dirL = -1;

        waitForStart();

        while (opModeIsActive()) {
            ourGamepad1.update();
            ourGamepad2.update();

            // nati
            if (gamepad2.right_stick_x == 0 && gamepad2.left_stick_x == 0){
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

            if (ourGamepad2.share()) {
                isEndgame = !isEndgame;
            }

            if (isEndgame) {
                // activate ducks
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
                // stop ducks
                if (ourGamepad1.dpad_right()) {
                    duckSystem.stop();
                    duckSpin = false;
                }

                // totem system azimuth
                double aziPowerRed = gamepad2.right_stick_x / 1000;
                double aziPowerBlue = gamepad2.left_stick_x / 1000;
                if (gamepad2.right_stick_button) {
                    aziPowerRed /= 2;
                }
                if (gamepad2.left_stick_button) {
                    aziPowerBlue /= 2;
                }
                if (Math.abs(aziPowerRed) > 0) {
                    drivingSystem.rotateAroundDucks(aziPowerRed, true);
                } else if (Math.abs(aziPowerBlue) > 0) {
                    drivingSystem.rotateAroundDucks(aziPowerBlue, false);
                }

                // totem system altitude
                totemSystem.moveAltitude(-gamepad2.right_stick_y / 500);
                totemSystem.moveAltitude(-gamepad2.left_stick_y / 500);

                // extend meter
                if (gamepad2.dpad_up) {
                    totemSystem.extendLeft(1);
                    totemSystem.extendRight(-1);
                }
                if (gamepad2.dpad_down) {
                    totemSystem.extendLeft(-1);
                    totemSystem.extendRight(1);
                }

                if (ourGamepad2.rb()) {
                    dirR = -dirR;
                }
                if (ourGamepad2.lb()) {
                    dirL = -dirL;
                }

                if (gamepad2.left_trigger > 0) {
                    totemSystem.extendLeft(dirL * Math.sqrt(
                            1 - (gamepad1.left_trigger - 1) * (gamepad1.left_trigger - 1)
                    ));
                }

                if (gamepad2.right_trigger > 0) {
                    totemSystem.extendRight(dirR * Math.sqrt(
                            1 - (gamepad1.right_trigger - 1) * (gamepad1.right_trigger - 1)
                    ));
                }

                if (gamepad2.right_trigger == 0 && !gamepad2.dpad_down && !gamepad2.dpad_up) {
                    totemSystem.stopRight();
                }
                if (gamepad2.left_trigger == 0 && !gamepad2.dpad_down && !gamepad2.dpad_up) {
                    totemSystem.stopLeft();
                }
            } else {
                // square
                if (ourGamepad2.x()) {
                    passingObstacle = false;
                    if (!toggleReload) {
                        armSystem.reload();
                        toggleReload = true;
                    } else {
                        armSystem.moveArm(ArmSystem.Floors.OBSTACLE);
                        toggleReload = false;
                    }
                }

                // floors
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

                // collect / spit
                if (ourGamepad2.rt()) {
                    armSystem.toggleCollecting();
                }
                if (ourGamepad2.lt()) {
                    armSystem.toggleSpitting();
                }

                // arm +- 50
                if (ourGamepad2.rb()) {
                    armSystem.moveArm(armSystem.arm.getTargetPosition() + 50);
                }
                if (ourGamepad2.lb()) {
                    armSystem.moveArm(armSystem.arm.getTargetPosition() - 50);
                }

                // rumble controller if touchSensor was just pressed
                boolean touchPressed = armSystem.touch.isPressed();

                // stop arm
                if (touchPressed && armSystem.getCollectState() == ArmSystem.CollectState.COLLECTING) {
                    armSystem.stop();
                }

                // rumble
                if (touchPressed && !prevTouchPressed) {
                    gamepad1.rumble(1000);
                    gamepad2.rumble(1000);
                }
                prevTouchPressed = touchPressed;

                if (ourGamepad1.squareHold()) {
                    armSystem.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                }

                if (!passingObstacle) {
                    armSystem.restOnLoad();
                }
                if (armSystem.collectState == ArmSystem.CollectState.COLLECTING) {
                    armSystem.stayDownOnLoad();
                }

                telemetry.addData("is cargo sensor: ", colorSystem.isCargo());
                telemetry.update();
            }
        }
    }
}
