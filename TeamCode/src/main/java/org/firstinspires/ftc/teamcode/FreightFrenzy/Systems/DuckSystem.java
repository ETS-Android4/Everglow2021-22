package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class DuckSystem {
    public final DcMotor dückMotor;

    public DuckSystem(LinearOpMode opMode) {
        this.dückMotor = opMode.hardwareMap.get(DcMotor.class, "duck");
    }

    public void runDuckWheel() {
        dückMotor.setPower(0.3);
    }

    public void stopDuckWheel() {
        dückMotor.setPower(0);
    }
}
