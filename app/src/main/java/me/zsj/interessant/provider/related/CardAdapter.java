package me.zsj.interessant.provider.related;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import me.zsj.interessant.IntentManager;
import me.zsj.interessant.R;
import me.zsj.interessant.model.ItemList;
import me.zsj.interessant.widget.RatioImageView;

/**
 * @author zsj
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder> {

    private List<ItemList> items;
    private Activity context;

    public CardAdapter(Activity context) {
        this.context = context;
    }

    public void setItems(List<ItemList> items) {
        this.items = items;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_related_card, parent, false);
        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        ItemList item = items.get(position);

        holder.imageView.setOriginalSize(5, 3);
        Glide.with(holder.imageView.getContext())
                .load(item.data.cover.detail)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.imageView);
        holder.desc.setText(item.data.title);

        RxView.clicks(holder.content)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(aVoid -> fly(holder.imageView, item));
    }

    private void fly(View view, ItemList item) {
        IntentManager.flyToMovieDetail(context, item, view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CardHolder extends RecyclerView.ViewHolder {

        FrameLayout content;
        RatioImageView imageView;
        TextView desc;

        public CardHolder(View itemView) {
            super(itemView);
            content = (FrameLayout) itemView.findViewById(R.id.content);
            imageView = (RatioImageView) itemView.findViewById(R.id.movie_album);
            desc = (TextView) itemView.findViewById(R.id.related_title);
        }
    }
}
