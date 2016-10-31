package me.zsj.interessant.provider;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import me.drakeet.multitype.ItemViewProvider;
import me.zsj.interessant.R;
import me.zsj.interessant.common.Holder;
import me.zsj.interessant.common.OnMovieClickListener;
import me.zsj.interessant.model.ItemList;
import rx.functions.Action1;

/**
 * Created by zsj on 2016/10/2.
 */

public class DailyItemViewProvider extends
        ItemViewProvider<ItemList, Holder>  {

    private static final String VIDEO_TAG = "video";
    private static final String TEXTHEADER_TAG = "textHeader";
    private static final int TOUCH_TIME = 1000;

    private OnMovieClickListener onMovieClickListener;

    public void setOnMovieClickListener(OnMovieClickListener onMovieClickListener) {
        this.onMovieClickListener = onMovieClickListener;
    }

    @NonNull @Override
    protected Holder onCreateViewHolder(@NonNull LayoutInflater inflater,
                                        @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_movie, parent, false);
        return new Holder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final Holder holder,
                                    @NonNull final ItemList item) {

        if (item.type.contains(VIDEO_TAG)) {
            holder.movieAlbum.setVisibility(View.VISIBLE);
            holder.movieDesc.setVisibility(View.VISIBLE);
            holder.movieAlbum.setOriginalSize(16, 9);
            Glide.with(holder.movieAlbum.getContext())
                    .load(item.data.cover.detail)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.movieAlbum);
            holder.movieDesc.setText(item.data.title);
        } else {
            holder.movieAlbum.setVisibility(View.GONE);
            holder.movieDesc.setVisibility(View.GONE);
        }

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

}
