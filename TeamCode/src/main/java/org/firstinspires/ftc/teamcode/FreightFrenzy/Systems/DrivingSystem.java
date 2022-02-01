package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import static org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils.normalizeAngle;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.StopCondition;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class DrivingSystem {
    private final DcMotor frontRight;
    private final DcMotor frontLeft;
    private final DcMotor backRight;
    private final DcMotor backLeft;

    private final DistanceSensor sensorBackUp;
    private final DistanceSensor sensorBackDown;

    private final LinearOpMode opMode;

    private final BNO055IMU.Parameters parameters;
    private final BNO055IMU            imu;

    /**
     * If the robot is turning, this is the angle it will try to reach relatively to the
     * IMU's zero around the Z-axis
     */
    private double targetAngle = 0;

    private static final double COUNTS_PER_MOTOR_REV = 515;    // eg: GoBILDA Motor Encoder
    private static final double WHEEL_RADIUS_CM      = 4.8;

    /**
     * Constructor of the Driving System class.
     * Gets the driving motors and the distance sensors from the Hardware Map.
     * Initializes the driving motors and sets their Zero Power Behavior to BRAKE.
     * Initializes the internal IMU.
     *
     * @param opMode the LinearOpMode in which the driving system class is being used.
     */
    public DrivingSystem(LinearOpMode opMode) {
        // Get the driving motors from the Hardware Map
        this.frontRight = opMode.hardwareMap.get(DcMotor.class, "front_right");
        this.frontLeft  = opMode.hardwareMap.get(DcMotor.class, "front_left");
        this.backRight  = opMode.hardwareMap.get(DcMotor.class, "back_right");
        this.backLeft   = opMode.hardwareMap.get(DcMotor.class, "back_left");

        // Get sensors from the Hardware Map
        this.sensorBackUp   = opMode.hardwareMap.get(DistanceSensor.class, "distance_sensor_bu");
        this.sensorBackDown = opMode.hardwareMap.get(DistanceSensor.class, "distance_sensor_bd");

        this.opMode = opMode;

        // Set the driving motors' Zero Power Behavior to BRAKE 
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Create IMU
        parameters                                  = new BNO055IMU.Parameters();
        parameters.angleUnit                        = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit                        = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile              = "BNO055IMUCalibration.json";
        parameters.loggingEnabled                   = true;
        parameters.loggingTag                       = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = opMode.hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        stop();
        resetDistance();
    }

    /**
     * @return The current orientation of the robot around the Z-axis according to the IMU.
     */
    public double getCurrentAngle() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
    }

    /**
     * If the robot is turning, this is how much it has left to rotate.
     *
     * @return The difference between the robot's current angle and the target angle of the robot.
     * Goes from -180 to 180 degrees.
     */
    private double getAngleDeviation() {
        return normalizeAngle(getCurrentAngle() - targetAngle);
    }

    /**
     * The method we use to control the Driving System using a controller.
     *
     * @param x1 The power in the X-axis (positive = rightward).
     * @param y1 The power in the Y-axis (positive = forward).
     * @param x2 The power of the rotation around the Z-axis (positive = clockwise).
     */
    public void driveByJoystick(double x1, double y1,
                                double x2) {
        double frontRightPower = -x1 - y1 - 0.7 * x2;
        double frontLeftPower = y1 - x1 - 0.7 * x2;
        double backRightPower = -y1 + x1 - 0.7 * x2;
        double backLeftPower = y1 + x1 - 0.7 * x2;

        // Normalization of the motors' power
        if (Math.abs(frontRightPower) > 1 || Math.abs(frontLeftPower) > 1
                || Math.abs(backRightPower) > 1 || Math.abs(backRightPower) > 1) {
            double norm = Math.max(
                    Math.max(Math.abs(frontRightPower), Math.abs(frontLeftPower)),
                    Math.max(Math.abs(backRightPower), Math.abs(backLeftPower))
            );
            frontRightPower /= norm;
            frontLeftPower /= norm;
            backRightPower /= norm;
            backLeftPower /= norm;
        }

        frontRight.setPower(frontRightPower);
        frontLeft.setPower(frontLeftPower);
        backRight.setPower(backRightPower);
        backLeft.setPower(backLeftPower);
    }

    /**
     * The method we use to rotate in place. The robot rotates rapidly if the Angle Deviation is
     * large, and decelerates as it gets closer to the target angle.
     *
     * @param deg           The number of degrees to rotate (positive = clockwise).
     * @param speedDecrease The factor by which we decrease the rotation speed proportionally to the Angle Deviation.
     */
    public void turn(float deg, int speedDecrease) {
        this.targetAngle = normalizeAngle(this.targetAngle + deg);
        double theta = getAngleDeviation();

        // Rotate until the desired angle is reached within an error of 0.5 degrees
        while (Math.abs(theta) > 0.5) {
            theta = getAngleDeviation();
            double direction = (theta / Math.abs(theta));
            driveByJoystick(0, 0,
                    Math.max(Math.abs(theta / speedDecrease), 0.07)
                            * direction
            );
        }
        stop();
    }

    /**
     * The method we use to travel a set distance forwards or backwards.
     *
     * @param distance The distance to be travelled.
     * @param power    The power of the driving motors (positive = forward).
     */
    public void driveStraight(double distance, double power) {
        // The method receives a positive distance.
        if (distance < 0) {
            throw new IllegalArgumentException("driveStraight was given a negative distance: " + distance);
        }

        this.targetAngle = getCurrentAngle();
        resetDistance();
        double AverageMotors = 0;

        // Go until the desired distance is reached
        while (distance * COUNTS_PER_MOTOR_REV / (2.0 * Math.PI * WHEEL_RADIUS_CM) > AverageMotors) {
            // x2 is used to fix the natural deviation of the robot from a straight line
            driveByJoystick(0, -power, getAngleDeviation() / 40);
            AverageMotors = Math.abs(
                    (this.frontRight.getCurrentPosition() - this.frontLeft.getCurrentPosition()
                            - this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()
                    ) / 4.0
            );
        }
        stop();
    }

    /**
     * The method we use to travel a set distance rightwards or leftwards.
     *
     * @param distance The distance to be travelled.
     * @param power    The power of the driving motors (positive = rightward).
     */
    public void driveSideways(double distance, double power) {
        // The method receives a positive distance.
        if (distance < 0) {
            throw new IllegalArgumentException("driveStraight was given a negative distance: " + distance);
        }

        this.targetAngle = getCurrentAngle();
        resetDistance();
        double AverageMotors = 0;

        // Go until the desired distance is reached
        while ((Math.abs(distance) * COUNTS_PER_MOTOR_REV) / (2.0 * Math.PI * WHEEL_RADIUS_CM) > AverageMotors) {
            // x2 is used to fix the natural deviation of the robot from a straight line
            driveByJoystick(power, 0, getAngleDeviation() / 40);
            AverageMotors = Math.abs(
                    (-this.frontRight.getCurrentPosition() - this.frontLeft.getCurrentPosition()
                            + this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()
                    ) / 4.0
            );
        }
        stop();
    }

    /**
     * Drives sideways, and repeatedly calls the DetectionSystem.scan() method.
     */
    public void driveSidewaysAndScan(double distance, double power, DetectionSystem detectionSystem) {
        this.targetAngle = getCurrentAngle();
        resetDistance();
        double AverageMotors = 0;

        while ((Math.abs(distance) * COUNTS_PER_MOTOR_REV) / (2.0 * Math.PI * WHEEL_RADIUS_CM) > AverageMotors) {
            driveByJoystick(power, 0, getAngleDeviation() / 40);
            AverageMotors = Math.abs(
                    (-this.frontRight.getCurrentPosition() - this.frontLeft.getCurrentPosition()
                            + this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()
                    ) / 4.0
            );
            detectionSystem.scan();
        }
        stop();
    }

    /**
     * Drives forward until the desired distance from an obstacle (received by the distance sensors) is reached.
     */
    public void driveUntilObstacle(double distance, double power) {
        this.targetAngle = getCurrentAngle();
        resetDistance();

        while (sensorBackUp.getDistance(DistanceUnit.CM) > distance) {
            driveByJoystick(0, -power, getAngleDeviation() / 40);
            this.opMode.telemetry.addData("distance", sensorBackUp.getDistance(DistanceUnit.CM));
            this.opMode.telemetry.update();
        }
        stop();
    }

    /**
     * Same as driveUntilObstacle, but also lifts the arm so that it doesn't block the sensors.
     */
    public void moveArmAndDriveUntilObstacle(double distance, double power, ArmSystem armSystem) {
        armSystem.moveArm(-300);
        TimeUtils.sleep(700);
        driveUntilObstacle(distance, power);
        armSystem.autonomousReload();
    }

    /**
     * Drives sideways until the predicate of stopCondition returns true.
     *
     * @return the total distance traveled.
     */
    public double driveSidewaysUntil(double power, StopCondition stopCondition) {
        targetAngle = getCurrentAngle();
        resetDistance();
        double averageMotors = 0;
        this.opMode.telemetry.addData("distance", averageMotors);
        while (!stopCondition.shouldStop()) {
            driveByJoystick(power, 0, getAngleDeviation() / 40);
            averageMotors = Math.abs(
                    (-this.frontRight.getCurrentPosition() - this.frontLeft.getCurrentPosition()
                            + this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()
                    ) / 4.0
            );
            this.opMode.telemetry.addData("distance", averageMotors);
            this.opMode.telemetry.update();
        }
        stop();
        return Math.abs(averageMotors / COUNTS_PER_MOTOR_REV * (2.0 * Math.PI * WHEEL_RADIUS_CM));
    }

    /**
     * Drives straight until the predicate of stopCondition returns true.
     *
     * @return the total distance traveled.
     */
    public double driveStraightUntil(double power, StopCondition stopCondition) {
        targetAngle = getCurrentAngle();
        power *= -1;
        resetDistance();
        double averageMotors = 0;
        this.opMode.telemetry.addData("distance", averageMotors);
        while (!stopCondition.shouldStop()) {
            driveByJoystick(0, power, getAngleDeviation() / 40);
            averageMotors = Math.abs(
                    (this.frontRight.getCurrentPosition() - this.frontLeft.getCurrentPosition()
                            - this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()
                    ) / 4.0
            );
            this.opMode.telemetry.addData("distance", averageMotors);
            this.opMode.telemetry.update();
        }
        stop();
        return Math.abs(averageMotors / COUNTS_PER_MOTOR_REV * (2.0 * Math.PI * WHEEL_RADIUS_CM));
    }

    /**
     * @param duration       The duration (in seconds) of the scanning.
     * @param distanceSensor The sensor for which to measure an average distance received over time.
     * @return The average distance shown by distanceSensor over the given span of time.
     */
    public double distanceSensorAverage(double duration, DistanceSensor distanceSensor) {
        List<Double> distances = new ArrayList<>();
        ElapsedTime elapsedTime = new ElapsedTime();

        elapsedTime.reset();
        elapsedTime.startTime();

        while (elapsedTime.seconds() < duration) {
            double distanceDown = distanceSensor.getDistance(DistanceUnit.CM);
            if (distanceDown < 50) {
                distances.add(distanceDown);
            }
        }

        return MathUtils.average(distances);
    }

    /**
     * Goes to a fixed distance from an object, given the current distance from said object.
     *
     * @param currentDistance The current distance from the object.
     * @param distance        The wanted distance from the object.
     * @param power           The power of the driving motors.
     */
    public void driveToFixedDistance(double currentDistance, double distance, double power) {
        if (currentDistance > distance) {
            driveStraight(currentDistance - distance, -power);
        } else {
            driveStraight(distance - currentDistance, power);
        }
    }

    /**
     * A method to execute the Capping Sequence: the robot caps the Shipping Hub with the Team Shipping Element.
     *
     * @param armSystem The Arm System to be used by the CS.
     */
    public void CS(ArmSystem armSystem) {
        // Go right until left until the Shipping Hubs' stem is detected
        while (sensorBackUp.getDistance(DistanceUnit.CM) > 50 || sensorBackDown.getDistance(DistanceUnit.CM) > 50) {
            driveByJoystick(-0.4, 0, 0);
        }
        stop();

        double avgDistance = distanceSensorAverage(2, sensorBackDown);
        opMode.telemetry.addData("avgDistance: ", avgDistance);
        opMode.telemetry.update();
        driveToFixedDistance(avgDistance, 35, 0.3);

        while (sensorBackDown.getDistance(DistanceUnit.CM) < 40) {
            turn(1, 200);
//            avgDistance = distanceSensorAverage(2, sensorBackDown);
//            opMode.telemetry.addData("avgDistance: ", avgDistance);
//            opMode.telemetry.update();
        }

        armSystem.placeTotem();
        driveStraight(10, 0.3);
        armSystem.stop();
        armSystem.autonomousReload();
        TimeUtils.sleep(3000);
    }

    /**
     * Resets all the driving motors' encoders and sets their run mode to RUN_USING_ENCODER.
     */
    public void resetDistance() {
        this.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Brings the robot to a complete stop.
     */
    public void stop() {
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
    }
}