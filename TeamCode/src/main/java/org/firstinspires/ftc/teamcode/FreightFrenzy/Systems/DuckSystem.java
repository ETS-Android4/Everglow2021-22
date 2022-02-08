package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class DuckSystem {
    private final DcMotor duck1;
    private final DcMotor duck2;

    private boolean isRunning = false;
    private double speed = 0.9;

    public DuckSystem(LinearOpMode opMode) {
        this.duck1 = opMode.hardwareMap.get(DcMotor.class, "duck1");
        this.duck2 = opMode.hardwareMap.get(DcMotor.class, "duck2");
    }

    public void toggle(){
        if (isRunning){
            stöp();
            isRunning = false;
        }else {
            run();
            isRunning = true;
        }
    }

    public void run() {
        isRunning = true;
        duck1.setPower(-speed);
        duck2.setPower(speed);
    }

    public void increaseSpeed(){
        speed += 0.1;
    }
    public void decreaseSpeed(){
        speed -= 0.1;
    }
    public void runFor(long durationMillis) {
        run();
        TimeUtils.sleep(durationMillis);
        stöp();
    }
    public double GetSpeed(){
        return speed;
    }

    public void stöp() {
        isRunning = false;
        duck1.setPower(0);
        duck2.setPower(0);
    }
}
