package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class DetectionSystem {

    public static final double LEFT_TARGET_DISTANCE_CM  = 65;
    public static final double RIGHT_TARGET_DISTANCE_CM = 65;
    public static final double ERROR_THRESHOLD_CM       = 30;

    private final LinearOpMode   opMode;
    public final DistanceSensor leftSensor;
    public final DistanceSensor rightSensor;

    private final ArmSystem arm;

    public DetectionSystem(LinearOpMode opMode) {
        this.opMode      = opMode;
        this.leftSensor  = opMode.hardwareMap.get(DistanceSensor.class, "distance_sensor_left");
        this.rightSensor = opMode.hardwareMap.get(DistanceSensor.class, "distance_sensor_right");
        this.arm = new ArmSystem(opMode);
    }

    public ArmSystem.Floors findTargetFloor2() {
        arm.moveArm(-300);
        TimeUtils.sleep(700);
        double distanceLeft = leftSensor.getDistance(DistanceUnit.CM);
        double distanceRight = rightSensor.getDistance(DistanceUnit.CM);
        arm.reload();
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
        opMode.telemetry.addData("distanceLeft", distanceLeft);
        opMode.telemetry.addData("distanceRight", distanceRight);
        opMode.telemetry.addData("targetFloor", targetFloor);
        opMode.telemetry.update();
        return targetFloor;

    }
}
