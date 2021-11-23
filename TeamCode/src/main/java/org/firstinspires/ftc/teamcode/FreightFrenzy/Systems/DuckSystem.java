package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class DuckSystem {
    DcMotor dückMotor;

    public DuckSystem(LinearOpMode opMode) {
        this.dückMotor = opMode.hardwareMap.get(DcMotor.class, "duck");
    }

    public void run() {
        dückMotor.setPower(0.68);
    }

    public void stöp() {
        dückMotor.setPower(0);
    }
}
