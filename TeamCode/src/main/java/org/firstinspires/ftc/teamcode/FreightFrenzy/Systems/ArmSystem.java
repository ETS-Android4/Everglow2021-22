package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class ArmSystem {
    DcMotor flyWheels;
    DcMotor arm;

    public ArmSystem(LinearOpMode opMode) {
        this.flyWheels = opMode.hardwareMap.get(DcMotor.class, "flyWheels");
        this.arm = opMode.hardwareMap.get(DcMotor.class, "arm");
    }

    public void pull() {
        double position = flyWheels.getCurrentPosition();
        flyWheels.setPower(0.5);
        while (position < 268) {
            position = flyWheels.getCurrentPosition();
        }
        flyWheels.setPower(0);
    }

    public void push() {
        double position = flyWheels.getCurrentPosition();
        flyWheels.setPower(-0.5);
        while (position < 268) {
            position = flyWheels.getCurrentPosition();
        }
        flyWheels.setPower(0);
    }
}
