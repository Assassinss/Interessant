package me.zsj.interessant.provider.daily;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import me.drakeet.multitype.ItemViewProvider;
import me.zsj.interessant.IntentManager;
import me.zsj.interessant.R;
import me.zsj.interessant.common.Holder;

/**
 * Created by zsj on 2016/10/2.
 */

public class DailyItemViewProvider extends
        ItemViewProvider<ItemList, Holder>  {

    private static final String VIDEO_TAG = "video";
    private static final int TOUCH_TIME = 1000;

    private Activity context;

    public DailyItemViewProvider(Activity context) {
        this.context = context;
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

            if (item.data.author != null) {
                holder.tag.setVisibility(View.VISIBLE);
                holder.tag.setText(item.data.author.name);
            } else {
                holder.tag.setVisibility(View.GONE);
            }

        } else {
            holder.movieAlbum.setVisibility(View.GONE);
            holder.movieDesc.setVisibility(View.GONE);
        }

        RxView.clicks(holder.movieContent).throttleFirst(TOUCH_TIME, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> {
                    fly(holder.movieAlbum, item);
                });
    }

    private void fly(View view, ItemList item) {
        IntentManager.flyToMovieDetail(context, item, view);
    }

}
