package me.zsj.interessant;

/**
 * @author zsj
 */

public class InteressantFactory {

    private static final Object object = new Object();
    private volatile static InteressantRetrofit retrofit;

    public static InteressantRetrofit getRetrofit() {
        synchronized (object) {
            if (retrofit == null) {
                retrofit = new InteressantRetrofit();
            }
            return retrofit;
        }
    }
}
