package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import static org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils.normalizeAngle;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.copySign;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.StopCondition;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class DrivingSystem {

    int count = 0;
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
        imu.write8(BNO055IMU.Register.OPR_MODE, BNO055IMU.SensorMode.CONFIG.bVal & 0x0F);
        TimeUtils.sleep(100); //Changing modes requires a delay before doing anything else
        //Write to the AXIS_MAP_CONFIG register
        imu.write8(BNO055IMU.Register.AXIS_MAP_CONFIG, AXIS_MAP_CONFIG_BYTE & 0x0F);
        //Write to the AXIS_MAP_SIGN register
        imu.write8(BNO055IMU.Register.AXIS_MAP_SIGN, AXIS_MAP_SIGN_BYTE & 0x0F);
        //Need to change back into the IMU mode to use the gyro
        imu.write8(BNO055IMU.Register.OPR_MODE, BNO055IMU.SensorMode.IMU.bVal & 0x0F);
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
        double drivePower = Math.hypot(x1, y1);
        double angleAdjustment = toDegrees(atan2(x2, drivePower));
        double angle = getCurrentAngle() - 0.3 * angleAdjustment;

        opMode.telemetry.addData("angleAdjustment", angleAdjustment);
        opMode.telemetry.update();


        driveByJoystick(1.16 * (sin((90 - angle) * Math.PI / 180) * x1 + sin(angle * Math.PI / 180) * y1),
                -cos(angle * Math.PI / 180) * y1 + cos((90 - angle) * Math.PI / 180) * x1,
                x2);
    }


    public void rotateAroundArm(double power) {
        double factor = 3;
        backLeft.setPower(power * (0.1 + 1 / factor / 2));
        backRight.setPower(power * (0.1 - 1 / factor));
        frontLeft.setPower(power * (0.1 - 1 / factor / 8.5));
        frontRight.setPower(power * (0.1 + 1 / factor / 8.5));
    }

    public void rotateAroundDucks(double power, boolean red) {
        double factor = 3;
        double forward = 0.3 * Math.abs(power);
        if (red) {
            backRight.setPower(-forward + power * (1.5 / factor));
            backLeft.setPower(forward - power * (1 / factor));
            frontLeft.setPower(forward - power * (0.5 / factor));
            frontRight.setPower(forward);
        } else {
            backLeft.setPower(-forward + power * (1.5 / factor));
            backRight.setPower(forward - power * (1 / factor));
            frontRight.setPower(forward - power * (0.5 / factor));
            frontLeft.setPower(forward);
        }
    }

    public void rotateAroundDuck2(double power, boolean red) {
        double factor = 2;
        double push = 0.2 * Math.abs(power);
        double dirY = 0.5;
        double dirX;

        if (red)
            dirX = 0.5;
        else
            dirX = -0.5;

        double x1 = 1.16 * push * (
                sin((90 - getCurrentAngle()) * Math.PI / 180) * dirX
                        + sin(getCurrentAngle() * Math.PI / 180) * dirY
        );
        double y1 = push * (
                -cos(getCurrentAngle() * Math.PI / 180) * dirY
                        + cos((90 - getCurrentAngle()) * Math.PI / 180) * dirX
        );

        double frontRightPower;
        double frontLeftPower;
        double backRightPower;
        double backLeftPower;

        if (red) {
            frontRightPower = -y1 - x1;
            frontLeftPower = -y1 + x1 - power * (0.5 / factor);
            backRightPower = -y1 + x1 + power * (1 / factor);
            backLeftPower = -y1 - x1 - power * (1 / factor);
        } else {
            frontRightPower = -y1 - x1 - power * (0.5 / factor);
            frontLeftPower = -y1 + x1;
            backRightPower = -y1 + x1 - power * (1 / factor);
            backLeftPower = -y1 - x1 + power * (1 / factor);
        }

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

    public void driveToPoint(double targetX, double targetY, double ang, double driveSpeed, double rotateSpeed) {
        driveToPoint(targetX, targetY, ang, driveSpeed, rotateSpeed, false);
    }

    public void driveToPoint(double targetX, double targetY, double ang, double driveSpeed, double rotateSpeed, boolean stopIfBump) {
        resetDistance();

        final double slowDownArea = 0.4;

        double currentX = 0;
        double currentY = 0;
        this.targetAngle = -ang;

        final double ROTATE_SPEED_MIN = 0.2;
        final double MIN_MOVE_SPEED = 0.2;
        double lastDistanceTravelled = 0;
        while (opMode.opModeIsActive()) {
            double angleDeviation = getAngleDeviation();
            double xDiff = targetX - currentX;
            double yDiff = targetY - currentY;
            boolean xPassed = abs(currentX) >= abs(targetX);
            boolean yPassed = abs(currentY) >= abs(targetY);

            double xPower, yPower, rotatePower;
            if (Math.abs(angleDeviation) < 0.5) {
                rotatePower = 0;
            } else {
                rotatePower = copySign(
                        min(abs(angleDeviation) / 180 * (abs(rotateSpeed) - ROTATE_SPEED_MIN) + ROTATE_SPEED_MIN, 1),
                        angleDeviation);
            }
            double maxDiff = max(abs(xDiff), abs(yDiff));
            if (xPassed || maxDiff == 0) {
                xPower = 0;
            } else if (Math.abs(xDiff / targetX) < slowDownArea) {
                xPower = Math.max(Math.abs(xDiff / targetX) / slowDownArea * driveSpeed, MIN_MOVE_SPEED) * xDiff / Math.abs(xDiff);
            } else {
                xPower = driveSpeed * xDiff / Math.abs(xDiff);
            }

            if (yPassed || maxDiff == 0) {
                yPower = 0;
            } else if (Math.abs(yDiff / targetY) < slowDownArea) {
                yPower = Math.max(Math.abs(yDiff / targetY) / slowDownArea * driveSpeed, MIN_MOVE_SPEED) * yDiff / Math.abs(yDiff);
            } else {
                yPower = driveSpeed * yDiff / Math.abs(yDiff);
            }
            // normalize so that xPower^2 + yPower^2 + rotatePower^2 = 1
            double powerHypot = sqrt(pow(xPower, 2) + pow(yPower, 2) + pow(rotatePower, 2));
            double xPowerNormalized = xPower / powerHypot;
            double yPowerNormalized = yPower / powerHypot;

            final double bumpingThreshold = abs(driveSpeed * ACCELERATION_BUMPING_THRESHOLD);

            if (getAccelerationMagnitude() > bumpingThreshold) {
                opMode.telemetry.addLine("bump");
                opMode.telemetry.update();
            }

            if ((yPassed && xPassed && Math.abs(angleDeviation) < 0.5) || (getAccelerationMagnitude() > bumpingThreshold && stopIfBump)) {
                break;
            }

            driveByJoystickWithRelationToAxis(xPower, yPower, rotatePower);

            double currEncoder = (abs(backLeft.getCurrentPosition())
                    + abs(backRight.getCurrentPosition())
                    + abs(frontLeft.getCurrentPosition())
                    + abs(frontRight.getCurrentPosition())) / 4.;
            double currentDistanceTraveled = currEncoder / COUNTS_PER_MOTOR_REV * (2.0 * Math.PI * WHEEL_RADIUS_CM);
            double distanceTraveledNow = (currentDistanceTraveled - lastDistanceTravelled);
            lastDistanceTravelled = currentDistanceTraveled;
            currentX += xPowerNormalized * distanceTraveledNow;
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
        while (abs(theta) > 0.5 && opMode.opModeIsActive()) {
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

    public void turnAbsolute(float deg, int speedDecrease) {
        this.targetAngle = normalizeAngle(deg);
        double theta = getAngleDeviation();

        // Rotate until the desired angle is met within an error of 0.5 degrees
        while (abs(theta) > 0.5 && opMode.opModeIsActive()) {
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
     * @param maxDistance max distance until robot stops
     * @param power       driving power
     * @return distance traveled
     */
    public double driveUntilCollect(double maxDistance, double power) {
        armSystem.collect();
        armSystem.arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armSystem.arm.setPower(0.1);

        resetDistance();
        double averageMotors = 0;

        while ((maxDistance * COUNTS_PER_MOTOR_REV / (2.0 * Math.PI * WHEEL_RADIUS_CM) > averageMotors) && opMode.opModeIsActive()) {
            averageMotors = abs(
                    (this.frontRight.getCurrentPosition() + this.frontLeft.getCurrentPosition()
                            + this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()
                    ) / 4.0
            );

            double angleDeviation = getAngleDeviation();
            driveByJoystick(0, -power, angleDeviation / ROTATE_SPEED_DECREASE);
            armSystem.stayDownOnLoad();
            if (armSystem.touch.isPressed()) {
//                int driveStraightDistance = 5;
//                driveStraight(driveStraightDistance, power);
                armSystem.stop();
                return (2.0 * Math.PI * WHEEL_RADIUS_CM) * averageMotors / COUNTS_PER_MOTOR_REV;
            }
        }

        stop();
        armSystem.arm.setPower(0);
        return maxDistance;
    }

    public void driveUntilWhite(double power, double maxDistance,boolean stopAfter) {
        double averageMotors = 0;
        while (!colorSystem.overWhite() && opMode.opModeIsActive() && (maxDistance * COUNTS_PER_MOTOR_REV / (2.0 * Math.PI * WHEEL_RADIUS_CM) > averageMotors)) {
            double angleDeviation = getAngleDeviation();
            averageMotors = abs(
                    (this.frontRight.getCurrentPosition() + this.frontLeft.getCurrentPosition()
                            + this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()
                    ) / 4.0
            );
            driveByJoystick(0, -power, angleDeviation / ROTATE_SPEED_DECREASE);
        }
        if (stopAfter)
            stop();
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
            distance = -distance;
            power = -power;
        }

        resetDistance();
        /*
         * The average distance the motors have travelled (in ticks).
         * Basically means how far the robot has travelled.
         */
        double averageMotors = 0;

        // Go until the desired distance is reached
        while (distance * COUNTS_PER_MOTOR_REV / (2.0 * Math.PI * WHEEL_RADIUS_CM) > averageMotors && opMode.opModeIsActive()) {
            // x2 is used to fix the natural deviation of the robot from a straight line due to friction
            double angleDeviation = getAngleDeviation();
            driveByJoystick(0, -power, angleDeviation / ROTATE_SPEED_DECREASE);
            averageMotors = abs(
                    (this.frontRight.getCurrentPosition() + this.frontLeft.getCurrentPosition()
                            + this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()
                    ) / 4.0
            );
        }
        if (stopAfter) {
            stop();
        }
    }

    /**
     * Same as drive straight, but has a default stopAfter of true. robot will stop after driving.
     */
    public void driveStraight(double distance, double power) {
        driveStraight(distance, power, true);
    }

    public void driveStraightUntilBumping(double power, double initialDistance) {
        driveStraight(initialDistance, power, false);
        final double bumpingThreshold = abs(power * ACCELERATION_BUMPING_THRESHOLD);
        while (getAccelerationMagnitude() < bumpingThreshold && opMode.opModeIsActive()) {
            // x2 is used to fix the natural deviation of the robot from a straight line due to friction
            double angleDeviation = getAngleDeviation();
            driveByJoystick(0, -power, angleDeviation / ROTATE_SPEED_DECREASE);
        }
        stop();
    }

    public void driveSidewaysUntilBumping(double power, double initialDistance) {
        driveSideways(initialDistance, power, false);
        final double bumpingThreshold = abs(power * ACCELERATION_BUMPING_THRESHOLD);
        while (getAccelerationMagnitude() < bumpingThreshold && opMode.opModeIsActive()) {
            // x2 is used to fix the natural deviation of the robot from a straight line due to friction
            double angleDeviation = getAngleDeviation();
            driveByJoystick(power, 0, angleDeviation / ROTATE_SPEED_DECREASE);
        }
        stop();
    }

    /**
     * The method we use to travel a set distance rightwards or leftwards.
     *
     * @param distance  The distance to be travelled.
     * @param power     The power of the driving motors (positive = rightward).
     * @param stopAfter if true, the robot stops after moving
     */
    public void driveSideways(double distance, double power, boolean stopAfter) {
        // The method receives a positive distance.
        if (distance < 0) {
            distance = -distance;
            power = -power;
        }

        resetDistance();
        /*
         * The average distance the motors have travelled (in ticks).
         * Basically means how far the robot has travelled.
         */
        double averageMotors = 0;

        // Go until the desired distance is reached
        while ((distance * COUNTS_PER_MOTOR_REV) / (2.0 * Math.PI * WHEEL_RADIUS_CM) > averageMotors && opMode.opModeIsActive()) {
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
     * The method we use to travel a set distance rightwards or leftwards.
     *
     * @param distance The distance to be travelled.
     * @param power    The power of the driving motors (positive = rightward).
     */
    public void driveSideways(double distance, double power) {
        driveSideways(distance, power, true);
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
        while (!stopCondition.shouldStop() && opMode.opModeIsActive()) {
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
        while (!stopCondition.shouldStop() && opMode.opModeIsActive()) {
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