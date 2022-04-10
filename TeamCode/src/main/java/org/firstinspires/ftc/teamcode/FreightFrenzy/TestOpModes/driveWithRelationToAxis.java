package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

@TeleOp(name = "driveWithRelationToAxis", group = "Test")
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

        waitForStart();

        while (opModeIsActive()) {
            ourGamepad1.update();
            drivingSystem.driveByJoystickWithRelationToAxis(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);

            if(ourGamepad1.cross()){
                RZNCX(-1);
            }
            if(ourGamepad1.circle()){
                RZNCX(1);
            }
            if(ourGamepad1.square()){
                drivingSystem.driveToPoint(0,50,0,0.3,1);

            }
            if(ourGamepad1.triangle()){
                drivingSystem.driveToPoint(50,0,180,0.5,1);
            }
        }
    }

    void RZNCX(int mirror){
        drivingSystem.driveToPoint(-55*mirror,-25,-135 * mirror,0.7,1);
        TimeUtils.sleep(1000);
        drivingSystem.driveToPoint(55*mirror,25,0*mirror,0.7,1);
    }
}