package org.firstinspires.ftc.teamcode.FreightFrenzy.Utils;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class TimeUtils {
    public static LinearOpMode opMode;
    public static void sleep(long millis) {
        ElapsedTime elapsedTime = new ElapsedTime();
        while (elapsedTime.milliseconds() < millis && opMode.opModeIsActive()){
            // do noting
        }
    }
}
