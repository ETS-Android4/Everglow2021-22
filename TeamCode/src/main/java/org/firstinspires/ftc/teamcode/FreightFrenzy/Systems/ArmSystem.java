package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class ArmSystem {

    /**
     * Enum of collection states: STOPPED, COLLECTING, SPITTING.
     */
    public enum CollectState {
        STOPPED, COLLECTING, SPITTING
    }

    /**
     * Enum of the different floors the arm should be able reach: FIRST, SECOND, THIRD, TOTEM.
     */
    public enum Floors {
        FIRST, SECOND, THIRD, TOTEM
    }

    public        DcMotor      flyWheels;
    public        DcMotor      arm;
    private       boolean      loaded         = false;
    private       boolean      firstFloor     = false;
    private       Integer      targetPosition = null;
    private final LinearOpMode opMode;

    private CollectState collectState = CollectState.STOPPED;

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

    /**
     * Activate the flywheels inwards, in order to collect a freight.
     */
    public void collect() {
        collectState = CollectState.COLLECTING;
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flyWheels.setPower(1);
    }

    /**
     * Toggle the collection on and off.
     */
    public void toggleCollecting() {
        if (collectState == CollectState.COLLECTING) {
            stop();
        } else {
            collect();
        }
    }

    /**
     * Activate the flywheels outwards, in order to deploy a freight.
     */
    public void spit() {
        collectState = CollectState.SPITTING;
        flyWheels.setPower(-0.5);
    }

    /**
     * Toggle the deployment on and off
     */
    public void toggleSpitting() {
        if (collectState == CollectState.SPITTING) {
            stop();
        } else {
            spit();
        }
    }

    /**
     * Stops the flywheels.
     */
    public void stop() {
        collectState = CollectState.STOPPED;
        flyWheels.setPower(0);
    }

    /**
     * Move the arm to a target position in ticks.
     * @param place the target position in ticks.
     */
    public void moveArm(int place) {
        loaded              = false;
        firstFloor          = false;
        this.targetPosition = null;
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setTargetPosition(place);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.5);
    }

    public void moveArmWithoutWobble(int place) {
        loaded              = false;
        firstFloor          = false;
        this.targetPosition = place;
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setTargetPosition(place);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.8);
        this.opMode.telemetry.addLine("fast");
        this.opMode.telemetry.update();
    }

    public void slowArm() {
        if (targetPosition != null && Math.abs(targetPosition - arm.getCurrentPosition()) < 800) {
            arm.setPower(0.2);
            this.opMode.telemetry.addLine("slow");
        } else {
            this.opMode.telemetry.addLine("fast inside slow arm");
        }
        this.opMode.telemetry.update();
    }

    /**
     * Return the arm to the resting position.
     */
    public void reload() {
        firstFloor = false;
        arm.setTargetPosition(-100);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.35);
        loaded = true;
    }

    /**
     * Turns the arm motor off when the arm is close to resting position.
     */
    public void restOnLoad() {
        if (-105 <= arm.getCurrentPosition() && loaded) {
            arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    /**
     * Turns the arm motor off when the arm is close to the first floor resting position.
     */
    public void restOnFirstFloor() {
        if (-2380 >= arm.getCurrentPosition() && firstFloor) {
            arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            arm.setPower(0);
            arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    /**
     * Reload method to be used in an autonomous. Contains restOnLoad() as well.
     */
    public void autonomousReload() {
        reload();
        restOnLoad();
        TimeUtils.sleep(500);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    /**
     * Move the arm to a target floor behind the robot.
     * @param level the target floor.
     */
    public void moveArm(Floors level) {
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

    /**
     * Move the arm to a target floor in front of the robot.
     * @param level the target floor.
     */
    public void autonomousMoveArm(Floors level) {
        switch (level) {
            case THIRD:
                moveArm(-550);
                break;
            case SECOND:
                moveArm(-335);
                break;
            case FIRST:
                moveArm(-120);
                break;
        }
    }

    /**
     * Autonomous freight placement sequence. Moves the arm to the target floor in front of the robot,
     * and deploys the freight.
     * @param floor the target floor.
     */
    public void autonomousPlaceFreight(Floors floor) {
        autonomousMoveArm(floor);
        TimeUtils.sleep(1000);
        spit();
        TimeUtils.sleep(500);
        stop();
    }

    /**
     * Autonomous Shipping Element placement sequence. Moves the arm to the TOTEM level,
     * lowers the arm and spits.
     */
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
