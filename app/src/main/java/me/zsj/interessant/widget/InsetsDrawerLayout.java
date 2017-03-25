package me.zsj.interessant.widget;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;

import me.zsj.interessant.R;

/**
 * @author zsj
 */

public class InsetsDrawerLayout extends DrawerLayout implements View.OnApplyWindowInsetsListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView;


    public InsetsDrawerLayout(Context context) {
        this(context, null);
    }

    public InsetsDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InsetsDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOnApplyWindowInsetsListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.list);
    }

    @Override
    public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
        int l = insets.getSystemWindowInsetLeft();
        int t = insets.getSystemWindowInsetTop();
        int r = insets.getSystemWindowInsetRight();

        toolbar.setPadding(l, toolbar.getPaddingTop() + t, 0, 0);

        final boolean ltr = recyclerView.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR;

        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingEnd() + (ltr ? r : 0),
                getPaddingBottom()
        );

        setOnApplyWindowInsetsListener(null);
        return insets.consumeSystemWindowInsets();
    }

}
