package me.zsj.interessant.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.animation.DecelerateInterpolator;

import me.zsj.interessant.common.Holder;

/**
 * @author zsj
 */

public class SlideInDownAnimator extends SimpleItemAnimator {


    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        return false;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof Holder) {
            Holder commonHolder = (Holder) viewHolder;
            int height = ScreenUtils.getHeight(commonHolder.itemView.getContext());
            commonHolder.itemView.setTranslationY(height);
            commonHolder.itemView.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.0f))
                    .setDuration(1000)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            dispatchAddFinished(commonHolder);
                        }
                    })
                    .start();
        }
        return false;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        return false;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder,
                                 RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop,
                                 int toLeft, int toTop) {
        return false;
    }

    @Override
    public void runPendingAnimations() {}

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {}

    @Override
    public void endAnimations() {}

    @Override
    public boolean isRunning() {
        return false;
    }

}
