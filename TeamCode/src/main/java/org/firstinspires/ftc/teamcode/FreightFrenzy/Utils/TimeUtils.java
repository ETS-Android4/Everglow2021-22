package org.firstinspires.ftc.teamcode.FreightFrenzy.Utils;

public class TimeUtils {
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
