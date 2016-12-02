package me.zsj.interessant.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import me.zsj.interessant.R;
import me.zsj.interessant.widget.RatioImageView;

/**
 * @author zsj
 */

public class Holder extends RecyclerView.ViewHolder {

    public View itemView;
    public RatioImageView movieAlbum;
    public TextView movieDesc;
    public FrameLayout movieContent;
    public TextView tag;

    public Holder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        movieContent = (FrameLayout) itemView.findViewById(R.id.movie_content);
        movieAlbum = (RatioImageView) itemView.findViewById(R.id.movie_album);
        movieDesc = (TextView) itemView.findViewById(R.id.movie_desc);
        tag = (TextView) itemView.findViewById(R.id.author_tag);
    }
}
