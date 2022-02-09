package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class DuckSystem {
    private final DcMotor duck1;
    private final DcMotor duck2;

    private boolean isRunning = false;
    private double  speed     = 0.7;

    public DuckSystem(LinearOpMode opMode) {
        this.duck1 = opMode.hardwareMap.get(DcMotor.class, "duck1");
        this.duck2 = opMode.hardwareMap.get(DcMotor.class, "duck2");
    }

    /**
     * Toggles the duck motors on and off.
     */
    public void toggle() {
        if (isRunning) {
            stop();
            isRunning = false;
        } else {
            run();
            isRunning = true;
        }
    }

    /**
     * Turns the duck motors.
     */
    public void run() {
        isRunning = true;
        duck1.setPower(-speed);
        duck2.setPower(speed);
    }

    public void increaseSpeed() {
        speed += 0.1;
    }

    public void decreaseSpeed() {
        speed -= 0.1;
    }

    /**
     * Turns the duck motors on for a set duration of time.
     * @param durationMillis the duration of running in milliseconds.
     */
    public void runFor(long durationMillis) {
        run();
        TimeUtils.sleep(durationMillis);
        stop();
    }

    public double GetSpeed() {
        return speed;
    }

    public void stop() {
        isRunning = false;
        duck1.setPower(0);
        duck2.setPower(0);
    }
}
