package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class DrivingSystem {
    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;


    public DrivingSystem(LinearOpMode opMode) {
        this.frontRight = opMode.hardwareMap.get(DcMotor.class, "front_right");
        this.frontLeft = opMode.hardwareMap.get(DcMotor.class, "front_left");
        this.backRight = opMode.hardwareMap.get(DcMotor.class, "back_right");
        this.backLeft = opMode.hardwareMap.get(DcMotor.class, "back_left");
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

    public void DriveStraight(double distance){
        ResetDistance();
        final double WHEEL_Radius_CM   = 4.8 ;
        double AverageMotars = (this.frontRight.getCurrentPosition() - this.frontLeft.getCurrentPosition() - this.backLeft.getCurrentPosition() + this.backRight.getCurrentPosition())/4;
        while((distance*1440)/(2*Math.PI*WHEEL_Radius_CM) > AverageMotars){
            driveByJoystick(0,0.5,0);
        }
    }

    public void ResetDistance(){
        this.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


}
