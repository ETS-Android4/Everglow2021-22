package org.firstinspires.ftc.teamcode.FreightFrenzy.Utils;

public class MathUtils {



    /**
     * Given Two Angles, return the distance between the two angles, ensuring that the difference is less than 180 degrees.
     * baseAngle and currentAngle must be between -180 and 180.
     * If baseAngle > currentAngle, the return value will be positive. If currentAngle > baseAngle, the value will be negative.
     */
    static public double relativeAngle(double baseAngle, double currentAngle) {
        return normalizeAngle(currentAngle - baseAngle);
    }

    /**
     * Given an angle in any range, returns an equivelent angle between -180 and 180.
     */
    public static double normalizeAngle(double angle){
        return (angle+180) % 360 - 180;
    }

    public static double sinDegrees(double angle){
        return Math.sin(Math.toRadians(angle));
    }

    public static double cosDegrees(double angle){
        return Math.cos(Math.toRadians(angle));
    }



}
