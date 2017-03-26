package me.zsj.interessant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.zsj.interessant.api.AuthorApi;
import me.zsj.interessant.base.ToolbarActivity;
import me.zsj.interessant.model.Category;
import me.zsj.interessant.model.ItemList;
import me.zsj.interessant.provider.related.Card;
import me.zsj.interessant.provider.related.HeaderItem;

/**
 * @author zsj
 */

public class VideoAuthorActivity extends ToolbarActivity {

    private static final String LEFT_TEXT_HEADER = "leftAlignTextHeader";
    private static final String BRIEF_CARD = "briefCard";
    private static final String VIDEO_COLLECT_BRIEF = "videoCollectionWithBrief";

    private MultiTypeAdapter adapter;

    private Items items = new Items();
    private AuthorApi authorApi;

    private int start = 0;

    @Override
    public int providerLayoutId() {
        return R.layout.video_author_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ab.setTitle("Author");

        RecyclerView list = (RecyclerView) findViewById(R.id.author_list);

        adapter = new MultiTypeAdapter(items);

        Register.registerAuthorItem(adapter, this);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);

        RxRecyclerView.scrollStateChanges(list)
                .compose(bindToLifecycle())
                .subscribe(newState -> {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        start += 10;
                        loadData();
                    }
                });

        authorApi = RetrofitFactory.getRetrofit().createApi(AuthorApi.class);

        loadData();
    }

    private void loadData() {
        authorApi.authors(start)
                .filter(videoAuthor -> videoAuthor != null)
                .flatMap(videoAuthor -> Flowable.fromIterable(videoAuthor.itemList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> adapter.notifyDataSetChanged())
                .subscribe(this::addData, Throwable::printStackTrace);
    }

    private void addData(ItemList item) {
        if (item.type.equals(LEFT_TEXT_HEADER)) {
            items.add(new Category(item.data.text));
        } else if (item.type.equals(BRIEF_CARD)) {
            items.add(new HeaderItem(item.data, null, false));
        } else if (item.type.equals(VIDEO_COLLECT_BRIEF)) {
            items.add(new HeaderItem(item.data.header, false));
            items.add(new Card(item));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
