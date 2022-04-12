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

@TeleOp(name = "GameTeleOp", group = "Linear Opmode")
public class GameTeleOp extends LinearOpMode {

    // when the right stick is pressed on the controller, make the rotation slower by this factor.
    private static final double RIGHT_STICK_DOWN_MOVE_REDUCTION = 10;
    private static final double LEFT_STICK_DOWN_MOVE_REDUCTION  = 5;

    DrivingSystem   drivingSystem;
    ArmSystem       armSystem;
    DuckSystem      duckSystem;
    ColorSystem colorSystem;
    EverglowGamepad ourGamepad1;
    TotemSystem totemSystem;

    boolean passingObstacle = false;

    @Override
    public void runOpMode() {
        drivingSystem = new DrivingSystem(this);
        armSystem     = new ArmSystem(this);
        duckSystem    = new DuckSystem(this);
        colorSystem = new ColorSystem(this);
        ourGamepad1   = new EverglowGamepad(gamepad1);
        totemSystem =  new TotemSystem(this);

        boolean prevTouchPressed = false;
        boolean toggleReload = true;

        waitForStart();

        while (opModeIsActive()) {
            ourGamepad1.update();
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

                drivingSystem.driveByJoystick(left_stick_x, left_stick_y, right_stick_x);
            }

            if (ourGamepad1.x()) {
                if (!toggleReload) {
                    armSystem.reload();
                    toggleReload = true;
                } else {
                    armSystem.moveArm(ArmSystem.Floors.OBSTACLE);
                    toggleReload = false;
                }
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
                armSystem.moveArm(armSystem.arm.getTargetPosition() + 50);
            }

            if (ourGamepad1.lb()) {
                armSystem.moveArm(armSystem.arm.getTargetPosition() - 50);
            }

            if (armSystem.getCollectState() == ArmSystem.CollectState.COLLECTING && armSystem.touch.isPressed()) {
                armSystem.stop();
            }

            if (ourGamepad1.dpad_left() || ourGamepad1.dpad_right()) {
                duckSystem.toggle();
            }

            if (ourGamepad1.dpad_up()) {
                armSystem.moveArm(ArmSystem.Floors.CAROUSEL);
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
            boolean touchPressed = armSystem.touch.isPressed();

            if (touchPressed && armSystem.getCollectState() == ArmSystem.CollectState.COLLECTING) {
                armSystem.stop();
            }

            if (touchPressed && !prevTouchPressed) {
                gamepad1.rumble(1000);
            }

            prevTouchPressed = touchPressed;

            if (!passingObstacle) {
                armSystem.restOnLoad();
            }

            if(ourGamepad1.crossHold()){
                armSystem.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }

            if(armSystem.collectState != ArmSystem.CollectState.COLLECTING) {
                armSystem.restOnLoad();
            }
            else{
                armSystem.stayDownOnLoad();
            }

            telemetry.addData("arm pos: ", armSystem.arm.getTargetPosition());
            telemetry.addData("is cargo sensor: ", colorSystem.isCargo());
            telemetry.update();
        }
    }
}
