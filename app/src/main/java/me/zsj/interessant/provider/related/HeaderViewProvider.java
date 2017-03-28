package me.zsj.interessant.provider.related;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import me.drakeet.multitype.ItemViewProvider;
import me.zsj.interessant.R;
import me.zsj.interessant.interesting.InterestingActivity;
import me.zsj.interessant.utils.CircleTransform;

import static me.zsj.interessant.MainActivity.CATEGORY_ID;
import static me.zsj.interessant.MainActivity.TITLE;

/**
 * @author zsj
 */

public class HeaderViewProvider extends
        ItemViewProvider<HeaderItem, HeaderViewProvider.HeaderHolder> {

    @NonNull @Override
    protected HeaderHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_related_header, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull HeaderHolder holder, @NonNull HeaderItem headerItem) {
        String icon;
        String title;
        String description;
        int id;
        if (headerItem.header == null) {
            id = headerItem.data.id;
            icon = headerItem.data.icon;
            title = headerItem.data.title;
            description = headerItem.data.description;
        } else {
            id = headerItem.header.id;
            icon = headerItem.header.icon;
            title = headerItem.header.title;
            description = headerItem.header.description;
        }

        Context context = holder.avatar.getContext();
        Glide.with(context)
                .load(icon)
                .transform(new CircleTransform(context))
                .into(holder.avatar);

        holder.relatedTitle.setText(title);

        holder.relatedDesc.setText(description);

        if (headerItem.isShowArrowIcon) holder.arrowRight.setVisibility(View.VISIBLE);
        else holder.arrowRight.setVisibility(View.GONE);

        RxView.clicks(holder.content)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(aVoid -> toInteresting(context, id, title));
    }

    private void toInteresting(Context context, int id, String title) {
        Intent interestingIntent = new Intent(context, InterestingActivity.class);
        interestingIntent.putExtra(CATEGORY_ID, id);
        interestingIntent.putExtra(TITLE, title);
        interestingIntent.putExtra(InterestingActivity.RELATED_HEADER_VIDEO, true);
        context.startActivity(interestingIntent);
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {

        RelativeLayout content;
        ImageView avatar;
        TextView relatedTitle;
        TextView relatedDesc;
        ImageView arrowRight;

        public HeaderHolder(View itemView) {
            super(itemView);
            content = (RelativeLayout) itemView.findViewById(R.id.header_content);
            avatar = (ImageView) itemView.findViewById(R.id.movie_avatar);
            relatedTitle = (TextView) itemView.findViewById(R.id.related_title);
            relatedDesc = (TextView) itemView.findViewById(R.id.related_desc);
            arrowRight = (ImageView) itemView.findViewById(R.id.arrow_right);
        }
    }
}
