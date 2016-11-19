package me.zsj.interessant;

import android.app.Activity;

import me.drakeet.multitype.MultiTypeAdapter;
import me.zsj.interessant.model.Category;
import me.zsj.interessant.model.ItemList;
import me.zsj.interessant.provider.CategoryItemViewProvider;
import me.zsj.interessant.provider.DailyItemViewProvider;

/**
 * @author zsj.
 */

public class Register {

    public static void registerItem(MultiTypeAdapter adapter, Activity context) {
        adapter.register(Category.class, new CategoryItemViewProvider());
        adapter.register(ItemList.class, new DailyItemViewProvider(context));
    }

}
