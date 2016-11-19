package me.zsj.interessant.interesting;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.zsj.interessant.MainActivity;
import me.zsj.interessant.model.ItemList;
import me.zsj.interessant.rx.ErrorAction;
import me.zsj.interessant.rx.RxScroller;

/**
 * Created by zsj on 2016/10/11.
 */

public class ShareListFragment extends ItemFragment {

    private static final String SHARE = "shareCount";

    private InterestingAdapter adapter;

    private List<ItemList> shareList = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final int categoryId = getArguments().getInt(MainActivity.CATEGORY_ID);

        adapter = new InterestingAdapter(context, shareList);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);

        loadData(categoryId, SHARE);

        RxRecyclerView.scrollStateChanges(list)
                .compose(bindToLifecycle())
                .compose(RxScroller.scrollTransformer(layoutManager,
                        adapter, shareList))
                .subscribe(newState -> {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        start += 10;
                        loadData(categoryId, SHARE);
                    }
                });

    }

    private void loadData(int categoryId, String strategy) {
        interestingApi.getInteresting(start, categoryId, strategy)
                .compose(bindToLifecycle())
                .compose(interestingTransformer)
                .doOnNext(itemLists -> shareList.addAll(itemLists))
                .subscribe(itemLists -> {
                    adapter.notifyDataSetChanged();
                }, ErrorAction.errorAction(context));

    }

}

