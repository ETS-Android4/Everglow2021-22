package org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static void saveToClipBoard(String data) {
        Context context = AppUtil.getDefContext();
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("robot code", data);
        clipboard.setPrimaryClip(clip);
    }

    public static void saveImage(Mat image) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS").format(new Date());
        File captureDirectory = AppUtil.ROBOT_DATA_DIR;
        File file = new File(captureDirectory, String.format(Locale.getDefault(), "webcam-frame-%s.jpg", timeStamp));
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            Bitmap bitmap = matToBitmap(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        }
    }

    public static String timestampString(){
        return new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS").format(new Date());
    }

    public static Bitmap matToBitmap(Mat image) {
        return Bitmap.createBitmap(image.cols(), image.rows(), Bitmap.Config.ARGB_8888);
    }
}
