package me.zsj.interessant.rx;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zsj on 2016/10/11.
 */

public class RxScroller {

    private RxScroller() {
        throw new AssertionError("No instances.");
    }

    public static <T>Observable.Transformer<Integer, Integer> scrollTransformer(
            final LinearLayoutManager layoutManager, final RecyclerView.Adapter adapter,
            final List<T> data) {
        return integerObservable -> integerObservable
                .filter(integer -> layoutManager.findFirstCompletelyVisibleItemPosition() >=
                            adapter.getItemCount() - 4)
                .filter(integer -> data.size() != 0)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return layoutManager.findFirstVisibleItemPosition() != 0;
                    }
                });
    }
}
