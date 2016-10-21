package me.zsj.interessant.interesting;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import me.zsj.interessant.R;
import me.zsj.interessant.common.Holder;
import me.zsj.interessant.common.OnMovieClickListener;
import me.zsj.interessant.model.ItemList;
import rx.functions.Action1;

/**
 * Created by zsj on 2016/10/11.
 */

public class InterestingAdapter extends RecyclerView.Adapter<Holder> {

    private static final int TOUCH_TIME = 1000;

    private List<ItemList> itemList;

    private OnMovieClickListener onMovieClickListener;

    public void setOnMovieClickListener(OnMovieClickListener onMovieClickListener) {
        this.onMovieClickListener = onMovieClickListener;
    }

    public InterestingAdapter(List<ItemList> itemLists) {
        this.itemList = itemLists;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final ItemList item = itemList.get(position);

        Glide.with(holder.movieAlbum.getContext())
                .load(item.data.cover.detail)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.movieAlbum);
        holder.movieDesc.setText(item.data.title);

        if (onMovieClickListener != null) {
            RxView.clicks(holder.movieContent).throttleFirst(TOUCH_TIME, TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            onMovieClickListener.onMovieClick(item, holder.movieAlbum);
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
