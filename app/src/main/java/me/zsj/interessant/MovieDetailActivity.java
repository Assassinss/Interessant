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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import me.zsj.interessant.api.ReplayApi;
import me.zsj.interessant.model.ItemList;
import me.zsj.interessant.model.Replies;
import me.zsj.interessant.model.ReplyList;
import me.zsj.interessant.utils.TimeUtils;
import me.zsj.interessant.widget.FabToggle;
import me.zsj.interessant.widget.InsetDividerDecoration;
import me.zsj.interessant.widget.ParallaxScrimageView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by zsj on 2016/10/5.
 */

public class MovieDetailActivity extends RxAppCompatActivity implements View.OnClickListener {

    public static final String PLAY_URL = "playUrl";

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

        item = getIntent().getParcelableExtra(MainActivity.PROVIDER_ITEM);

        movieDescription = LayoutInflater.from(this)
                .inflate(R.layout.item_movie_detail_header, replies, false);
        final TextView title = (TextView) movieDescription.findViewById(R.id.movie_title);
        TextView type = (TextView) movieDescription.findViewById(R.id.movie_type);
        TextView description = (TextView) movieDescription.findViewById(R.id.movie_desc);

        title.setText(item.data.title);
        type.setText(item.data.category + " | " + TimeUtils.secToTime((int) item.data.duration));
        description.setText(item.data.description);

        play = (FabToggle) findViewById(R.id.fab_play);

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

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetailActivity.this, PlayActivity.class);
                intent.putExtra("item", item);
                startActivity(intent);
            }
        });

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
                .map(new Func1<Replies, List<ReplyList>>() {
                    @Override
                    public List<ReplyList> call(Replies replies) {
                        getLastId(replies.replyList);
                        return replies.replyList;
                    }
                })
                .doOnNext(new Action1<List<ReplyList>>() {
                    @Override
                    public void call(List<ReplyList> replyLists) {
                        datas.addAll(replyLists);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ReplyList>>() {
                    @Override
                    public void call(List<ReplyList> replyLists) {
                        adapter.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {}
                });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finishAfterTransition();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        finishAfterTransition();
    }

}
