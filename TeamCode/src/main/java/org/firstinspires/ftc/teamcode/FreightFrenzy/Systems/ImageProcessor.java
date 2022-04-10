package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class ImageProcessor {
    private static final class TotemAreas {

        public final Rect left;
        public final Rect center;
        public final Rect right;

        public TotemAreas(Rect left, Rect center, Rect right) {
            this.left = left;
            this.center = center;
            this.right = right;
        }

    }

    private static final TotemAreas BLUE_AREAS = new TotemAreas(
            createRect(0, 553, 250, 250),
            createRect(379, 528, 250, 250),
            createRect(828, 505, 250, 250)
    );

    private static final TotemAreas RED_AREAS = new TotemAreas(
            createRect(584, 361, 250, 250),
            createRect(1050, 341, 250, 250),
            createRect(1424, 318, 250, 250)
    );

    private final MathUtils.Side side;

    private static final int RECT_X_INCREASE = 0;
    private static final int RECT_Y_INCRASE = 0;

    private static Rect createRect(int x, int y, int width, int height) {
        return new Rect(x - RECT_X_INCREASE / 2, y - RECT_Y_INCRASE, width + RECT_X_INCREASE, height + RECT_Y_INCRASE);
    }

    private static final Scalar low_blue = new Scalar(94, 80, 2);
    private static final Scalar high_blue = new Scalar(126, 255, 255);

    private static final Scalar low_red1 = new Scalar(151, 80, 2);
    private static final Scalar high_red1 = new Scalar(180, 255, 255);
    private static final Scalar low_red2 = new Scalar(0, 80, 2);
    private static final Scalar high_red2 = new Scalar(15, 255, 255);

    private final Mat hsvAll;
    private final Mat mask1;
    private final Mat mask2;
    private final Mat coloredMask;
    private final TotemAreas totemAreas;

    public ImageProcessor(Mat frameTemplate, MathUtils.Side side) {
        hsvAll = new Mat(frameTemplate.rows(), frameTemplate.cols(), frameTemplate.type());
        mask1 = new Mat(frameTemplate.rows(), frameTemplate.cols(), frameTemplate.type());
        mask2 = new Mat(frameTemplate.rows(), frameTemplate.cols(), frameTemplate.type());
        coloredMask = new Mat(frameTemplate.rows(), frameTemplate.cols(), frameTemplate.type());
        this.side = side;
        if (side == MathUtils.Side.RED) {
            totemAreas = RED_AREAS;
        } else {
            totemAreas = BLUE_AREAS;
        }
    }

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

        Mat left = coloredMask.submat(totemAreas.left);
        Mat center = coloredMask.submat(totemAreas.center);
        Mat right = coloredMask.submat(totemAreas.right);

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

    public Mat drawOnPreview(Mat input) {
        Imgproc.cvtColor(input, hsvAll, Imgproc.COLOR_RGB2HSV);
        findColoredAreas();
        if (side == MathUtils.Side.RED) {
            input.setTo(new Scalar(72, 224, 251), coloredMask);
        } else {
            input.setTo(new Scalar(251, 72, 196), coloredMask);
        }

        Imgproc.rectangle(input, totemAreas.left, new Scalar(0, 255, 0), 7);
        Imgproc.rectangle(input, totemAreas.center, new Scalar(0, 255, 0), 7);
        Imgproc.rectangle(input, totemAreas.right, new Scalar(0, 255, 0), 7);
        return input;
    }
}
