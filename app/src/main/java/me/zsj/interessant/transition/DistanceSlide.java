package me.zsj.interessant.transition;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import me.zsj.interessant.R;

import static android.animation.ObjectAnimator.ofFloat;

/**
 * Created by zsj on 2016/11/7.
 */

public class DistanceSlide extends Visibility {

    private static final String PROPNAME_SCREEN_LOCATION = "android:visibility:screenLocation";

    private int spread = 1;

    public DistanceSlide() {}

    public DistanceSlide(Context context, AttributeSet set) {
        super(context, set);
        TypedArray ta = context.obtainStyledAttributes(set, R.styleable.DistanceSlide);
        spread = ta.getInteger(R.styleable.DistanceSlide_spread, spread);
        ta.recycle();
    }

    public int getSpread() {
        return spread;
    }

    public void setSpread(int spread) {
        this.spread = spread;
    }

    @Override
    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        int[] position = (int[]) endValues.values.get(PROPNAME_SCREEN_LOCATION);
        return createAnimator(view, sceneRoot.getHeight() + (position[1] * spread), 0f);
    }

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        int[] position = (int[]) endValues.values.get(PROPNAME_SCREEN_LOCATION);
        return createAnimator(view, 0f, sceneRoot.getHeight() + position[1] * spread);
    }

    private Animator createAnimator(View view, float startTranslationY, float endTranslationY) {
        view.setTranslationY(startTranslationY);
        return ofFloat(view, View.TRANSLATION_Y, endTranslationY);
    }

}
