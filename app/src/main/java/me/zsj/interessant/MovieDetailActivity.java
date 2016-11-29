package me.zsj.interessant;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import me.zsj.interessant.api.ReplayApi;
import me.zsj.interessant.model.Replies;
import me.zsj.interessant.model.ReplyList;
import me.zsj.interessant.model.ItemList;
import me.zsj.interessant.utils.CircleTransform;
import me.zsj.interessant.utils.TimeUtils;
import me.zsj.interessant.widget.FabToggle;
import me.zsj.interessant.widget.InsetDividerDecoration;
import me.zsj.interessant.widget.ParallaxScrimageView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static me.zsj.interessant.MainActivity.PROVIDER_ITEM;


/**
 * @author zsj
 */

public class MovieDetailActivity extends RxAppCompatActivity implements View.OnClickListener {

    private ReplayApi replayApi;
    private List<ReplyList> datas = new ArrayList<>();
    private ItemList item;

    private FabToggle play;
    private ReplyAdapter adapter;
    private View movieDescription;

    private int fabOffset;
    private int lastId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);
        RecyclerView replies = (RecyclerView) findViewById(R.id.recycler_replies);
        final ParallaxScrimageView backdrop = (ParallaxScrimageView) findViewById(R.id.backdrop);
        final ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(this);

        item = getIntent().getParcelableExtra(PROVIDER_ITEM);

        movieDescription = LayoutInflater.from(this)
                .inflate(R.layout.item_movie_detail_header, replies, false);
        final TextView title = (TextView) movieDescription.findViewById(R.id.movie_title);
        final TextView type = (TextView) movieDescription.findViewById(R.id.movie_type);
        final TextView description = (TextView) movieDescription.findViewById(R.id.movie_desc);
        final ImageView author = (ImageView) movieDescription.findViewById(R.id.author);
        author.setOnClickListener(this);
        final TextView name = (TextView) movieDescription.findViewById(R.id.name);
        final LinearLayout authorContent = (LinearLayout) movieDescription.findViewById(R.id.author_content);

        title.setText(item.data.title);
        type.setText(item.data.category + " | " + TimeUtils.secToTime((int) item.data.duration));
        description.setText(item.data.description);

        if (item.data.author != null) {
            authorContent.setVisibility(View.VISIBLE);
            name.setText(item.data.author.name);
            Glide.with(this)
                    .load(item.data.author.icon)
                    .transform(new CircleTransform(this))
                    .into(author);
        }

        play = (FabToggle) findViewById(R.id.fab_play);
        play.setOnClickListener(this);

        backdrop.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                backdrop.getViewTreeObserver().removeOnPreDrawListener(this);
                fabOffset = backdrop.getHeight() - play.getHeight() / 2 + title.getMeasuredHeight();
                play.setOffset(fabOffset);
                return true;
            }
        });

        Picasso.with(this)
                .load(item.data.cover.detail)
                .into(backdrop);

        replayApi = InteressantFactory.getRetrofit().createApi(ReplayApi.class);

        adapter = new ReplyAdapter(datas, movieDescription);
        replies.addItemDecoration(new InsetDividerDecoration(
                ReplyAdapter.Holder.class,
                getResources().getDimensionPixelSize(R.dimen.divider_height),
                getResources().getDimensionPixelSize(R.dimen.keyline_1),
                ContextCompat.getColor(this, R.color.divider)
        ));
        replies.setAdapter(adapter);

        final LinearLayoutManager layoutManager = (LinearLayoutManager) replies.getLayoutManager();

        replies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                backdrop.setOffset(movieDescription.getTop());
                play.setOffset(fabOffset + movieDescription.getTop());
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (layoutManager.findFirstVisibleItemPosition() != 0
                        && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (lastId != 0) loadReplies();
                }
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadReplies(true);
    }

    private void loadReplies() {
        loadReplies(false /*Load more data.*/);
    }

    private void loadReplies(boolean clean) {
        Observable<Replies> result;
        if (clean) result = replayApi.fetchReplies(item.data.id);
         else result = replayApi.fetchReplies(item.data.id, lastId);

        result.compose(this.<Replies>bindToLifecycle())
                .map(replies -> {
                    getLastId(replies.replyList);
                    return replies.replyList;
                })
                .doOnNext(replyLists -> datas.addAll(replyLists))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(replyLists -> {
                    adapter.notifyDataSetChanged();
                }, throwable -> {});
    }

    private void getLastId(List<ReplyList> replies) {
        if (replies.size() == 0) return;
        lastId = replies.get(replies.size() - 1).sequence;
    }

    @Override
    public void onBackPressed() {
        play.animate()
                .scaleX(0)
                .scaleY(0)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finishAfterTransition();
                    }
                })
                .start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finishAfterTransition();
                break;
            case R.id.fab_play:
                Intent intent = new Intent(this, PlayActivity.class);
                intent.putExtra("item", item);
                startActivity(intent);
                break;
            case R.id.author:
                Intent relatedIntent = new Intent(this, RelatedActivity.class);
                relatedIntent.putExtra(RelatedActivity.ID, item.data.id);
                startActivity(relatedIntent);
                break;
        }
    }

}
