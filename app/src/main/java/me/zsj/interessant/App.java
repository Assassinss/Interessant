package me.zsj.interessant;

import android.app.Application;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import me.drakeet.multitype.ItemViewProvider;
import me.drakeet.multitype.MultiTypePool;
import me.zsj.interessant.model.Category;
import me.zsj.interessant.model.ItemList;
import me.zsj.interessant.provider.CategoryItemViewProvider;
import me.zsj.interessant.provider.DailyItemViewProvider;

/**
 * Created by zsj on 2016/10/2.
 */

public class App extends Application {

    private static Map<String, ItemViewProvider> providers = new HashMap<>();
    private static final String PROVIDER_ITEM = "item";

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        DailyItemViewProvider dailyItemViewProvider = new DailyItemViewProvider();
        MultiTypePool.register(Category.class, new CategoryItemViewProvider());
        MultiTypePool.register(ItemList.class, dailyItemViewProvider);
        providers.put(PROVIDER_ITEM, dailyItemViewProvider);
    }

    public static ItemViewProvider getProvider(String key) {
        return providers.get(key);
    }
}
