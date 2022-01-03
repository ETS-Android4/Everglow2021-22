package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class ArmSystem {
    public DcMotor flyWheels;
    public DcMotor arm;
    double       armPosition;
    boolean loaded = false;
    boolean firstFloor = false;
    LinearOpMode opMode;

    public enum Floors {
        FIRST, SECOND, THIRD
    }

    public ArmSystem(LinearOpMode opMode) {
        this.flyWheels = opMode.hardwareMap.get(DcMotor.class, "flywheels");
        this.arm       = opMode.hardwareMap.get(DcMotor.class, "arm");
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armPosition = 0;
        this.opMode = opMode;
    }

    public void collect() {
        flyWheels.setPower(0.5);
    }

    public void spit() {
        flyWheels.setPower(-0.5);
    }

    public void stop() {
        flyWheels.setPower(0);
    }

    public void moveArm(int place) {
        loaded = false;
        firstFloor = false;
        arm.setTargetPosition(place);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.5);
    }

    public void reload() {
        arm.setTargetPosition(-100);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.25);
        loaded = true;
    }

    public void restOnLoad() {
        if (-105 <= arm.getCurrentPosition() && loaded) {
            arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

    public void restOnFirstFloor() {
        if (-2500 >= arm.getCurrentPosition() && firstFloor) {
            arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

    public void moveArm(Floors level) {
        armPosition = arm.getCurrentPosition();
        this.opMode.telemetry.addData("armPosition", armPosition);
        this.opMode.telemetry.update();
        switch (level) {
            case THIRD:
                moveArm(-550);
                break;
            case SECOND:
                moveArm(-350);
                break;
            case FIRST:
                moveArm(-165);
        }
    }
}
