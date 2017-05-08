package me.zsj.interessant;

import android.app.Activity;

import me.drakeet.multitype.MultiTypeAdapter;
import me.zsj.interessant.model.Category;
import me.zsj.interessant.model.ItemList;
import me.zsj.interessant.binder.daily.CategoryViewBinder;
import me.zsj.interessant.binder.daily.DailyViewBinder;
import me.zsj.interessant.binder.related.Card;
import me.zsj.interessant.binder.related.CardViewBinder;
import me.zsj.interessant.binder.related.HeaderItem;
import me.zsj.interessant.binder.related.HeaderViewBinder;
import me.zsj.interessant.binder.related.RelatedHeader;
import me.zsj.interessant.binder.related.RelatedHeaderViewBinder;
import me.zsj.interessant.binder.video.FooterForward;
import me.zsj.interessant.binder.video.FooterForwardViewBinder;
import me.zsj.interessant.binder.video.VideoViewBinder;

/**
 * @author zsj.
 */

public class Register {

    public static void registerItem(MultiTypeAdapter adapter, Activity context) {
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(ItemList.class, new DailyViewBinder(context));
    }

    public static void registerRelatedItem(MultiTypeAdapter adapter, Activity context) {
        registerCommonItem(adapter, context);
    }

    public static void registerFindItem(MultiTypeAdapter adapter, Activity context) {
        adapter.register(FooterForward.class, new FooterForwardViewBinder());
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(ItemList.class, new VideoViewBinder(context));
        registerCommonItem(adapter, context);
    }

    public static void registerAuthorItem(MultiTypeAdapter adapter, Activity context) {
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(ItemList.class, new VideoViewBinder(context));
        registerCommonItem(adapter, context);
    }

    private static void registerCommonItem(MultiTypeAdapter adapter, Activity context) {
        adapter.register(HeaderItem.class, new HeaderViewBinder());
        adapter.register(Card.class, new CardViewBinder(context));
        adapter.register(RelatedHeader.class, new RelatedHeaderViewBinder());
    }

}
