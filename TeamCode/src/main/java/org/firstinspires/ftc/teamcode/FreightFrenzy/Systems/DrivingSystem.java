package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import static org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils.normalizeAngle;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.StopCondition;

public class DrivingSystem {
    private final DcMotor      frontRight;
    private final DcMotor      frontLeft;
    private final DcMotor      backRight;
    private final DcMotor      backLeft;
    private final DistanceSensor distanceSensor;
    private final LinearOpMode opMode;

    private final BNO055IMU imu;

    private double targetAngle = 0;

    private static final double COUNTS_PER_MOTOR_REV = 515;    // eg: GoBILDA Motor Encoder
    private static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    private static final double WHEEL_DIAMETER_MM    = 50;     // For figuring circumference
    private static final double COUNTS_PER_mm        = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_MM * 3.1415);
    private static final double WHEEL_RADIUS_CM      = 4.8;

    public DrivingSystem(LinearOpMode opMode) {
        this.frontRight = opMode.hardwareMap.get(DcMotor.class, "front_right");
        this.frontLeft  = opMode.hardwareMap.get(DcMotor.class, "front_left");
        this.backRight  = opMode.hardwareMap.get(DcMotor.class, "back_right");
        this.backLeft   = opMode.hardwareMap.get(DcMotor.class, "back_left");
        this.distanceSensor = opMode.hardwareMap.get(DistanceSensor.class, "distance_sensor_left");
        this.opMode     = opMode;

        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        stöp();
        resetDistance();

        // Create IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit                        = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit                        = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile              = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled                   = true;
        parameters.loggingTag                       = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = opMode.hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    public double getCurrentAngle() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
    }

    private double getAngleDeviation() {
        return normalizeAngle(getCurrentAngle() - targetAngle);
    }

    private double motorDistanceTraveled() {
        return (this.frontRight.getCurrentPosition() - this.frontLeft.getCurrentPosition() - this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()) / 4.0;
    }

    public void driveByJoystick(double x1, double y1,
                                double x2) {
        double frontRightPower = -x1 - y1 - 0.7 * x2;
        double frontLeftPower = y1 - x1 - 0.7 * x2;
        double backRightPower = -y1 + x1 - 0.7 * x2;
        double backLeftPower = y1 + x1 - 0.7 * x2;

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

    public void turn(float deg, int speedDecrease) {
        targetAngle = normalizeAngle(targetAngle + deg);
        double d = getAngleDeviation();
        while (Math.abs(d) > 0.5) {
            d = getAngleDeviation();
            double direction = (d / Math.abs(d));
            driveByJoystick(0, 0,
                    Math.max(Math.abs(d / speedDecrease),
                            0.07) * direction);
//            this.opMode.telemetry.addData("speed:", d);
        }
        stöp();
    }

    public void driveStraight(double distance, double power) {
        power *= -1;
        resetDistance();
        double AverageMotors = 0;
//        this.opMode.telemetry.addData("distance", AverageMotors);
        while ((Math.abs(distance) * COUNTS_PER_MOTOR_REV) / (2.0 * Math.PI * WHEEL_RADIUS_CM) > AverageMotors) {
            driveByJoystick(0, power, getAngleDeviation() / 40);
            AverageMotors = (this.frontRight.getCurrentPosition() - this.frontLeft.getCurrentPosition() - this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()) / 4.0;
            AverageMotors = Math.abs(AverageMotors);
//            this.opMode.telemetry.addData("distance", AverageMotors);
//            this.opMode.telemetry.update();
        }
        stöp();
    }


    public void driveSideways(double distance, double power) {
        resetDistance();
        double AverageMotors = 0;
//        this.opMode.telemetry.addData("distance", AverageMotors);
        while ((Math.abs(distance) * COUNTS_PER_MOTOR_REV) / (2.0 * Math.PI * WHEEL_RADIUS_CM) > AverageMotors) {
            driveByJoystick(power, 0, getAngleDeviation() / 40);
            AverageMotors = (-this.frontRight.getCurrentPosition() - this.frontLeft.getCurrentPosition() + this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()) / 4.0;
            AverageMotors = Math.abs(AverageMotors);
//            this.opMode.telemetry.addData("distance", AverageMotors);
//            this.opMode.telemetry.update();
        }
        stöp();
    }

    /**
     * Drives Sideways until the predicate of stopCondition returns true.
     *  @return the total distance traveled.
     */
    public double driveSidewaysUntil(double power, StopCondition stopCondition){
        resetDistance();
        double averageMotors = 0;
        this.opMode.telemetry.addData("distance", averageMotors);
        while (!stopCondition.shouldStop()) {
            driveByJoystick(power, 0, getAngleDeviation() / 40);
            averageMotors = (-this.frontRight.getCurrentPosition() - this.frontLeft.getCurrentPosition() + this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()) / 4.0;
            averageMotors = Math.abs(averageMotors);
            this.opMode.telemetry.addData("distance", averageMotors);
            this.opMode.telemetry.update();
        }
        stöp();
        // (Math.abs(distance) * COUNTS_PER_MOTOR_REV) / (2.0 * Math.PI * WHEEL_RADIUS_CM) = averageMotors
        return Math.abs(averageMotors /COUNTS_PER_MOTOR_REV*(2.0 * Math.PI * WHEEL_RADIUS_CM));
    }

    public double driveStraightUntil(double power, StopCondition stopCondition) {
        power *= -1;
        resetDistance();
        double averageMotors = 0;
        this.opMode.telemetry.addData("distance", averageMotors);
        while (!stopCondition.shouldStop()) {
            driveByJoystick(0, power, getAngleDeviation() / 40);
            averageMotors = (this.frontRight.getCurrentPosition() - this.frontLeft.getCurrentPosition() - this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()) / 4.0;
            averageMotors = Math.abs(averageMotors);
            this.opMode.telemetry.addData("distance", averageMotors);
            this.opMode.telemetry.update();
        }
        stöp();
        return Math.abs(averageMotors /COUNTS_PER_MOTOR_REV*(2.0 * Math.PI * WHEEL_RADIUS_CM));
    }


    /**
     * Drives sideways, and repeatedly calls the DetectionSystem.scan() method.
     */
    public void driveSidewaysAndScan(double distance, double power, DetectionSystem detectionSystem) {
        resetDistance();
        double AverageMotors = 0;
        this.opMode.telemetry.addData("distance", AverageMotors);
        while ((Math.abs(distance) * COUNTS_PER_MOTOR_REV) / (2.0 * Math.PI * WHEEL_RADIUS_CM) > AverageMotors) {
            driveByJoystick(power, 0, getAngleDeviation() / 40);
            AverageMotors = (-this.frontRight.getCurrentPosition() - this.frontLeft.getCurrentPosition() + this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()) / 4.0;
            AverageMotors = Math.abs(AverageMotors);
            detectionSystem.scan();
            this.opMode.telemetry.addData("distance", AverageMotors);
            this.opMode.telemetry.update();
        }
        stöp();
    }


    public void driveUntilObstacle(double distance, double power) {
        power *= -1;
        resetDistance();
        while (distanceSensor.getDistance(DistanceUnit.CM) > distance) {
            driveByJoystick(0, power, getAngleDeviation() / 40);
            this.opMode.telemetry.addData("distance", distanceSensor.getDistance(DistanceUnit.CM));
            this.opMode.telemetry.update();
        }
        stöp();
    }


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

    public void stöp() {
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
    }
}