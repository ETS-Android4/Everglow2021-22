package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import static org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils.normalizeAngle;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;

import static java.lang.Math.abs;
import static java.lang.Math.copySign;
import static java.lang.Math.cos;
import static java.lang.Math.hypot;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.StopCondition;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class DrivingSystem {

    private static final int ROTATE_SPEED_DECREASE = 40;
    private static final double ACCELERATION_BUMPING_THRESHOLD = 14;
    private final DcMotor frontRight;
    private final DcMotor frontLeft;
    private final DcMotor backRight;
    private final DcMotor backLeft;

    @Deprecated
    private DistanceSensor sensorBackUp;
    @Deprecated
    private DistanceSensor sensorBackDown;

    private final LinearOpMode opMode;
    private final ArmSystem armSystem;
    private final ColorSystem colorSystem;

    private final BNO055IMU.Parameters parameters;
    private final BNO055IMU imu;


    /**
     * If the robot is turning, this is the angle it will try to reach relatively to the
     * IMU's zero around the Z-axis.
     */
    private double targetAngle = 0;

    private static final double COUNTS_PER_MOTOR_REV = 515;    // eg: GoBILDA Motor Encoder
    private static final double WHEEL_RADIUS_CM = 4.8;

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
        armSystem = new ArmSystem(opMode);
        colorSystem = new ColorSystem(opMode);
        this.frontRight = opMode.hardwareMap.get(DcMotor.class, "front_right");
        this.frontLeft = opMode.hardwareMap.get(DcMotor.class, "front_left");
        this.backRight = opMode.hardwareMap.get(DcMotor.class, "back_right");
        this.backLeft = opMode.hardwareMap.get(DcMotor.class, "back_left");

        // Get sensors from the Hardware Map
        this.opMode = opMode;

        // Set the driving motors' Zero Power Behavior to BRAKE 
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // Create IMU
        parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = opMode.hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // copied from https://ftcforum.firstinspires.org/forum/ftc-technology/53812-mounting-the-revhub-vertically
        byte AXIS_MAP_CONFIG_BYTE = 0x6; //This is what to write to the AXIS_MAP_CONFIG register to swap x and z axes
        byte AXIS_MAP_SIGN_BYTE = 0x1; //This is what to write to the AXIS_MAP_SIGN register to negate the z axis
        //Need to be in CONFIG mode to write to registers
        imu.write8(BNO055IMU.Register.OPR_MODE,BNO055IMU.SensorMode.CONFIG.bVal & 0x0F);
        TimeUtils.sleep(100); //Changing modes requires a delay before doing anything else
        //Write to the AXIS_MAP_CONFIG register
        imu.write8(BNO055IMU.Register.AXIS_MAP_CONFIG,AXIS_MAP_CONFIG_BYTE & 0x0F);
        //Write to the AXIS_MAP_SIGN register
        imu.write8(BNO055IMU.Register.AXIS_MAP_SIGN,AXIS_MAP_SIGN_BYTE & 0x0F);
        //Need to change back into the IMU mode to use the gyro
        imu.write8(BNO055IMU.Register.OPR_MODE,BNO055IMU.SensorMode.IMU.bVal & 0x0F);
        TimeUtils.sleep(100); //Changing modes again requires a delay


        stop();
        resetDistance();
    }

    public Acceleration getAcceleration() {
        return imu.getLinearAcceleration();
    }

    public double getAccelerationMagnitude() {
        Acceleration acceleration = getAcceleration();
        return Math.sqrt(
                Math.pow(acceleration.xAccel, 2) +
                        Math.pow(acceleration.yAccel, 2) +
                        Math.pow(acceleration.zAccel, 2)
        );
    }

    public Orientation getFullAngle() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES);
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
    public double getAngleDeviation() {
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
        double frontRightPower = -y1 - x1 - 0.7 * x2;
        double frontLeftPower = -y1 + x1 + 0.7 * x2;
        double backRightPower = -y1 + x1 - 0.7 * x2;
        double backLeftPower = -y1 - x1 + 0.7 * x2;

        // Normalization of the driving motors' power
        if (abs(frontRightPower) > 1 || abs(frontLeftPower) > 1
                || abs(backRightPower) > 1 || abs(backRightPower) > 1) {
            double norm = max(
                    max(abs(frontRightPower), abs(frontLeftPower)),
                    max(abs(backRightPower), abs(backLeftPower))
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

    public void driveByJoystickWithRelationToAxis(double x1, double y1, double x2) {
        driveByJoystick(1.16*(sin((90-getCurrentAngle()) * Math.PI / 180) * x1 + sin(getCurrentAngle() * Math.PI / 180) * y1),
                -cos(getCurrentAngle() * Math.PI / 180) * y1 + cos((90-getCurrentAngle()) * Math.PI / 180) * x1,
                x2);
    }

    public void rotateAroundArm(double power) {
        double factor = 3;
        backLeft.setPower(power / factor / 2);
        backRight.setPower(-power / factor);
        frontLeft.setPower(-power / factor / 8.5);
        frontRight.setPower(power / factor / 8.5);
    }

    public void driveToPoint(double targetX, double targetY, double ang, double driveSpeed, double rotateSpeed) {
        resetDistance();

        double currentX = 0;
        double currentY = 0;
        this.targetAngle = ang;

        final double ROTATE_SPEED_MIN = 0.2;

        double lastDistanceTravelled = 0;
        while (true) {
            double angleDeviation = getAngleDeviation();
            double xDiff = targetX - currentX;
            double yDiff = targetY - currentY;
            boolean xPassed = abs(currentX) >= abs(targetX);
            boolean yPassed = abs(currentY) >= abs(targetY);

            double xPower, yPower, rotatePower;
            if (Math.abs(angleDeviation) < 1){
                rotatePower = 0;
            }else {
                rotatePower = copySign(
                        min(abs(angleDeviation)/180 * (abs(rotateSpeed) - ROTATE_SPEED_MIN) + ROTATE_SPEED_MIN, 1),
                        angleDeviation);
            }
            double maxDiff = max(abs(xDiff), abs(yDiff));
            if (xPassed || maxDiff == 0) {
                xPower = 0;
            } else {
                xPower = xDiff/maxDiff * driveSpeed;
            }

            if (yPassed || maxDiff == 0) {
                yPower = 0;
            } else {
                yPower = yDiff/maxDiff * driveSpeed;
            }
            // normalize so that xPower^2 + yPower^2 + rotatePower^2 = 1
            double powerHypot = sqrt(pow(xPower, 2) + pow(yPower, 2) + pow(rotatePower, 2));
            double xPowerNormalized = xPower/powerHypot;
            double yPowerNormalized = yPower/powerHypot;



            if (yPassed && xPassed && Math.abs(angleDeviation) < 1){
                break;
            }

            driveByJoystickWithRelationToAxis(xPower, yPower, rotatePower);

            double currEncoder = (abs(backLeft.getCurrentPosition())
                    + abs(backRight.getCurrentPosition())
                    + abs(frontLeft.getCurrentPosition())
                    + abs(frontRight.getCurrentPosition()))/4.;
            double currentDistanceTraveled = currEncoder / COUNTS_PER_MOTOR_REV * (2.0 * Math.PI * WHEEL_RADIUS_CM);
            double distanceTraveledNow = currentDistanceTraveled - lastDistanceTravelled;
            lastDistanceTravelled = currentDistanceTraveled;
            currentX += xPowerNormalized *  distanceTraveledNow;
            currentY += yPowerNormalized * distanceTraveledNow;

            opMode.telemetry.addData("angleDeviation: ", angleDeviation);
            opMode.telemetry.addData("rotatePower: ", rotatePower);
            opMode.telemetry.addData("currentX: ", currentX);
            opMode.telemetry.addData("currentY: ", currentY);
            opMode.telemetry.addData("xDiff: ", xDiff);
            opMode.telemetry.addData("yDiff: ", yDiff);
            opMode.telemetry.addData("powerX: ", xPower);
            opMode.telemetry.addData("powerY: ", yPower);
            opMode.telemetry.addData("xPassed: ", xPassed);
            opMode.telemetry.addData("yPassed: ", yPassed);
            opMode.telemetry.addData("currentDistance: ", currentDistanceTraveled);
            opMode.telemetry.update();
        }
        stop();
    }

    public void driveToPoint2(double targetX, double targetY, double ang, double driveSpeed, double rotateSpeed) {
        resetDistance();

        double currentX = 0;
        double currentY = 0;
        this.targetAngle = ang;

        final double ROTATE_SPEED_MIN = 0.2;

        double lastDistanceTravelled = 0;
        while (true) {
            double angleDeviation = getAngleDeviation();
            double xDiff = targetX - currentX;
            double yDiff = targetY - currentY;
            boolean xPassed = abs(currentX) >= abs(targetX);
            boolean yPassed = abs(currentY) >= abs(targetY);

            double xPower, yPower, rotatePower;
            if (Math.abs(angleDeviation) < 1){
                rotatePower = 0;
            }else {
                rotatePower = copySign(
                        min(abs(angleDeviation)/180 * (abs(rotateSpeed) - ROTATE_SPEED_MIN) + ROTATE_SPEED_MIN, 1),
                        angleDeviation);
            }
            double maxDiff = max(abs(xDiff), abs(yDiff));
            if (xPassed || maxDiff == 0) {
                xPower = 0;
            } else {
                xPower = xDiff/maxDiff * driveSpeed;
            }

            if (yPassed || maxDiff == 0) {
                yPower = 0;
            } else {
                yPower = yDiff/maxDiff * driveSpeed;
            }
            // normalize so that xPower^2 + yPower^2 + rotatePower^2 = 1
            double powerHypot = sqrt(pow(xPower, 2) + pow(yPower, 2) + pow(rotatePower, 2));
            double xPowerNormalized = xPower/powerHypot;
            double yPowerNormalized = yPower/powerHypot;



            if (yPassed && xPassed && Math.abs(angleDeviation) < 1){
                break;
            }

            driveByJoystickWithRelationToAxis(xPower, yPower, rotatePower);

            double currEncoder = (abs(backLeft.getCurrentPosition())
                    + abs(backRight.getCurrentPosition())
                    + abs(frontLeft.getCurrentPosition())
                    + abs(frontRight.getCurrentPosition()))/4.;
            double currentDistanceTraveled = currEncoder / COUNTS_PER_MOTOR_REV * (2.0 * Math.PI * WHEEL_RADIUS_CM);
            double distanceTraveledNow = currentDistanceTraveled - lastDistanceTravelled;
            lastDistanceTravelled = currentDistanceTraveled;
            currentX += xPowerNormalized *  distanceTraveledNow;
            currentY += yPowerNormalized * distanceTraveledNow;

            opMode.telemetry.addData("angleDeviation: ", angleDeviation);
            opMode.telemetry.addData("rotatePower: ", rotatePower);
            opMode.telemetry.addData("currentX: ", currentX);
            opMode.telemetry.addData("currentY: ", currentY);
            opMode.telemetry.addData("xDiff: ", xDiff);
            opMode.telemetry.addData("yDiff: ", yDiff);
            opMode.telemetry.addData("powerX: ", xPower);
            opMode.telemetry.addData("powerY: ", yPower);
            opMode.telemetry.addData("xPassed: ", xPassed);
            opMode.telemetry.addData("yPassed: ", yPassed);
            opMode.telemetry.addData("currentDistance: ", currentDistanceTraveled);
            opMode.telemetry.update();
        }
        stop();
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

        // Rotate until the desired angle is met within an error of 0.5 degrees
        while (abs(theta) > 0.5) {
            theta = getAngleDeviation();
            opMode.telemetry.addData("angleDeviation", theta);
            opMode.telemetry.update();
            double direction = (theta / abs(theta));
            driveByJoystick(0, 0,
                    max(abs(theta / speedDecrease), 0.5)
                            * direction
            );
        }
        stop();
    }

    /**
     * Same as drive straight, but has a default stopAfter of true. robot will stop after driving.
     */
    public void driveStraight(double distance, double power) {
        driveStraight(distance, power, true);
    }

    /**
    * @param maxDistance max distance until robot stops
    * @param power driving power
    * @return distance traveled
    */
    public double[] driveUntilCollect(double maxDistance, double power){
        armSystem.collect();

        resetDistance();
        double averageMotors = 0;
        double distanceLeft = 0;

        final double bumpingThreshold = abs(power * ACCELERATION_BUMPING_THRESHOLD);

        while (maxDistance * COUNTS_PER_MOTOR_REV / (2.0 * Math.PI * WHEEL_RADIUS_CM) > averageMotors) {
            averageMotors = abs(
                    (this.frontRight.getCurrentPosition() + this.frontLeft.getCurrentPosition()
                            + this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()
                    ) / 4.0
            );

            double angleDeviation = getAngleDeviation();
            driveByJoystick(0, -power, angleDeviation / ROTATE_SPEED_DECREASE);
            if(armSystem.touch.isPressed()){
                armSystem.stop();
                return new double[]{(2.0 * Math.PI * WHEEL_RADIUS_CM) * averageMotors / COUNTS_PER_MOTOR_REV,distanceLeft};
            }

        }

        stop();
        return new double[]{maxDistance, distanceLeft};
    }

    public void driveUntilWhite(double power ,boolean stopAfter){
        driveByJoystick(0,-power,0);
        while(!colorSystem.overWhite()){
        }
        if(stopAfter)
            stop();
    }

    public double combinedDriveUntilCollect(double maxDistance, double power){
        resetDistance();
        armSystem.collect();
        double bumpingThreshold = abs(power) * ACCELERATION_BUMPING_THRESHOLD;
        while (true) {
            double averageMotors = abs(
                    (frontRight.getCurrentPosition() + frontLeft.getCurrentPosition()
                            + backLeft.getCurrentPosition() + backRight.getCurrentPosition()
                    ) / 4.0
            );
            double distanceTraveled = (2.0 * Math.PI * WHEEL_RADIUS_CM) * averageMotors / COUNTS_PER_MOTOR_REV;
            driveByJoystick(0, -power, getAngleDeviation() / ROTATE_SPEED_DECREASE);
            if (armSystem.touch.isPressed() || distanceTraveled > maxDistance){
                stop();
                armSystem.stop();
                return distanceTraveled;
            }else if (getAccelerationMagnitude() > bumpingThreshold && distanceTraveled > 15){
                opMode.telemetry.addLine("Passed Acceleration bumping Threshold");
                opMode.telemetry.update();
                wobbleDriveUntilCollect(100, power);
                stop();
                armSystem.stop();
                return distanceTraveled;
            }
        }
    }

    public double wobbleDriveUntilCollect(double maxDistance, double power){
        final double WOBBLE_AMPLITUDE = 20;
        final double WOBBLE_PERIOD = 0.4;
        final double WOBBLE_ANG_FREQ = 2*Math.PI/WOBBLE_PERIOD;
        final double WOBBLE_DISTANCE_FACTOR = 1.3;
        ElapsedTime elapsedTime = new ElapsedTime();
        armSystem.collect();
        resetDistance();
        while (true){
            double averageMotors = abs(
                    (frontRight.getCurrentPosition() + frontLeft.getCurrentPosition()
                            + backLeft.getCurrentPosition() + backRight.getCurrentPosition()
                    ) / 4.0
            );
            double distanceTraveled = (2.0 * Math.PI * WHEEL_RADIUS_CM) * averageMotors / COUNTS_PER_MOTOR_REV * WOBBLE_DISTANCE_FACTOR;
            double currentTargetAngle = targetAngle + WOBBLE_AMPLITUDE * Math.sin(elapsedTime.seconds() * WOBBLE_ANG_FREQ);
            double angleDeviation = normalizeAngle(getCurrentAngle() - currentTargetAngle);
            driveByJoystick(0, -power, angleDeviation / ROTATE_SPEED_DECREASE);

            if (distanceTraveled > maxDistance || armSystem.touch.isPressed()){
                stop();
                armSystem.stop();
                return distanceTraveled;
            }
        }
    }

    /**
     * The method we use to travel a set distance forwards or backwards.
     *
     * @param distance The distance to be travelled.
     * @param power    The power of the driving motors (positive = forward).
     */
    public void driveStraight(double distance, double power, boolean stopAfter) {
        // The method receives a positive distance.
        if (distance < 0) {
            throw new IllegalArgumentException("Method driveStraight was given a negative distance: " + distance);
        }

        resetDistance();
        /*
         * The average distance the motors have travelled (in ticks).
         * Basically means how far the robot has travelled.
         */
        double averageMotors = 0;

        // Go until the desired distance is reached
        while (distance * COUNTS_PER_MOTOR_REV / (2.0 * Math.PI * WHEEL_RADIUS_CM) > averageMotors) {
            // x2 is used to fix the natural deviation of the robot from a straight line due to friction
            double angleDeviation = getAngleDeviation();
            driveByJoystick(0, -power, angleDeviation / ROTATE_SPEED_DECREASE);
            averageMotors = abs(
                    (this.frontRight.getCurrentPosition() + this.frontLeft.getCurrentPosition()
                            + this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()
                    ) / 4.0
            );
        }
        if(stopAfter) {
            stop();
        }
    }

    public void driveStraightUntilBumping(double power, double initialDistance){
        driveStraight(initialDistance, power, false);
        final double bumpingThreshold = abs(power * ACCELERATION_BUMPING_THRESHOLD);
        while (getAccelerationMagnitude() < bumpingThreshold) {
            // x2 is used to fix the natural deviation of the robot from a straight line due to friction
            double angleDeviation = getAngleDeviation();
            driveByJoystick(0, -power, angleDeviation / ROTATE_SPEED_DECREASE);
        }
        stop();
    }
    public void driveSidewaysUntilBumping(double power, double initialDistance){
        driveSideways(initialDistance, power, false);
        final double bumpingThreshold = abs(power * ACCELERATION_BUMPING_THRESHOLD);
        while (getAccelerationMagnitude() < bumpingThreshold) {
            // x2 is used to fix the natural deviation of the robot from a straight line due to friction
            double angleDeviation = getAngleDeviation();
            driveByJoystick(power, 0, angleDeviation / ROTATE_SPEED_DECREASE);
        }
        stop();
    }


    /**
     * The method we use to travel a set distance rightwards or leftwards.
     *  @param distance The distance to be travelled.
     * @param power    The power of the driving motors (positive = rightward).
     */
    public void driveSideways(double distance, double power) {
        driveSideways(distance, power, true);
    }

    /**
     * The method we use to travel a set distance rightwards or leftwards.
     *
     * @param distance The distance to be travelled.
     * @param power    The power of the driving motors (positive = rightward).
     * @param stopAfter if true, the robot stops after moving
     */
    public void driveSideways(double distance, double power, boolean stopAfter) {
        // The method receives a positive distance.
        if (distance < 0) {
            throw new IllegalArgumentException("Method driveSideways was given a negative distance: " + distance);
        }

        resetDistance();
        /*
         * The average distance the motors have travelled (in ticks).
         * Basically means how far the robot has travelled.
         */
        double averageMotors = 0;

        // Go until the desired distance is reached
        while ((distance * COUNTS_PER_MOTOR_REV) / (2.0 * Math.PI * WHEEL_RADIUS_CM) > averageMotors) {
            // x2 is used to fix the natural deviation of the robot from a straight line
            double angleDeviation = getAngleDeviation();
            opMode.telemetry.addData("angleDeviation", angleDeviation);
            opMode.telemetry.update();
            driveByJoystick(power, 0, angleDeviation / ROTATE_SPEED_DECREASE);
            averageMotors = abs(
                    (this.frontRight.getCurrentPosition() - this.frontLeft.getCurrentPosition()
                            + this.backLeft.getCurrentPosition() - this.backRight.getCurrentPosition()
                    ) / 4.0
            );
        }
        if (stopAfter) {
            stop();
        }
    }

    /**
     * Drives sideways, and repeatedly calls the DetectionSystem.scan() method.
     */
    public void driveSidewaysAndScan(double distance, double power, DetectionSystem detectionSystem) {
        this.targetAngle = getCurrentAngle();
        // The method receives a positive distance.
        if (distance < 0) {
            throw new IllegalArgumentException("Method driveSidewaysAndScan was given a negative distance: " + distance);
        }

        resetDistance();
        /*
         * The average distance the motors have travelled (in ticks).
         * Basically means how far the robot has travelled.
         */
        double averageMotors = 0;

        while ((abs(distance) * COUNTS_PER_MOTOR_REV) / (2.0 * Math.PI * WHEEL_RADIUS_CM) > averageMotors) {
            driveByJoystick(power, 0, getAngleDeviation() / ROTATE_SPEED_DECREASE);
            averageMotors = abs(
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
            driveByJoystick(0, -power, getAngleDeviation() / ROTATE_SPEED_DECREASE);
        }
        stop();
    }

    /**
     * Same as driveUntilObstacle, but also lifts the arm so that it doesn't block the sensors.
     */
    @Deprecated
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
        /*
         * The average distance the motors have travelled (in ticks).
         * Basically means how far the robot has travelled.
         */
        double averageMotors = 0;

        // Run until the stop condition is met
        while (!stopCondition.shouldStop()) {
            driveByJoystick(power, 0, getAngleDeviation() / 40);
            averageMotors = abs(
                    (-this.frontRight.getCurrentPosition() - this.frontLeft.getCurrentPosition()
                            + this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()
                    ) / 4.0
            );
        }
        stop();
        return abs(averageMotors / COUNTS_PER_MOTOR_REV * (2.0 * Math.PI * WHEEL_RADIUS_CM));
    }

    /**
     * Drives straight until the predicate of stopCondition returns true.
     *
     * @return the total distance traveled.
     */
    public double driveStraightUntil(double power, StopCondition stopCondition) {
        targetAngle = getCurrentAngle();

        resetDistance();
        /*
         * The average distance the motors have travelled (in ticks).
         * Basically means how far the robot has travelled.
         */
        double averageMotors = 0;

        // Run until the stop condition is met
        while (!stopCondition.shouldStop()) {
            driveByJoystick(0, -power, getAngleDeviation() / 40);
            averageMotors = abs(
                    (this.frontRight.getCurrentPosition() - this.frontLeft.getCurrentPosition()
                            - this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()
                    ) / 4.0
            );
        }
        stop();
        return abs(averageMotors / COUNTS_PER_MOTOR_REV * (2.0 * Math.PI * WHEEL_RADIUS_CM));
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

        // Run for [duration] seconds
        while (elapsedTime.seconds() < duration) {
            double distance = distanceSensor.getDistance(DistanceUnit.CM);
            if (distance < 50) {
                distances.add(distance);
            }
        }

        return MathUtils.average(distances);
    }

    /**
     * Goes to a fixed distance from an object, given the current distance from said object.
     * If too far, go forward, and if too close go backward.
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
        // Go right until the Shipping Hubs' stem is detected
        while (sensorBackUp.getDistance(DistanceUnit.CM) > 50 || sensorBackDown.getDistance(DistanceUnit.CM) > 50) {
            driveByJoystick(-0.4, 0, 0);
        }
        stop();

        double avgDistance = distanceSensorAverage(2, sensorBackDown);
        driveToFixedDistance(avgDistance, 35, 0.3);

        while (sensorBackDown.getDistance(DistanceUnit.CM) < 40) {
            driveByJoystick(0, 0, 0.2);
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