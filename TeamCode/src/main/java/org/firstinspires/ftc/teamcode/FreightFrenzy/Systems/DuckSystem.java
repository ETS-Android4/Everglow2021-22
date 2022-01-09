package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class DuckSystem {
    private final DcMotor duck1;
    private final DcMotor duck2;

    private boolean isRunning = false;

    public DuckSystem(LinearOpMode opMode) {
        this.duck1 = opMode.hardwareMap.get(DcMotor.class, "duck1");
        this.duck2 = opMode.hardwareMap.get(DcMotor.class, "duck2");
    }

    public void toggle(){
        if (isRunning){
            stöp();
        }else {
            run();
        }
    }

    public void run() {
        isRunning = true;
        duck1.setPower(0.4);
        duck2.setPower(-0.4);
    }

    public void runFor(long durationMillis) {
        run();
        TimeUtils.sleep(durationMillis);
        stöp();
    }

    public void stöp() {
        isRunning = false;
        duck1.setPower(0);
        duck2.setPower(0);
    }
}
