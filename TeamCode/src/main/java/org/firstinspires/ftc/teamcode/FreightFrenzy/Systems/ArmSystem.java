package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class ArmSystem {
    DcMotor flyWheels;
    public DcMotor arm;
    double ArmPosition;

    public enum Floors {
        FIRST, SECOND, THIRD
    }

    public ArmSystem(LinearOpMode opMode) {
        this.flyWheels = opMode.hardwareMap.get(DcMotor.class, "front_right"); // flyWheels
        this.arm       = opMode.hardwareMap.get(DcMotor.class, "front_left"); // arm
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ArmPosition = 0;
    }

    public void collect() {
        flyWheels.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flyWheels.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double position = 0;
        flyWheels.setPower(0.5);
        while (position < 268) {
            position = flyWheels.getCurrentPosition();
        }
        flyWheels.setPower(0);
    }

    public void spit() {
        flyWheels.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flyWheels.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double position = 0;
        flyWheels.setPower(-0.5);
        while (position > -268) {
            position = flyWheels.getCurrentPosition();
        }
        flyWheels.setPower(0);
    }

    public void moveArmm(int place) {
        arm.setTargetPosition(place);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(1);
    }

    public void reload() {
        arm.setPower(0.3);
        while (ArmPosition > 100) {
            ArmPosition = arm.getCurrentPosition();
        }
        arm.setPower(0);
    }

    public void moveArm(Floors level) {
        switch (level) {
            case FIRST:
//                arm.setTargetPosition(343);
////                arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                arm.setPower(0.5);
                while (ArmPosition < 343) {
                    ArmPosition = arm.getCurrentPosition();
                }
                arm.setPower(0);
                break;
            case SECOND:
//                arm.setTargetPosition(408);
//                arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                arm.setPower(0.5);
                while (ArmPosition < 408) {
                    ArmPosition = arm.getCurrentPosition();
                }
                arm.setPower(0);
                break;
            case THIRD:
//                arm.setTargetPosition(430);
//                arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                arm.setPower(0.5);
                while (ArmPosition < 430) {
                    ArmPosition = arm.getCurrentPosition();
                }
                arm.setPower(0);
        }
    }
}
