package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class ArmSystem {
    DcMotor flyWheels;
    DcMotor arm;

    public enum Floors{
        FIRST, SECOND, THIRD
    }
    public ArmSystem(LinearOpMode opMode) {
        this.flyWheels = opMode.hardwareMap.get(DcMotor.class, "front_right");//flyWheels
        this.arm = opMode.hardwareMap.get(DcMotor.class, "front_left");//arm
    }

    public void pull() {
        double position = flyWheels.getCurrentPosition();
        flyWheels.setPower(0.5);
        while (position < 268) {
            position = flyWheels.getCurrentPosition();
        }
        flyWheels.setPower(0);
    }

    public void push() {
        double position = flyWheels.getCurrentPosition();
        flyWheels.setPower(-0.5);
        while (position < 268) {
            position = flyWheels.getCurrentPosition();
        }
        flyWheels.setPower(0);
    }



    public void MoveArm(Floors level){
        double ArmPosition = arm.getCurrentPosition();
        switch (level){
            case FIRST:
                arm.setPower(0.5);
                while(ArmPosition < 342.5){
                    ArmPosition = arm.getCurrentPosition();
                }
                arm.setPower(0);
                break;
            case SECOND:
                arm.setPower(0.5);
                while(ArmPosition < 407.7){
                    ArmPosition = arm.getCurrentPosition();
                }
                arm.setPower(0);
                break;
            case THIRD:
                arm.setPower(0.5);
                while (ArmPosition < 430.1){
                    ArmPosition = arm.getCurrentPosition();
                }
                arm.setPower(0);
        }
    }
}
