package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class ArmSystem {

    public enum CollectState{
        STOPPED, COLLECTING, SPITTING
    }

    public        DcMotor      flyWheels;
    public        DcMotor      arm;
    private       boolean      loaded     = false;
    private       boolean      firstFloor = false;
    private final LinearOpMode opMode;

    private CollectState collectState = CollectState.STOPPED;

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

    public CollectState getCollectState() {
        return collectState;
    }

    public void collect() {
        collectState = CollectState.COLLECTING;
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flyWheels.setPower(1);
    }

    public void toggleCollecting(){
        if (collectState == CollectState.COLLECTING){
            stop();
        }else {
            collect();
        }
    }

    public void toggleSpitting(){
        if (collectState == CollectState.SPITTING){
            stop();
        }else {
            spit();
        }
    }


    public void spit() {
        collectState = CollectState.SPITTING;
        flyWheels.setPower(-0.5);
    }

    public void stop() {
        collectState = CollectState.STOPPED;
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
        arm.setPower(0.35);
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
            case THIRD:
                moveArm(-2050);
                break;
            case SECOND:
                moveArm(-2300);
                break;
            case FIRST:
                moveArm(-2400);
                firstFloor = true;
                break;
            case TOTEM:
                moveArm(-1900);
                break;
        }
    }

    public void autonomousMoveArm(Floors level) {
        switch (level) {
            case THIRD:
                moveArm(-650);
                break;
            case SECOND:
                moveArm(-350);
                break;
            case FIRST:
                moveArm(-100);
                break;
        }
    }

    public void autonomousPlaceFreight(Floors floor){
        autonomousMoveArm(floor);
        TimeUtils.sleep(1000);
        spit();
        TimeUtils.sleep(500);
        stop();
    }

    public void placeTotem() {
        moveArm(Floors.TOTEM);
        TimeUtils.sleep(2000);
        arm.setTargetPosition(-2000);
        arm.setPower(0.1);
        TimeUtils.sleep(2000);
        spit();
        TimeUtils.sleep(200);
    }
}
