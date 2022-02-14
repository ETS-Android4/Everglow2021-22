package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

@TeleOp(name = "driveWithRelationToAxis", group = "Linear Opmode")
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
            drivingSystem.driveByJoystickWithRelationToAxis(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);

            if(ourGamepad1.rt()){
                drivingSystem.driveToPoint(50,50,0);
            }
            if(ourGamepad1.rb()){
                drivingSystem.driveToPoint(50,50,180);
            }
            if(ourGamepad1.lt()){
                drivingSystem.driveToPoint(60,30,0);
            }
            if(ourGamepad1.lb()){
                drivingSystem.driveToPoint(60,30,90);
            }
        }
    }
}