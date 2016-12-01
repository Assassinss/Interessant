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
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import me.drakeet.multitype.ItemViewProvider;
import me.zsj.interessant.FindInterestingActivity;
import me.zsj.interessant.R;
import me.zsj.interessant.interesting.InterestingActivity;
import me.zsj.interessant.model.Header;
import me.zsj.interessant.utils.CircleTransform;
import me.zsj.interessant.utils.IDUtils;

import static me.zsj.interessant.MainActivity.CATEGORY_ID;
import static me.zsj.interessant.MainActivity.TITLE;

/**
 * @author zsj
 */

public class HeaderViewProvider extends ItemViewProvider<HeaderItem, HeaderViewProvider.HeaderHolder> {

    @NonNull @Override
    protected HeaderHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_related_header, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull HeaderHolder holder, @NonNull HeaderItem headerItem) {
        Header header = headerItem.header;

        Context context = holder.avatar.getContext();
        Glide.with(context)
                .load(header.icon)
                .transform(new CircleTransform(context))
                .into(holder.avatar);

        holder.relatedTitle.setText(header.title);

        holder.relatedDesc.setText(header.description);

        RxView.clicks(holder.content)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> toInteresting(holder.content.getContext(), header));
    }

    private void toInteresting(Context context, Header header) {
        if (IDUtils.isDetermined(header.id)) {
            Intent findIntent = new Intent(context, FindInterestingActivity.class);
            findIntent.putExtra(CATEGORY_ID, header.id);
            findIntent.putExtra(TITLE, header.title);
            context.startActivity(findIntent);
        } else {
            Intent interestingIntent = new Intent(context, InterestingActivity.class);
            interestingIntent.putExtra(CATEGORY_ID, header.id);
            interestingIntent.putExtra(TITLE, header.title);
            interestingIntent.putExtra(InterestingActivity.RELATED_HEADER_VIDEO, true);
            context.startActivity(interestingIntent);
        }
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {

        RelativeLayout content;
        ImageView avatar;
        TextView relatedTitle;
        TextView relatedDesc;

        public HeaderHolder(View itemView) {
            super(itemView);
            content = (RelativeLayout) itemView.findViewById(R.id.header_content);
            avatar = (ImageView) itemView.findViewById(R.id.movie_avatar);
            relatedTitle = (TextView) itemView.findViewById(R.id.related_title);
            relatedDesc = (TextView) itemView.findViewById(R.id.related_desc);
        }
    }
}
