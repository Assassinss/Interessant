package me.zsj.interessant;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.zsj.interessant.model.ReplyList;
import me.zsj.interessant.utils.CircleTransform;

/**
 * Created by zsj on 2016/10/14.
 */

public class ReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int DETAIL_DESCRIPTION_TYPE = 0;

    private View description;
    private List<ReplyList> datas;


    public ReplyAdapter(List<ReplyList> data, View description) {
        datas = data;
        this.description = description;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.layout.item_movie_detail_header:
                return new SimpleViewHolder(description);
            default:
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_reply, parent, false);
                return new Holder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) != R.layout.item_movie_detail_header) {
            ReplyList reply = datas.get(position);
            Holder viewHolder = (Holder) holder;
            Glide.with(viewHolder.avatar.getContext())
                    .load(reply.user.avatar)
                    .transform(new CircleTransform(viewHolder.avatar.getContext()))
                    .into(viewHolder.avatar);

            viewHolder.replyAvatar.setText(reply.user.nickname);
            viewHolder.replyDesc.setText(reply.message);
            viewHolder.replyTime.setText(DateUtils.getRelativeTimeSpanString(reply.createTime,
                    System.currentTimeMillis(),  DateUtils.SECOND_IN_MILLIS).toString().toLowerCase());
            viewHolder.like.setText(String.valueOf(reply.likeCount));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == DETAIL_DESCRIPTION_TYPE) {
            return R.layout.item_movie_detail_header;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView replyAvatar;
        TextView replyDesc;
        TextView replyTime;
        TextView like;

        public Holder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.movie_avatar);
            replyAvatar = (TextView) itemView.findViewById(R.id.reply_avatar);
            replyDesc = (TextView) itemView.findViewById(R.id.replay_desc);
            replyTime = (TextView) itemView.findViewById(R.id.reply_time);
            like = (TextView) itemView.findViewById(R.id.likeCount);
        }
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder {

        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }

}
