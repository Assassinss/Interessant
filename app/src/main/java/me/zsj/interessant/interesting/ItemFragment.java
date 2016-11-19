package me.zsj.interessant.interesting;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.List;

import me.zsj.interessant.InteressantFactory;
import me.zsj.interessant.R;
import me.zsj.interessant.api.InterestingApi;
import me.zsj.interessant.model.Interesting;
import me.zsj.interessant.model.ItemList;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zsj on 2016/10/11.
 */

public class ItemFragment extends RxFragment {

    RecyclerView list;
    LinearLayoutManager layoutManager;

    InterestingApi interestingApi;
    int start = 0;
    Activity context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (Activity) context;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.interesting_list_fragment, container, false);
        list = (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        layoutManager = new LinearLayoutManager(context);
        interestingApi = InteressantFactory.getRetrofit().createApi(InterestingApi.class);
    }

    Observable.Transformer<Interesting, List<ItemList>>
            interestingTransformer = interestingObservable -> interestingObservable
                    .filter(interesting -> interesting != null)
                    .map(interesting -> interesting.itemList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

}
