package me.zsj.interessant.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;

/**
 * @author zsj
 */

public class AnimUtils {

    public static void animIn(View view) {
        view.animate()
                .alpha(1f)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        view.setVisibility(View.VISIBLE);
                    }
                })
                .start();
    }

    public static void animOut(View view) {
        view.animate()
                .alpha(0f)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                })
                .start();
    }
}
