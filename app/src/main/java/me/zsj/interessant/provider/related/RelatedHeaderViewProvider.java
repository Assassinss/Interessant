package me.zsj.interessant.provider.related;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import me.drakeet.multitype.ItemViewProvider;
import me.zsj.interessant.R;
import me.zsj.interessant.interesting.InterestingActivity;
import me.zsj.interessant.model.Header;

import static me.zsj.interessant.MainActivity.CATEGORY_ID;

/**
 * @author zsj
 */

public class RelatedHeaderViewProvider extends
        ItemViewProvider<RelatedHeaderItem, RelatedHeaderViewProvider.RelatedHeaderHolder> {


    @NonNull @Override
    protected RelatedHeaderHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_category_header, parent, false);
        return new RelatedHeaderHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull RelatedHeaderHolder holder,
                                    @NonNull RelatedHeaderItem relatedHeaderItem) {
        Header header = relatedHeaderItem.header;

        holder.categoryTitle.setText(header.title);

        holder.content.setOnClickListener(view ->
                toInteresting(holder.content.getContext(), header.id));
    }

    private void toInteresting(Context context, int id) {
        Intent intent = new Intent(context, InterestingActivity.class);
        intent.putExtra(CATEGORY_ID, id);
        intent.putExtra(InterestingActivity.RELATED_VIDEO, true);
        context.startActivity(intent);
    }

    static class RelatedHeaderHolder extends RecyclerView.ViewHolder {

        FrameLayout content;
        TextView categoryTitle;

        public RelatedHeaderHolder(View itemView) {
            super(itemView);
            content = (FrameLayout) itemView.findViewById(R.id.category_content);
            categoryTitle = (TextView) itemView.findViewById(R.id.category_title);
        }
    }
}
