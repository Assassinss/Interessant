package me.zsj.interessant.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * @author zsj
 */

/**
 * reference https://github.com/mcxtzhang/ZLayoutManager
 */
public class TagLayoutManager extends RecyclerView.LayoutManager {

    private SparseArray<Rect> itemFrames = new SparseArray<>();
    private int verticalOffset;
    private int lastVisiblePosition;
    private int firstVisiblePosition;
    private int totalChildHeight;


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        if (getItemCount() <= 0) return;
        if (state.isPreLayout()) return;

        detachAndScrapAttachedViews(recycler);

        fill(recycler, 0);
    }

    private int fill(RecyclerView.Recycler recycler, int dy) {

        int topOffset = getPaddingTop();

        if (getChildCount() > 0) {
            for (int i = getChildCount() - 1; i >= 0; i--) {
                View child = getChildAt(i);
                if (dy > 0) {
                    if (getDecoratedBottom(child) - dy < topOffset) {
                        removeAndRecycleView(child, recycler);
                        firstVisiblePosition++;
                    }
                } else if (dy < 0) {
                    if (getDecoratedTop(child) - dy > getHeight() - getPaddingBottom()) {
                        removeAndRecycleView(child, recycler);
                        firstVisiblePosition--;
                    }
                }
            }
        }

        int leftOffset = getPaddingLeft();
        int lineMaxHeight = 0;

        if (dy >= 0) {
            int minPos = firstVisiblePosition;
            lastVisiblePosition = getItemCount() - 1;
            if (getChildCount() > 0) {
                View lastView = getChildAt(getChildCount() - 1);
                minPos = getPosition(lastView) + 1;
                topOffset = getDecoratedTop(lastView);
                leftOffset = getDecoratedRight(lastView);
                lineMaxHeight = Math.max(lineMaxHeight, getChildRealHeight(lastView));
            }

            for (int i = minPos; i <= lastVisiblePosition; i++) {
                View child = recycler.getViewForPosition(i);
                addView(child);
                measureChildWithMargins(child, 0, 0);

                if (leftOffset + getChildRealWidth(child) <= getRealWidth()) {
                    layoutDecoratedWithMargins(child, leftOffset, topOffset,
                            leftOffset + getChildRealWidth(child),
                            topOffset + getChildRealHeight(child));

                    Rect rect = new Rect(leftOffset, topOffset + verticalOffset,
                            leftOffset + getChildRealWidth(child),
                            topOffset + getChildRealHeight(child) + verticalOffset);
                    itemFrames.put(i, rect);

                    leftOffset += getChildRealWidth(child);
                    lineMaxHeight = Math.max(lineMaxHeight, getChildRealHeight(child));
                } else {
                    leftOffset = getPaddingLeft();
                    topOffset += lineMaxHeight;
                    totalChildHeight = topOffset;
                    lineMaxHeight = 0;

                    if (topOffset - dy > getHeight() - getPaddingBottom()) {
                        removeAndRecycleView(child, recycler);
                        lastVisiblePosition = i - 1;
                    } else {
                        layoutDecoratedWithMargins(child, leftOffset, topOffset,
                                leftOffset + getChildRealWidth(child),
                                topOffset + getChildRealHeight(child));

                        Rect rect = new Rect(leftOffset, topOffset + verticalOffset,
                                leftOffset + getChildRealWidth(child),
                                topOffset + getChildRealHeight(child) + verticalOffset);
                        itemFrames.put(i, rect);

                        leftOffset += getChildRealWidth(child);
                        lineMaxHeight = Math.max(lineMaxHeight, getChildRealHeight(child));
                    }
                }
            }

            View lastChild = getChildAt(getChildCount() - 1);
            if (getPosition(lastChild) == getItemCount() - 1) {
                int gap = getHeight() - getPaddingBottom() - getDecoratedBottom(lastChild);
                if (gap > 0) {
                    dy -= gap;
                }
            }

        } else {
            int maxPos = getItemCount() - 1;
            firstVisiblePosition = 0;

            if (getChildCount() > 0) {
                View firstView = getChildAt(0);
                maxPos = getPosition(firstView) - 1;
            }

            for (int i = maxPos; i >= firstVisiblePosition; i--) {
                Rect rect = itemFrames.get(i);

                if (rect.bottom - verticalOffset - dy < getPaddingTop()) {
                    firstVisiblePosition = i + 1;
                    break;
                } else {
                    View child = recycler.getViewForPosition(i);
                    addView(child, 0);
                    measureChildWithMargins(child, 0, 0);

                    layoutDecoratedWithMargins(child, rect.left, rect.top - verticalOffset,
                            rect.right, rect.bottom - verticalOffset);
                }
            }
        }

        return dy;
    }

    private int getChildRealWidth(View view) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        return getDecoratedMeasuredWidth(view) + params.leftMargin + params.rightMargin;
    }

    private int getChildRealHeight(View view) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + params.topMargin + params.bottomMargin;
    }

    private int getRealWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler,
                                  RecyclerView.State state) {
        if (getItemCount() == 0 && dy == 0) {
            return 0;
        }

        if (totalChildHeight <= getRealWidth()) {
            return 0;
        }

        int viewOffset = dy;

        if ((verticalOffset + dy) < 0) {
            viewOffset = -verticalOffset;
        } else if (viewOffset > 0) {
            View lastChild = getChildAt(getChildCount() - 1);
            if (getPosition(lastChild) == getItemCount() - 1) {
                int gap = getHeight() - getPaddingBottom() - getDecoratedBottom(lastChild);
                if (gap > 0) {
                    viewOffset = -gap;
                } else if (gap == 0) {
                    viewOffset = 0;
                } else {
                    viewOffset = Math.min(viewOffset, -gap);
                }
            }
        }

        fill(recycler, dy);
        offsetChildrenVertical(-viewOffset);
        verticalOffset += viewOffset;
        return viewOffset;
    }

}
