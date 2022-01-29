package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

@TeleOp(name = "GameTeleOp", group = "Linear Opmode")
public class GameTeleOp extends LinearOpMode {

    // when the right stick is pressed on the controller, make the rotation slower by this factor.
    private static final double RIGHT_STICK_DOWN_MOVE_REDUCTION = 10;
    private static final double LEFT_STICK_DOWN_MOVE_REDUCTION  = 5;

    DrivingSystem   drivingSystem;
    ArmSystem       armSystem;
    DuckSystem      duckSystem;
    EverglowGamepad ourGamepad1;
    EverglowGamepad ourGamepad2;
    TouchSensor     touch;

    boolean collectingTotem = false;

    @Override
    public void runOpMode() {
        drivingSystem = new DrivingSystem(this);
        armSystem     = new ArmSystem(this);
        duckSystem    = new DuckSystem(this);
        ourGamepad1   = new EverglowGamepad(gamepad1);
        ourGamepad2   = new EverglowGamepad(gamepad2);
        touch         = hardwareMap.get(TouchSensor.class, "touch");

        boolean prevTouchSensorPressed = false;


        waitForStart();

        while (opModeIsActive()) {
            ourGamepad1.update();
            ourGamepad2.update();
            {
                double left_stick_x = gamepad2.left_stick_x;
                double left_stick_y = gamepad2.left_stick_y;
                double right_stick_x = gamepad2.right_stick_x;
                if (gamepad2.right_stick_button){
                    right_stick_x/=RIGHT_STICK_DOWN_MOVE_REDUCTION;
                }
                if (gamepad2.left_stick_button){
                    left_stick_x/= LEFT_STICK_DOWN_MOVE_REDUCTION;
                    left_stick_y/= LEFT_STICK_DOWN_MOVE_REDUCTION;
                }

                telemetry.addData("left_x", left_stick_x);
                telemetry.addData("left_y", left_stick_y);
                telemetry.addData("right_x", right_stick_x);

                drivingSystem.driveByJoystick(left_stick_x, left_stick_y, right_stick_x);
            }

            if (ourGamepad2.x()) {
                armSystem.reload();
            }
            if (ourGamepad2.a()) {
                armSystem.moveArm(ArmSystem.Floors.FIRST);
                collectingTotem = false;
            }
            if (ourGamepad2.b()) {
                armSystem.moveArm(ArmSystem.Floors.SECOND);
                collectingTotem = false;
            }
            if (ourGamepad2.y()) {
                armSystem.moveArm(ArmSystem.Floors.THIRD);
                collectingTotem = false;
            }
            if (ourGamepad2.dpad_right()){
                armSystem.moveArm(ArmSystem.Floors.TOTEM);
            }

            if (ourGamepad2.rt()) {
                armSystem.toggleCollecting();
            }

            if (ourGamepad2.lt()) {
                armSystem.toggleSpitting();
            }

            if (ourGamepad2.rb() || ourGamepad2.lb()){
                armSystem.stop();
            }

            if (armSystem.getCollectState() == ArmSystem.CollectState.COLLECTING && touch.isPressed()) {
                armSystem.stop();
            }

            if (ourGamepad2.dpad_left()) {
                duckSystem.toggle();
            }

            if (ourGamepad2.dpad_up()) {
                armSystem.moveArm(armSystem.arm.getCurrentPosition() + 50);
            }

            if (ourGamepad2.dpad_down()) {
                armSystem.moveArm(armSystem.arm.getCurrentPosition() - 50);
            }
            // rumble controller if touchSensor was just pressed
            if (touch.isPressed()) {
                if (!prevTouchSensorPressed) {
                    gamepad2.rumble(1000);
                }
                prevTouchSensorPressed = true;
            } else {
                prevTouchSensorPressed = false;
            }
            if (!collectingTotem) {
                armSystem.restOnLoad();
            }
            if (gamepad2.share) {
                armSystem.moveArm(-100);
                collectingTotem = true;
            }

            if (gamepad2.options){
                drivingSystem.placeTotem(armSystem);
            }


            armSystem.restOnFirstFloor();

//            telemetry.addData("arm position: ", armSystem.arm.getCurrentPosition());
            telemetry.update();
        }
    }
}
