package me.zsj.interessant.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author zsj
 */

public class ScreenUtils {

    private ScreenUtils(){}

    public static int getWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        Display display = manager.getDefaultDisplay();
        display.getRealSize(size);
        return size.x;
    }

    public static int getHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        Display display = manager.getDefaultDisplay();
        display.getRealSize(size);
        return size.y;
    }

}
