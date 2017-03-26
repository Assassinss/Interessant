package me.zsj.interessant.rx;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.reactivex.ObservableTransformer;

/**
 * @author zsj
 */

public class RxScroller {

    private RxScroller() {
        throw new AssertionError("No instances.");
    }

    public static <T> ObservableTransformer<Integer, Integer> scrollTransformer(
            final LinearLayoutManager layoutManager, final RecyclerView.Adapter adapter,
            final List<T> data) {
        return integerObservable -> integerObservable
                .filter(integer -> layoutManager.findFirstCompletelyVisibleItemPosition() >=
                            adapter.getItemCount() - 4)
                .filter(integer -> data.size() != 0)
                .filter(integer -> layoutManager.findFirstVisibleItemPosition() != 0);
    }

}
