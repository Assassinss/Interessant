package me.zsj.interessant;

import android.app.Activity;

import me.drakeet.multitype.MultiTypeAdapter;
import me.zsj.interessant.model.Category;
import me.zsj.interessant.provider.daily.ItemList;
import me.zsj.interessant.provider.daily.CategoryItemViewProvider;
import me.zsj.interessant.provider.daily.DailyItemViewProvider;
import me.zsj.interessant.provider.related.CardItem;
import me.zsj.interessant.provider.related.CardViewProvider;
import me.zsj.interessant.provider.related.HeaderItem;
import me.zsj.interessant.provider.related.HeaderViewProvider;
import me.zsj.interessant.provider.related.RelatedHeaderItem;
import me.zsj.interessant.provider.related.RelatedHeaderViewProvider;

/**
 * @author zsj.
 */

public class Register {

    public static void registerItem(MultiTypeAdapter adapter, Activity context) {
        adapter.register(Category.class, new CategoryItemViewProvider());
        adapter.register(ItemList.class, new DailyItemViewProvider(context));
    }

    public static void registerRelatedItem(MultiTypeAdapter adapter, Activity context) {
        adapter.register(HeaderItem.class, new HeaderViewProvider());
        adapter.register(CardItem.class, new CardViewProvider(context));
        adapter.register(RelatedHeaderItem.class, new RelatedHeaderViewProvider());
    }

}
