package org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class Utils {
    public static void saveToClipBoard(String data){
        Context context = AppUtil.getDefContext();
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("robot code", data);
        clipboard.setPrimaryClip(clip);
    }
}
