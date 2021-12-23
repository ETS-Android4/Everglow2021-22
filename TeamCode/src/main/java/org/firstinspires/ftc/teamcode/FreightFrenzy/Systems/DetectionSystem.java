package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class DetectionSystem {

    private static final double LEFT_TARGET_DISTANCE_CM = 65;
    private static final double MID_TARGET_DISTANCE_CM = 60;
    private static final double RIGHT_TARGET_DISTANCE_CM = 65;

    private final LinearOpMode opMode;
    private final DistanceSensor leftSensor;
    private final DistanceSensor midSensor;
    private final DistanceSensor rightSensor;

    public DetectionSystem(LinearOpMode opMode) {
        this.opMode = opMode;
        this.leftSensor = opMode.hardwareMap.get(DistanceSensor.class, "distance_sensor_left");
        this.midSensor = opMode.hardwareMap.get(DistanceSensor.class, "distance_sensor_middle");
        this.rightSensor = opMode.hardwareMap.get(DistanceSensor.class, "distance_sensor_right");
    }

    public ArmSystem.Floors findTargetFloor2(){
        double distanceLeft = leftSensor.getDistance(DistanceUnit.CM);
        double distanceMid = midSensor.getDistance(DistanceUnit.CM);
        double distanceRight = leftSensor.getDistance(DistanceUnit.CM);

        double errorLeft = Math.abs(LEFT_TARGET_DISTANCE_CM - distanceLeft);
        double errorMid = Math.abs(MID_TARGET_DISTANCE_CM - distanceMid);
        double errorRight = Math.abs(RIGHT_TARGET_DISTANCE_CM - distanceRight);

        opMode.telemetry.addData("distanceLeft", distanceLeft);
        opMode.telemetry.addData("distanceMid", distanceMid);
        opMode.telemetry.addData("distanceRight", distanceRight);
        opMode.telemetry.update();

        if (errorLeft < errorMid && errorLeft < errorRight ){
            return ArmSystem.Floors.FIRST;
        }else if (errorMid < errorLeft && errorMid < errorRight ){
            return ArmSystem.Floors.SECOND;
        }else {
            return ArmSystem.Floors.THIRD;
        }



    }
}
