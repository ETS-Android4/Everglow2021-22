package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class TotemSystem {
    private final LinearOpMode opMode;
    public Servo altitude1;
    public Servo altitude2;
    private final CRServo meterLeft;
    private final CRServo meterRight;

    private static final double ALTITUDE1_ZERO = 0.5;
    private static final double ALTITUDE2_ZERO = 0.5;
    public static final double ALTITUDE1_MAX = 0.7;
    private static final double ALTITUDE2_MAX = 0.32;

    public static final double ALTITUDE_PICKUP = 0.24;
    public static final double ALTITUDE_PICKUP_HIGH = 0.25;
    public static final double ALTITUDE_AFTER_PICKUP = 0.6;


    public TotemSystem(LinearOpMode opMode) {
        this(opMode, true);
    }

    public TotemSystem(LinearOpMode opMode, boolean isAutonomous) {
        this.opMode = opMode;
        altitude1 = opMode.hardwareMap.get(Servo.class, "altitude_right");
        altitude2 = opMode.hardwareMap.get(Servo.class, "altitude_left");
        meterLeft = opMode.hardwareMap.get(CRServo.class, "meter_left");
        meterRight = opMode.hardwareMap.get(CRServo.class, "meter_right");
        if (isAutonomous) {
            setAltitude(ALTITUDE1_MAX);
        } else {
            setAltitude(ALTITUDE_AFTER_PICKUP);
        }
    }

    public void extendLeft(double power) {
        meterLeft.setPower(power);
    }

    public void extendRight(double power) {
        meterRight.setPower(power);
    }

    public void stopLeft() {
        meterLeft.setPower(0);
    }

    public void stopRight() {
        meterRight.setPower(0);
    }

    public void moveAltitude(double delta) {
//        if(altitude1.getPosition() + delta > ALTITUDE1_MAX || altitude2.getPosition() - delta < ALTITUDE2_MAX) return;
        if (altitude1 != null) {
            altitude1.setPosition(altitude1.getPosition() + delta);
            altitude2.setPosition(altitude2.getPosition() - delta);
        }
    }

    public void setAltitude(double position) {
        if (altitude1 != null) {
            altitude1.setPosition(position);
            altitude2.setPosition(1 - position + 0.01);
        }
    }

    public void setAltitudeSlowly(double targetAltitude, long time) {
        if (altitude1 != null) {
            final int NUM_ITERATIONS = 100;
            final double NUM_ITERATIONS_D = NUM_ITERATIONS; // store as double so math works out
            final double startAltitude = altitude1.getPosition();
            ElapsedTime elapsedTime = new ElapsedTime();
            for (int i = 1; i <= NUM_ITERATIONS; i++) {
                double percentDone = i / NUM_ITERATIONS_D;
                setAltitude(startAltitude * (1 - percentDone) + targetAltitude * percentDone);

                int timeDelay = (int) (time * percentDone - elapsedTime.milliseconds());
                TimeUtils.sleep(Math.max(timeDelay, 0));
            }
        }
    }

    public void disable() {
        ((PwmControl) altitude1).setPwmDisable();
    }

    public void enable() {
        ((PwmControl) altitude1).setPwmEnable();
    }

    public boolean isEnabled() {
        return ((PwmControl) altitude1).isPwmEnabled();
    }

}
