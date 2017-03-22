package me.zsj.interessant;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

/**
 * @author zsj
 */

public class App extends Application {

    private HttpProxyCacheServer cacheServer;


    public static HttpProxyCacheServer cacheServer(Context context) {
        App app = (App) context.getApplicationContext();
        return app.cacheServer == null ? app.cacheServer = app.newCacheServer() : app.cacheServer;
    }

    private HttpProxyCacheServer newCacheServer() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 * 1024)       // 1 Gb for cache
                .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
