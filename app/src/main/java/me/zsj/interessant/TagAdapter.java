package me.zsj.interessant;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * @author zsj
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.Holder> {

    private List<String> tags;
    private OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public TagAdapter(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tag, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.tag.setText(tags.get(position));

        holder.tag.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView tag;

        public Holder(View itemView) {
            super(itemView);
            tag = (TextView) itemView.findViewById(R.id.tag);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
