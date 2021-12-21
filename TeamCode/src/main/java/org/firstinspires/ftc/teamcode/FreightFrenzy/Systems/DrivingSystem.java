package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import static org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils.normalizeAngle;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;

public class DrivingSystem {
    private final DcMotor      frontRight;
    private final DcMotor      frontLeft;
    private final DcMotor      backRight;
    private final DcMotor      backLeft;
    private final LinearOpMode opMode;

    private final BNO055IMU imu;

    private double targetAngle = 0;

    static final        double COUNTS_PER_MOTOR_REV = 515;    // eg: GoBILDA Motor Encoder
    static final        double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    static final        double WHEEL_DIAMETER_MM    = 50;     // For figuring circumference
    static final        double COUNTS_PER_mm        = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_MM * 3.1415);
    public static final double WHEEL_RADIUS_CM      = 4.8;

    public DrivingSystem(LinearOpMode opMode) {
        this.frontRight = opMode.hardwareMap.get(DcMotor.class, "front_right");
        this.frontLeft  = opMode.hardwareMap.get(DcMotor.class, "front_left");
        this.backRight  = opMode.hardwareMap.get(DcMotor.class, "back_right");
        this.backLeft   = opMode.hardwareMap.get(DcMotor.class, "back_left");
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
        double frontRightPower = x1 - y1 - x2;
        double frontLeftPower = y1 + x1 - x2;
        double backRightPower = -y1 - x1 - x2;
        double backLeftPower = y1 - x1 - x2;

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

    public void rotateInPlace(double rotationDegrees, double maxSpeed, double minSpeed, boolean mock) {
        final int TIMES_TO_PASS_TARGET = 4;

        targetAngle = normalizeAngle(getCurrentAngle() + rotationDegrees);
        this.opMode.telemetry.addData("targetAngle", targetAngle);
        this.opMode.telemetry.addData("isRotatingInPlace", true);
        this.opMode.telemetry.update();

        boolean rotatingClockwise = getAngleDeviation() < 0;
        int numTimesPastTarget = 0;
        while (numTimesPastTarget < TIMES_TO_PASS_TARGET) {
            double currentAngle = getCurrentAngle();
            double angleDeviation = getAngleDeviation();
            double motorPower = -angleDeviation / 180 * (maxSpeed - minSpeed) + minSpeed;
            if (mock) {
                driveByJoystick(0, 0, 0);
            } else {
                driveByJoystick(0, 0, motorPower);
            }
            if (angleDeviation > 0 && rotatingClockwise) {
                numTimesPastTarget++;
                rotatingClockwise = false;
            } else if (angleDeviation < 0 && !rotatingClockwise) {
                numTimesPastTarget++;
                rotatingClockwise = true;
            }
            this.opMode.telemetry.addData("currentAngle", currentAngle);
            this.opMode.telemetry.addData("motorPower", motorPower);
            this.opMode.telemetry.addData("angleDeviation", angleDeviation);
            this.opMode.telemetry.addData("numTimesPastTarget", numTimesPastTarget);
            this.opMode.telemetry.update();

        }
        stöp();
        this.opMode.telemetry.addData("isRotatingInPlace", false);
        this.opMode.telemetry.update();

    }

    public void turn(float deg, int speedDecrease) {
        targetAngle = normalizeAngle(getCurrentAngle() + deg);
        double d = getAngleDeviation();
        while (Math.abs(d) > 0.5) {
            d = getAngleDeviation();
            double direction = (getAngleDeviation() / Math.abs(getAngleDeviation()));
            driveByJoystick(0, 0, Math.max(Math.abs(getAngleDeviation() / speedDecrease), 0.1) * direction);
            this.opMode.telemetry.addData("speed:", d);
        }
        stöp();
    }

    public void driveStraight(double distance, double power) {
        targetAngle = getCurrentAngle();
        resetDistance();
        double AverageMotors = 0;
        this.opMode.telemetry.addData("distance", AverageMotors);
        while ((Math.abs(distance) * COUNTS_PER_MOTOR_REV) / (2.0 * Math.PI * WHEEL_RADIUS_CM) > AverageMotors) {
            driveByJoystick(-getAngleDeviation() / 40, power, 0);
            AverageMotors = (this.frontRight.getCurrentPosition() - this.frontLeft.getCurrentPosition() - this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition()) / 4.0;
            AverageMotors = Math.abs(AverageMotors);
            this.opMode.telemetry.addData("distance", AverageMotors);
            this.opMode.telemetry.update();
        }
        stöp();
    }

    public void smartDriveStraight(double distance, double power) {
        // not done
        resetDistance();
        double startAngle = getCurrentAngle();
        double targetX = distance * MathUtils.cosDegrees(startAngle);
        double targetY = distance * MathUtils.sinDegrees(startAngle);

        double currentX = 0;
        double currentY = 0;

        double prevMotorDistanceTraveled = 0;

        while (Math.hypot(currentX, currentY) < distance) {
            double currentAngle = getCurrentAngle();
            double motorDistanceTraveled = motorDistanceTraveled();
            double motorDistanceTraveledInTick = motorDistanceTraveled - prevMotorDistanceTraveled;
            currentX +=
            prevMotorDistanceTraveled = motorDistanceTraveled;
        }
    }

    public void driveSideways(double distance, double power) {
        resetDistance();
        final double WHEEL_Radius_CM = 4.8;
        double AverageMotors = (this.frontRight.getCurrentPosition() - this.frontLeft.getCurrentPosition() + this.backLeft.getCurrentPosition() - this.backRight.getCurrentPosition()) / 4.0;
        while ((Math.abs(distance) * COUNTS_PER_MOTOR_REV) / (2 * Math.PI * WHEEL_Radius_CM) > AverageMotors) {
            driveByJoystick(power, 0, 0);
            AverageMotors = (this.frontRight.getCurrentPosition() - this.frontLeft.getCurrentPosition() + this.backLeft.getCurrentPosition() - this.backRight.getCurrentPosition()) / 4.0;
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