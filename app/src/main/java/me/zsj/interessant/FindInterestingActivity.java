package me.zsj.interessant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.zsj.interessant.api.InterestingApi;
import me.zsj.interessant.base.ToolbarActivity;
import me.zsj.interessant.model.Category;
import me.zsj.interessant.model.CategoryInfo;
import me.zsj.interessant.model.Data;
import me.zsj.interessant.model.ItemList;
import me.zsj.interessant.model.SectionList;
import me.zsj.interessant.provider.related.Card;
import me.zsj.interessant.provider.related.HeaderItem;
import me.zsj.interessant.provider.related.RelatedHeader;
import me.zsj.interessant.provider.video.FooterForward;
import me.zsj.interessant.rx.ErrorAction;

/**
 * @author zsj
 */

public class FindInterestingActivity extends ToolbarActivity {

    private static final String HORIZONTAL_SCROLL_CARD_SECTION = "horizontalScrollCardSection";
    private static final String VIDEO_LIST_SECTION = "videoListSection";
    private static final String AUTHOR_SECTION = "authorSection";

    private MultiTypeAdapter adapter;

    private Items items = new Items();
    private InterestingApi interestingApi;

    private CategoryInfo categoryInfo;
    private int id;

    @Override
    public int providerLayoutId() {
        return R.layout.find_interesting_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String title = getIntent().getStringExtra(MainActivity.TITLE);
        ab.setTitle(title.substring(1));

        id = getIntent().getExtras().getInt(MainActivity.CATEGORY_ID);

        interestingApi = RetrofitFactory.getRetrofit().createApi(InterestingApi.class);

        RecyclerView list = (RecyclerView) findViewById(R.id.list);

        adapter = new MultiTypeAdapter(items);
        list.setAdapter(adapter);

        Register.registerFindItem(adapter, this);

        findVideos();
    }

    private void findVideos() {
        interestingApi.findVideo(id)
                .filter(find -> find != null)
                .filter(find -> find.sectionList != null)
                .doOnNext(find -> this.categoryInfo = find.categoryInfo)
                .flatMap(find -> Flowable.fromIterable(find.sectionList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> adapter.notifyDataSetChanged())
                .subscribe(this::addData, ErrorAction.error(this));
    }

    private void addData(SectionList sectionList) {
        if (sectionList.type.equals(HORIZONTAL_SCROLL_CARD_SECTION)) {
            Data data = sectionList.itemList.get(0).data;
            items.add(new RelatedHeader(data.header, false));
            items.add(new Card(sectionList.itemList.get(0)));
        } else if (sectionList.type.equals(VIDEO_LIST_SECTION)) {
            items.add(new Category(sectionList.header.data.text));
            addVideo(sectionList.itemList);
            items.add(new FooterForward(categoryInfo.id, sectionList.footer.data.text));
        } else if (sectionList.type.equals(AUTHOR_SECTION)) {
            addAuthorItem(sectionList.itemList);
        }
    }

    private void addVideo(List<ItemList> itemLists) {
        for (ItemList item : itemLists) {
            items.add(item);
        }
    }

    private void addAuthorItem(List<ItemList> itemLists) {
        for (ItemList item : itemLists) {
            items.add(new HeaderItem(item.data.header, true));
            items.add(new Card(item));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.search_action) {
            toSearch(this);
        }
        return super.onOptionsItemSelected(item);
    }

}
