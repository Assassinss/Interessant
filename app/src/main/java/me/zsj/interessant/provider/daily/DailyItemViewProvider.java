package me.zsj.interessant.provider.daily;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import me.drakeet.multitype.ItemViewProvider;
import me.zsj.interessant.IntentManager;
import me.zsj.interessant.R;
import me.zsj.interessant.common.Holder;
import me.zsj.interessant.model.ItemList;

/**
 * @author zsj
 */

public class DailyItemViewProvider extends
        ItemViewProvider<ItemList, Holder>  {

    private static final String VIDEO_TAG = "video";

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
            holder.movieContent.setVisibility(View.VISIBLE);
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
            holder.movieContent.setVisibility(View.GONE);
        }

        holder.movieContent.setOnClickListener(v -> fly(holder.movieAlbum, item));
    }

    private void fly(View view, ItemList item) {
        IntentManager.flyToMovieDetail(context, item, view);
    }

}
