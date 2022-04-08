package org.firstinspires.ftc.teamcode.FreightFrenzy.Utils;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import androidx.annotation.Nullable;

import org.ejml.simple.SimpleMatrix;

import java.util.List;

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
    public static double normalizeAngle(double angle) {
        return (angle + 180) % 360 - 180;
    }

    public static double sinDegrees(double angle) {
        return Math.sin(Math.toRadians(angle));
    }

    public static double cosDegrees(double angle) {
        return Math.cos(Math.toRadians(angle));
    }

    @Nullable
    public static Double min(List<Double> values) {
        if (values.isEmpty()) {
            return null;
        }
        Double minValue = Double.MAX_VALUE;
        for (Double value : values) {
            if (value < minValue) {
                minValue = value;
            }
        }
        return minValue;
    }
    public static double min(double[] values) {
        if (values.length == 0) {
            return 0;
        }
        double minValue = Double.MAX_VALUE;
        for (double value : values) {
            if (value < minValue) {
                minValue = value;
            }
        }
        return minValue;
    }

    @Nullable
    public static Double max(List<Double> values) {
        if (values.isEmpty()) {
            return null;
        }
        Double maxValue = Double.MIN_VALUE;
        for (Double value : values) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    public static Double max(double[] values) {
        Double maxValue = Double.MIN_VALUE;
        for (Double value : values) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    public static double average(List<Double> values) {
        double sum = 0;
        for (double val : values) {
            sum += val;
        }
        if (values.size() == 0) {
            return 0;
        }
        return sum / values.size();
    }

    public static double sum(double[] values){
        double sum = 0;
        for (int i = 0; i<values.length;i++){
            sum += values[i];
        }
        return sum;
    }

    public static int isMirrored(int mirror){
        if (mirror == 1){
            return 0;
        }else {
            return 1;
        }
    }

    public static int numValuesOutOfRange(List<Double> values, double min, double max){
        int numOutOfRange = 0;
        for (double val: values){
            if (val > max || val < min){
                numOutOfRange++;
            }
        }
        return numOutOfRange;
    }

    public static double reduceValue(double value, double reducer, double reduceStart, double reduceMinMultipler){
        if (reducer > reduceStart){
            return value;
        }else {
            return value * reduceMinMultipler + (1 - reduceMinMultipler) * reducer / reduceStart * value;
        }
    }

    public static class Vector2{
        public double x;
        public double y;

        public Vector2(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public String toString(){
            return "x = " + x + ", y = " + y;
        }
    }


    public static Vector2 driveImpacts(double x1, double y1, double x2, double fr, double fl, double br, double bl, double theta){
        SimpleMatrix actualMotors = new SimpleMatrix( new double[][] {{fr}, {fl}, {br}, {bl}} );
        SimpleMatrix driveToMotorsLinear = new SimpleMatrix(new double[][] {
                {-1, -1, -0.7},
                {-1, 1, 0.7},
                {-1, 1, -0.7},
                {-1, -1, 0.7},
        });
        SimpleMatrix rotate = new SimpleMatrix(new double[][]{
                {cos(theta), sin(theta), 0},
                {-sin(theta), cos(theta), 0},
                {0, 0, 1}
        });
        SimpleMatrix driveToMotors = driveToMotorsLinear.mult(rotate);
        System.out.println(actualMotors);

        SimpleMatrix drive = driveToMotors.solve(actualMotors); // todo: catch singularMatrixException

        System.out.println(drive);

        return new Vector2(drive.get(0, 0),drive.get(1, 0));
    }

    public enum Side{
        RED(1), BLUE(-1);
        Side(int mirror) {
            this.mirror = mirror;
        }
        public final int mirror;

        public static Side fromMirror(int mirror){
            if (mirror == 1){
                return RED;
            }else {
                return BLUE;
            }
        }
    }
}
