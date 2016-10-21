package me.zsj.interessant.interesting;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.zsj.interessant.IntentManager;
import me.zsj.interessant.MainActivity;
import me.zsj.interessant.common.OnMovieClickListener;
import me.zsj.interessant.model.Interesting;
import me.zsj.interessant.model.ItemList;
import me.zsj.interessant.rx.ErrorAction;
import me.zsj.interessant.rx.RxScroller;
import rx.functions.Action1;

/**
 * Created by zsj on 2016/10/11.
 */

public class TimeListFragment extends BaseFragment implements OnMovieClickListener {

    private static final String DATE = "date";

    private InterestingAdapter adapter;

    private List<ItemList> timeList = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final int categoryId = getArguments().getInt(MainActivity.CATEGORY_ID);

        adapter = new InterestingAdapter(timeList);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);

        loadData(categoryId, DATE);

        RxRecyclerView.scrollStateChanges(list)
                .compose(this.<Integer>bindToLifecycle())
                .compose(RxScroller.scrollTransformer(layoutManager,
                        adapter, timeList))
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer newState) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            start += 10;
                            loadData(categoryId, DATE);
                        }
                    }
                });

        adapter.setOnMovieClickListener(this);
    }

    private void loadData(int categoryId, String strategy) {
        interestingApi.getInteresting(start, categoryId, strategy)
                .compose(this.<Interesting>bindToLifecycle())
                .compose(interestingTransformer)
                .doOnNext(new Action1<List<ItemList>>() {
                    @Override
                    public void call(List<ItemList> itemLists) {
                        timeList.addAll(itemLists);
                    }
                })
                .subscribe(new Action1<List<ItemList>>() {
                    @Override
                    public void call(List<ItemList> itemLists) {
                        adapter.notifyDataSetChanged();
                    }
                }, ErrorAction.errorAction(context));
    }

    @Override
    public void onMovieClick(ItemList item, View movieAlbum) {
        IntentManager.flyToMovieDetail(context, item, movieAlbum);
    }

}
