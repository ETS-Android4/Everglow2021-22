package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import static org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem.ERROR_THRESHOLD_CM;
import static org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem.LEFT_TARGET_DISTANCE_CM;
import static org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem.MID_TARGET_DISTANCE_CM;
import static org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem.RIGHT_TARGET_DISTANCE_CM;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * A detection System that only uses a left and a right sensor.
 * If both the left and right sensors returns values that are too large, then the scoring element is assumed to be in the middle.
 */
public class TwoSensorDetectionSystem {
    LinearOpMode opMode;
    DistanceSensor rightSensor;
    DistanceSensor leftSensor;

    public TwoSensorDetectionSystem(LinearOpMode opMode) {
        this.opMode = opMode;
        this.leftSensor = opMode.hardwareMap.get(DistanceSensor.class, "distance_sensor_left");
        this.rightSensor = opMode.hardwareMap.get(DistanceSensor.class, "distance_sensor_right");
    }

    public ArmSystem.Floors findTargetFloor(){
        double distanceLeft = leftSensor.getDistance(DistanceUnit.CM);
        double distanceRight = rightSensor.getDistance(DistanceUnit.CM);
        double errorLeft = Math.abs(LEFT_TARGET_DISTANCE_CM - distanceLeft);
        double errorRight = Math.abs(RIGHT_TARGET_DISTANCE_CM - distanceRight);

        ArmSystem.Floors targetFloor;
        if (errorLeft > ERROR_THRESHOLD_CM && errorRight > ERROR_THRESHOLD_CM){
            targetFloor = ArmSystem.Floors.SECOND;
        }else if (errorLeft < errorRight){
            targetFloor = ArmSystem.Floors.FIRST;
        }else {
            targetFloor = ArmSystem.Floors.THIRD;
        }
        opMode.telemetry.addData("distanceLeft", distanceLeft);
        opMode.telemetry.addData("distanceRight", distanceRight);
        opMode.telemetry.addData("targetFloor", targetFloor);
        opMode.telemetry.update();
        return targetFloor;
    }
}
