package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class DuckSystem {
    DcMotor duck1;
    DcMotor duck2;

    public DuckSystem(LinearOpMode opMode) {
        this.duck1 = opMode.hardwareMap.get(DcMotor.class, "duck1");
        this.duck2 = opMode.hardwareMap.get(DcMotor.class, "duck2");
    }

    public void run() {
        duck1.setPower(-0.5);
        duck2.setPower(0.5);
    }

    public void runFor(long durationMillis){
        run();
        TimeUtils.sleep(durationMillis);
        stöp();
    }

    public void stöp() {
        duck1.setPower(0);
        duck2.setPower(0);
    }
}
