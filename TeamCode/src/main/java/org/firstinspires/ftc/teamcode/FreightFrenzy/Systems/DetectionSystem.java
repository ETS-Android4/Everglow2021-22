package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class DetectionSystem {

    public static final double LEFT_TARGET_DISTANCE_CM  = 65;
    public static final double MID_TARGET_DISTANCE_CM   = 60;
    public static final double RIGHT_TARGET_DISTANCE_CM = 65;
    public static final double ERROR_THRESHOLD_CM       = 30;

    private final LinearOpMode opMode;
    private final DistanceSensor leftSensor;
    private final DistanceSensor midSensor;
    private final DistanceSensor rightSensor;

    public DetectionSystem(LinearOpMode opMode) {
        this.opMode      = opMode;
        this.leftSensor  = opMode.hardwareMap.get(DistanceSensor.class, "distance_sensor_left");
        this.midSensor   = opMode.hardwareMap.get(DistanceSensor.class, "distance_sensor_middle");
        this.rightSensor = opMode.hardwareMap.get(DistanceSensor.class, "distance_sensor_right");
    }

    public ArmSystem.Floors findTargetFloor2(){
        double distanceLeft = leftSensor.getDistance(DistanceUnit.CM);
        double distanceMid = midSensor.getDistance(DistanceUnit.CM);
        double distanceRight = rightSensor.getDistance(DistanceUnit.CM);

        double errorLeft = Math.abs(LEFT_TARGET_DISTANCE_CM - distanceLeft);
        double errorMid = Math.abs(MID_TARGET_DISTANCE_CM - distanceMid);
        double errorRight = Math.abs(RIGHT_TARGET_DISTANCE_CM - distanceRight);

        ArmSystem.Floors targetFloor;
        if (errorLeft < errorMid && errorLeft < errorRight ){
            targetFloor = ArmSystem.Floors.FIRST;
        }else if (errorMid < errorLeft && errorMid < errorRight ){
            targetFloor = ArmSystem.Floors.FIRST;
        }else {
            targetFloor = ArmSystem.Floors.FIRST;
        }

        opMode.telemetry.addData("distanceLeft", distanceLeft);
        opMode.telemetry.addData("distanceMid", distanceMid);
        opMode.telemetry.addData("distanceRight", distanceRight);
        opMode.telemetry.addData("targetFloor", targetFloor);
        opMode.telemetry.update();

        return targetFloor;

    }
}
