package me.zsj.interessant.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.zsj.interessant.R;

/**
 * @author zsj
 */

public class IndicatorTabLayout extends TabLayout implements ViewPager.OnPageChangeListener {

    private List<View> tabViews = new ArrayList<>();
    private int indicatorWidth;
    private int indicatorHeight;
    private int layoutHeight;
    private int indicatorColor;
    private int[] tabWidths;
    private int selectedPosition;
    private int tabCount;
    private float leftPositionOffset;
    private int totalOffset;

    private Paint indicatorPaint;
    private Paint paint;


    public IndicatorTabLayout(Context context) {
        this(context, null);
    }

    public IndicatorTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IndicatorTabLayout);
        indicatorHeight = ta.getDimensionPixelOffset(R.styleable.IndicatorTabLayout_indicatorHeight, dpToPx(2));
        indicatorWidth = ta.getDimensionPixelOffset(R.styleable.IndicatorTabLayout_indicatorWidth, 0);
        indicatorColor = ta.getColor(R.styleable.IndicatorTabLayout_indicatorColor,
                ContextCompat.getColor(context, R.color.colorAccent));
        ta.recycle();

        indicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indicatorPaint.setColor(indicatorColor);
        indicatorPaint.setStyle(Paint.Style.FILL);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        super.setupWithViewPager(viewPager);
        assert viewPager != null;
        viewPager.addOnPageChangeListener(this);
        tabCount = viewPager.getAdapter().getCount();
        tabWidths = new int[tabCount];
    }

    @Override
    public void addTab(@NonNull Tab tab, boolean setSelected) {
        TextView textView = (TextView) View.inflate(getContext(), R.layout.indicator_text, null);
        textView.setText(tab.getText());
        renderTabText(0);
        tab.setCustomView(textView);
        tabViews.add(textView);
        super.addTab(tab, setSelected);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        layoutHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureTabWidths();

        int tabWidth = totalOffset / tabWidths.length;
        if (indicatorWidth == 0) {
            indicatorWidth = tabWidth;
        }
    }

    private void measureTabWidths() {
        for (int i = 0; i < tabCount; i++) {
            int tabWidth = ((LinearLayout) getChildAt(0)).getChildAt(i).getWidth();
            if (tabWidth == 0) {
                break;
            }
            tabWidths[i] = tabWidth;
            totalOffset += tabWidth;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        int offset = 0;
        for (int i = 0; i < selectedPosition; i++) {
            offset += tabWidths[i];
        }

        canvas.translate((leftPositionOffset * tabWidths[selectedPosition]), 0);

        int topOffset = layoutHeight - indicatorHeight;
        int rightOffset = tabWidths[selectedPosition] / 2 - (indicatorWidth / 2);

        canvas.drawRect(offset, topOffset, offset + rightOffset,
                layoutHeight, paint);

        if (indicatorWidth > 0) {
            canvas.drawRect(offset + rightOffset, topOffset,
                    offset + indicatorWidth + rightOffset, layoutHeight, indicatorPaint);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        selectedPosition = position;
        leftPositionOffset = positionOffset;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public void onPageSelected(int position) {
        renderTabText(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void renderTabText(int position) {
        for (int i = 0; i < tabViews.size(); i++) {
            TextView textView = (TextView) tabViews.get(i);
            if (i == position) {
                textView.setTextColor(indicatorColor);
                textView.setTypeface(null, Typeface.BOLD);
            } else {
                textView.setTextColor(modulateColorAlpha(indicatorColor, 0.8f));
                textView.setTypeface(null, Typeface.NORMAL);
            }
        }
    }

    private int dpToPx(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }

    private int modulateColorAlpha(int color, float alphaMod) {
        int alpha = Math.round(Color.alpha(color) * alphaMod);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

}
