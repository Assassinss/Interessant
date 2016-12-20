package me.zsj.interessant.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import me.zsj.interessant.R;
import me.zsj.interessant.utils.ColorUtils;


public class ParallaxScrimageView extends ImageView {

    private static final int[] statePinned = {R.attr.pinned};

    private Paint scrimPaint;
    private int imageOffset;
    private int minOffset;
    private Rect clipBounds = new Rect();

    private float scrimAlpha = 0f;
    private float maxScrimAlpha = 1f;
    private int scrimColor = Color.TRANSPARENT;
    private boolean pinned;


    public ParallaxScrimageView(Context context) {
        this(context, null);
    }

    public ParallaxScrimageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParallaxScrimageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        scrimPaint = new Paint();
        float scrimAlpha = 0f;
        int scrimColor = Color.TRANSPARENT;
        scrimPaint.setColor(ColorUtils.modifyAlpha(scrimColor, scrimAlpha));

        setOutlineProvider(ViewOutlineProvider.BOUNDS);
    }

    public void setOffset(int offset) {
        offset = Math.max(minOffset, offset);
        if (offset != getTranslationY()) {
            setTranslationY(offset);
            float parallaxFactor = -0.5f;
            imageOffset = (int) (offset * parallaxFactor);
            clipBounds.set(0, -offset, getWidth(), getHeight());
            setClipBounds(clipBounds);
            setScrimAlpha(Math.min(
                    ((float) -offset / getMinimumHeight()) * maxScrimAlpha, maxScrimAlpha));
            postInvalidateOnAnimation();
        }
        pinned = minOffset == offset;
        refreshState();
    }

    public void setScrimAlpha(@FloatRange(from = 0f, to = 1f) float alpha) {
        if (scrimAlpha != alpha) {
            scrimAlpha = alpha;
            scrimPaint.setColor(ColorUtils.modifyAlpha(scrimColor, scrimAlpha));
            postInvalidateOnAnimation();
        }
    }

    private void refreshState() {
        refreshDrawableState();
        jumpDrawablesToCurrentState();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (h > getMinimumHeight()) {
            minOffset = getMinimumHeight() - h;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (imageOffset != 0) {
            final int saveCount = canvas.save();
            canvas.translate(0f, imageOffset);
            super.onDraw(canvas);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), scrimPaint);
            canvas.restoreToCount(saveCount);
        } else {
            super.onDraw(canvas);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), scrimPaint);
        }
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        int[] states = super.onCreateDrawableState(extraSpace + 1);
        if (pinned) {
            mergeDrawableStates(states, statePinned);
        }
        return states;
    }

}
