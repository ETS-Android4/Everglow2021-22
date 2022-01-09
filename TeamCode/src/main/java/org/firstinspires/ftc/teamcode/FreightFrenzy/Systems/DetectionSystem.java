package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class DetectionSystem {

    public static final double LEFT_TARGET_DISTANCE_CM  = 45;
    public static final double RIGHT_TARGET_DISTANCE_CM = 45;
    public static final double ERROR_THRESHOLD_CM       = 15;

    private static final double INFINITY = 9999999;

    private final LinearOpMode   opMode;
    public final DistanceSensor leftSensor;
    public final DistanceSensor rightSensor;


    private List<Double> errorLeftValues = new ArrayList<>();
    private List<Double> errorRightValues = new ArrayList<>();

    private final ArmSystem armSystem;

    public DetectionSystem(LinearOpMode opMode, ArmSystem armSystem) {
        this.opMode      = opMode;
        this.leftSensor  = opMode.hardwareMap.get(DistanceSensor.class, "distance_sensor_left");
        this.rightSensor = opMode.hardwareMap.get(DistanceSensor.class, "distance_sensor_right");
        this.armSystem = armSystem; // todo: this creates a new arm system, which can cause bugs since it's a seprate arm system and ArmSystem has local variables`
        reset();
    }

    public void debug(){
        opMode.telemetry.addData("errorLeftValues: ", errorLeftValues.toString());
        opMode.telemetry.addData("errorRightVales: ", errorLeftValues.toString());
    }

    public void reset(){
        errorLeftValues = new ArrayList<>();
        errorRightValues = new ArrayList<>();
    }

    public void scan(){
        double distanceLeft = leftSensor.getDistance(DistanceUnit.CM);
        double distanceRight = rightSensor.getDistance(DistanceUnit.CM);
        double errorLeft = Math.abs(LEFT_TARGET_DISTANCE_CM - distanceLeft);
        double errorRight = Math.abs(RIGHT_TARGET_DISTANCE_CM - distanceRight);
        errorLeftValues.add(errorLeft);
        errorRightValues.add(errorRight);
    }

    public ArmSystem.Floors findTargetFloorAfterScan(){
        ArmSystem.Floors targetFloor;
        Double minErrorRight = MathUtils.min(errorRightValues);
        Double minErrorLeft = MathUtils.min(errorLeftValues);
        if (minErrorLeft == null || minErrorRight == null){
            // scan was never called before this function was called
            opMode.telemetry.addLine("error: findTargetFloorAfterScan() called before scan was ever called.");
            return ArmSystem.Floors.FIRST;
        }
        if (minErrorRight > ERROR_THRESHOLD_CM && minErrorLeft > ERROR_THRESHOLD_CM) {
            targetFloor = ArmSystem.Floors.THIRD;
        } else if (minErrorLeft < minErrorRight) {
            targetFloor = ArmSystem.Floors.FIRST;
        } else {
            targetFloor = ArmSystem.Floors.SECOND;
        }
        opMode.telemetry.addData("target Floor:", targetFloor);
        opMode.telemetry.update();
        return targetFloor;
    }

    /**
     * Finds the floor where we need to place the cube, moving sideways to the left and right in order to ensure the scan is correct
     * @param power How fast to drive. Should be 0.2
     * @param distance How far to drive each way in cm. Should be 2.
     * @param drivingSystem the driving system being used.
     */
    public ArmSystem.Floors smartFindTargetFloor(double power, double distance, DrivingSystem drivingSystem){
        reset();
        armSystem.moveArm(-300);
        TimeUtils.sleep(700);

        drivingSystem.driveSidewaysAndScan(distance, power, this);
        TimeUtils.sleep(200);
        scan();
        drivingSystem.driveSidewaysAndScan(distance, -power, this);
        TimeUtils.sleep(200);
        scan();
        drivingSystem.driveSidewaysAndScan(distance, -power, this);
        TimeUtils.sleep(200);
        scan();
        drivingSystem.driveSidewaysAndScan(distance, power, this);
        TimeUtils.sleep(200);
        scan();
        ArmSystem.Floors targetFloor =  findTargetFloorAfterScan();

        armSystem.autonomousReload();
        return targetFloor;
    }

    public ArmSystem.Floors findTargetFloor2() {
        armSystem.moveArm(-300);
        TimeUtils.sleep(700);
        double distanceLeft = leftSensor.getDistance(DistanceUnit.CM);
        double distanceRight = rightSensor.getDistance(DistanceUnit.CM);
        armSystem.autonomousReload();
        double errorLeft = Math.abs(LEFT_TARGET_DISTANCE_CM - distanceLeft);
        double errorRight = Math.abs(RIGHT_TARGET_DISTANCE_CM - distanceRight);

        ArmSystem.Floors targetFloor;
        if (errorLeft > ERROR_THRESHOLD_CM && errorRight > ERROR_THRESHOLD_CM) {
            targetFloor = ArmSystem.Floors.THIRD;
        } else if (errorLeft < errorRight) {
            targetFloor = ArmSystem.Floors.FIRST;
        } else {
            targetFloor = ArmSystem.Floors.SECOND;
        }
//        opMode.telemetry.addData("distanceLeft", distanceLeft);
//        opMode.telemetry.addData("distanceRight", distanceRight);
//        opMode.telemetry.addData("targetFloor", targetFloor);
//        opMode.telemetry.update();
        return targetFloor;
    }
}
