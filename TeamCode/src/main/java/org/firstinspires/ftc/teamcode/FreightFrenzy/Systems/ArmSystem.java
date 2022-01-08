package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class ArmSystem {
    public        DcMotor      flyWheels;
    public        DcMotor      arm;
    private       boolean      loaded     = false;
    private       boolean      firstFloor = false;
    private final LinearOpMode opMode;

    public enum Floors {
        FIRST, SECOND, THIRD, TOTEM
    }

    public ArmSystem(LinearOpMode opMode) {
        this.flyWheels = opMode.hardwareMap.get(DcMotor.class, "flywheels");
        this.arm       = opMode.hardwareMap.get(DcMotor.class, "arm");
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.opMode = opMode;
    }

    public void collect() {
        flyWheels.setPower(0.5);
    }

    public void spit() {
        flyWheels.setPower(-0.3);
    }

    public void stop() {
        flyWheels.setPower(0);
    }

    public void moveArm(int place) {
        loaded     = false;
        firstFloor = false;
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setTargetPosition(place);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.5);
    }

    public void reload() {
        firstFloor = false;
        arm.setTargetPosition(-100);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.3);
        loaded = true;
    }

    public void restOnLoad() {
        if (-105 <= arm.getCurrentPosition() && loaded) {
            arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    public void restOnFirstFloor() {
        if (-2400 >= arm.getCurrentPosition() && firstFloor) {
            arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    public void autonomousReload() {
        reload();
        restOnLoad();
        TimeUtils.sleep(500);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void moveArm(Floors level) {
        this.opMode.telemetry.addData("armPosition", arm.getCurrentPosition());
        switch (level) {
            case TOTEM:
                moveArm(-1850); // todo: figure out exact number
                break;
            case THIRD:
                moveArm(-2150);
                break;
            case SECOND:
                moveArm(-2300);
                break;
            case FIRST:
                moveArm(-2400);
                firstFloor = true;
                break;

        }
    }

    public void autonomousMoveArm(Floors level) {
        switch (level) {
            case TOTEM:
                moveArm(-850); // todo: figure out exact number
                break;
            case THIRD:
                moveArm(-650);
                break;
            case SECOND:
                moveArm(-450);
                break;
            case FIRST:
                moveArm(-180);
                break;
        }
    }
}
