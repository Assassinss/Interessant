package me.zsj.interessant.utils;

import android.content.Context;
import android.util.TypedValue;

public class PixelConverter {

    private PixelConverter(){}

    public static int dptopx(Context context, int dp) {
        float size = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
        return (int) size;
    }
}
