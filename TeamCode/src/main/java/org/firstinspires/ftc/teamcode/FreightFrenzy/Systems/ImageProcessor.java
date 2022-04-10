package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class ImageProcessor {
    private Mat hsvAll;
    private Mat mask1;
    private Mat mask2;
    private Mat coloredMask;

    private final MathUtils.Side side;

    public ImageProcessor(Mat frameTemplate, MathUtils.Side side) {
        hsvAll = new Mat(frameTemplate.rows(), frameTemplate.cols(), frameTemplate.type());
        mask1 = new Mat(frameTemplate.rows(), frameTemplate.cols(), frameTemplate.type());
        mask2 = new Mat(frameTemplate.rows(), frameTemplate.cols(), frameTemplate.type());
        coloredMask = new Mat(frameTemplate.rows(), frameTemplate.cols(), frameTemplate.type());

        this.side = side;
    }

    private static final int rectXIncrease = 20;
    private static final int rectYIncrease = 100;

    private static Rect createRect(int x, int y, int width, int height) {
        return new Rect(x - rectXIncrease / 2, y - rectYIncrease, width + rectXIncrease, height + rectYIncrease);
    }

    private static final Rect leftArea = createRect(503, 491, 229, 234);
    private static final Rect centerArea = createRect(903, 484, 300, 218);
    private static final Rect rightArea = createRect(1376, 463, 249, 232);

    private static final Scalar low_blue = new Scalar(94, 80, 2);
    private static final Scalar high_blue = new Scalar(126, 255, 255);

    private static final Scalar low_red1 = new Scalar(151, 80, 2);
    private static final Scalar high_red1 = new Scalar(180, 255, 255);
    private static final Scalar low_red2 = new Scalar(0, 80, 2);
    private static final Scalar high_red2 = new Scalar(15, 255, 255);

    private void findColoredAreas() {
        if (side == MathUtils.Side.RED) {
            Core.inRange(hsvAll, low_red1, high_red1, mask1);
            Core.inRange(hsvAll, low_red2, high_red2, mask2);
            Core.bitwise_or(mask1, mask2, coloredMask);
        } else {
            Core.inRange(hsvAll, low_blue, high_blue, coloredMask);
        }
    }

    public ArmSystem.Floors findFloor(Mat input) {
        Imgproc.cvtColor(input, hsvAll, Imgproc.COLOR_RGB2HSV);
        findColoredAreas();

        Mat left = coloredMask.submat(leftArea);
        Mat center = coloredMask.submat(centerArea);
        Mat right = coloredMask.submat(rightArea);

        int leftPixels = Core.countNonZero(left);
        int centerPixels = Core.countNonZero(center);
        int rightPixels = Core.countNonZero(right);

        // find the area with the fewest colored
        if (leftPixels < rightPixels && leftPixels < centerPixels) {
            return ArmSystem.Floors.FIRST;
        } else if (centerPixels < leftPixels && centerPixels < rightPixels) {
            return ArmSystem.Floors.SECOND;
        } else {
            return ArmSystem.Floors.THIRD;
        }
    }

    public Mat drawOnPreview(Mat input){
        Imgproc.cvtColor(input, hsvAll, Imgproc.COLOR_RGB2HSV);
        findColoredAreas();
        if (side == MathUtils.Side.RED) {
            input.setTo(new Scalar(72, 224, 251), coloredMask);
        } else {
            input.setTo(new Scalar(251, 72, 196), coloredMask);
        }

        Imgproc.rectangle(input, leftArea, new Scalar(0, 255, 0), 7);
        Imgproc.rectangle(input, centerArea, new Scalar(0, 255, 0), 7);
        Imgproc.rectangle(input, rightArea, new Scalar(0, 255, 0), 7);
        return input;
    }
}
