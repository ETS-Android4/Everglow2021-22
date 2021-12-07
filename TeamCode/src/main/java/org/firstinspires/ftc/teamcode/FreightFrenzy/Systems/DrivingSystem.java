package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class DrivingSystem {
    private final DcMotor frontRight;
    private final DcMotor frontLeft;
    private final DcMotor backRight;
    private final DcMotor backLeft;

    private final BNO055IMU imu;

    private double targetAngle = 0;

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_MM   = 50 ;     // For figuring circumference
    static final double     COUNTS_PER_mm         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_MM * 3.1415);

    public DrivingSystem(LinearOpMode opMode) {
        this.frontRight = opMode.hardwareMap.get(DcMotor.class, "front_right");
        this.frontLeft = opMode.hardwareMap.get(DcMotor.class, "front_left");
        this.backRight = opMode.hardwareMap.get(DcMotor.class, "back_right");
        this.backLeft = opMode.hardwareMap.get(DcMotor.class, "back_left");

        // Create IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = opMode.hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    public double getCurrentAngle(){
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
    }

    private double getAngleDeviation(){
        return angleSubtraction(getCurrentAngle(), targetAngle);
    }

    /**
     * Given Two Angles, return the distance between the two angles, ensuring that the difference is less than 180 degrees.
     * If angle2 > angle1, the return value will be positive. If angle1 > angle2, the value will be negative.
     */
    static private double angleSubtraction(double angle1, double angle2) {
        double angleDeviation = angle2 - angle1;
        if (angleDeviation < -180){
            return 360 + angleDeviation;
        }
        else if(angleDeviation > 180){
            return -360 + angleDeviation;
        }else {
            return angleDeviation;
        }
    }


    public void driveByJoystick(double x1, double y1,
                                double x2) {
        double frontRightPower = x1-y1-x2;
        double frontLeftPower = y1+x1-x2;
        double backRightPower = -y1-x1-x2;
        double backLeftPower = y1-x1-x2;

        if(Math.abs(frontRightPower) > 1 || Math.abs(frontLeftPower) > 1
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

    public void stöp() {
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
    }
}
