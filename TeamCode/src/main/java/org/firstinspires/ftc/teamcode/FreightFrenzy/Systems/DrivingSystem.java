package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class DrivingSystem {
    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;

    public DrivingSystem(LinearOpMode opMode) {
        this.frontRight = opMode.frontRightMotor;
        this.frontLeft = opMode.frontLeftMotor;
        this.backRight = opMode.backRightMotor;
        this.backLeft = opMode.backLeftMotor;
    }

    public void driveByJoystick(double x1, double y1,
                                double x2) {
        double frontRightPower = -x1-y1-x2;
        double frontLeftPower = -(-y1+x1+x2);
        double backRightPower = -y1+x1-x2;
        double backLeftPower = -(-y1-x1+x2);

        //if()

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
