package me.zsj.interessant;

/**
 * @author zsj
 */

public class RetrofitFactory {

    private static final Object object = new Object();
    private volatile static WorkerRetrofit retrofit;

    public static WorkerRetrofit getRetrofit() {
        synchronized (object) {
            if (retrofit == null) {
                retrofit = new WorkerRetrofit();
            }
            return retrofit;
        }
    }
}
