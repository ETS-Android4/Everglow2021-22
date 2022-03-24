package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class ArmSystem {

    private final LinearOpMode opMode;
    public DcMotorEx flyWheels;
    public DcMotor arm;
    public TouchSensor touch;
    public TouchSensor underHand;
    public int changeHeight = 0;
    private boolean loaded = false;
    private boolean firstFloor = false;
    private Integer targetPosition = null;
    private CollectState collectState = CollectState.STOPPED;

    public ArmSystem(LinearOpMode opMode) {
        this.flyWheels = opMode.hardwareMap.get(DcMotorEx.class, "flywheels");
        this.arm = opMode.hardwareMap.get(DcMotor.class, "arm");
        touch = opMode.hardwareMap.get(TouchSensor.class, "touch");
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flyWheels.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
        flyWheels.setVelocity(-3000);
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
     * Refuse to move arm if bottom touch sensor is pressed
     */
    public void slipperUnder(){
        while(underHand.isPressed()){
            reload();
        }
    }
    /**
     * Move the arm to a target position in ticks.
     *
     * @param place the target position in ticks.
     */
    public void moveArm(int place) {
        loaded = false;
        firstFloor = false;
        this.targetPosition = null;
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setTargetPosition(place + changeHeight);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.8);
    }

    public void moveArmWithoutWobble(int place) {
        loaded = false;
        firstFloor = false;
        this.targetPosition = place;
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setTargetPosition(place);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.5);
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
        arm.setTargetPosition(0);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.35);
        loaded = true;
    }

    /**
     * Turns the arm motor off when the arm is close to resting position.
     */
    public void restOnLoad() {
        if (-50 <= arm.getCurrentPosition() && loaded) {
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
     *
     * @param level the target floor.
     */
    public void moveArm(Floors level) {
        switch (level) {
            case THIRD:
                moveArm(-1970);
                break;
            case SECOND:
                moveArm(-2350);
                break;
            case FIRST:
                moveArm(-2400);
                firstFloor = true;
                break;
            case TOTEM:
                moveArm(-1900);
                break;
            case OBSTACLE:
                moveArm(-250);
                break;
        }
    }

    /**
     * Move the arm to a target floor in front of the robot.
     *
     * @param level the target floor.
     */
    public void autonomousMoveArm(Floors level) {
        switch (level) {
            case THIRD:
                moveArm(-800);
                break;
            case SECOND:
                moveArm(-580);
                break;
            case FIRST:
                moveArm(-300);
                break;
        }
    }

    /**
     * Autonomous freight placement sequence. Moves the arm to the target floor in front of the robot,
     * and deploys the freight.
     *
     * @param floor the target floor.
     */
    public void autonomousPlaceFreight(Floors floor) {
        autonomousMoveArm(floor);
        TimeUtils.sleep(1000);
        spit();
        TimeUtils.sleep(500);
        stop();
    }

    public void awaitArmArrival() {
        while (Math.abs(arm.getTargetPosition() - arm.getCurrentPosition()) > 20) {
            TimeUtils.sleep(1);
        }
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
        FIRST, SECOND, THIRD, TOTEM, OBSTACLE;

        /**
         * When on the blue side, the totem pickup needs to be adjusted. This method returns the location the totem system should go to given the detected floor.
         *
         * @param mirror the mirror value, -1 if in red side or 1 if in blue side.
         * @return the floor that the armsystem should pick the totem up in.
         */
        public Floors switchIfMirrored(int mirror) {
            if (mirror == 1) {
                return this;
            } else {
                switch (this) {
                    case FIRST:
                        return THIRD;
                    case SECOND:
                        return SECOND;
                    case THIRD:
                        return FIRST;
                    default:
                        throw new IllegalArgumentException("switchIfMirrored given illegal arguments. Legal arguments are: FIRST, SECOND, THIRD");
                }
            }

        }
    }
}
