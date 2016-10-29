package me.zsj.interessant.utils;

import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;

/**
 * Created by zsj on 2016/10/29.
 */

public class AnimUtils {

    public static void animIn(View view) {
        view.animate()
                .alpha(1f)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(300)
                .start();
    }

    public static void animOut(View view) {
        view.animate()
                .alpha(0f)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(300)
                .start();;
    }
}
