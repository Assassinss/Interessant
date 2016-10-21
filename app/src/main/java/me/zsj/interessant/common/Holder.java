package me.zsj.interessant.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import me.zsj.interessant.R;

/**
 * Created by zsj on 2016/10/11.
 */

public class Holder extends RecyclerView.ViewHolder {

    public ImageView movieAlbum;
    public TextView movieDesc;
    public FrameLayout movieContent;

    public Holder(View itemView) {
        super(itemView);
        movieContent = (FrameLayout) itemView.findViewById(R.id.movie_content);
        movieAlbum = (ImageView) itemView.findViewById(R.id.movie_album);
        movieDesc = (TextView) itemView.findViewById(R.id.movie_desc);
    }
}
