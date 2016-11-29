package me.zsj.interessant.interesting;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.zsj.interessant.MainActivity;
import me.zsj.interessant.model.Interesting;
import me.zsj.interessant.model.ItemList;
import me.zsj.interessant.rx.ErrorAction;
import me.zsj.interessant.rx.RxScroller;
import rx.Observable;

/**
 * Created by zsj on 2016/10/11.
 */

public class TimeListFragment extends ItemFragment {

    private static final String DATE = "date";

    private InterestingAdapter adapter;

    private List<ItemList> timeList = new ArrayList<>();

    private static boolean related;
    private static boolean relatedHeader;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final int categoryId = getArguments().getInt(MainActivity.CATEGORY_ID);
        related = getArguments().getBoolean(InterestingActivity.RELATED_VIDEO);
        relatedHeader = getArguments().getBoolean(InterestingActivity.RELATED_HEADER_VIDEO);

        adapter = new InterestingAdapter(context, timeList);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);

        loadData(categoryId, DATE);

        RxRecyclerView.scrollStateChanges(list)
                .compose(bindToLifecycle())
                .compose(RxScroller.scrollTransformer(layoutManager,
                        adapter, timeList))
                .subscribe(newState -> {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        start += 10;
                        loadData(categoryId, DATE);
                    }
                });

    }

    private void loadData(int categoryId, String strategy) {
        Observable<Interesting> result;
        if (related) {
            result = interestingApi.related(start, categoryId, strategy);
        } else if (relatedHeader) {
            result = interestingApi.relatedHeader(start, categoryId, strategy);
        } else {
            result = interestingApi.getInteresting(start, categoryId, strategy);
        }

        result.compose(bindToLifecycle())
                .compose(interestingTransformer)
                .doOnNext(itemLists -> timeList.addAll(itemLists))
                .subscribe(itemLists -> {
                    adapter.notifyDataSetChanged();
                }, ErrorAction.errorAction(context));
    }

}
