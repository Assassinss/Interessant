package me.zsj.interessant;

/**
 * Created by zsj on 2016/10/1.
 */

public class InteressantFactory {

    private static final Object object = new Object();
    private static InteressantRetrofit retrofit;

    public static InteressantRetrofit getRetrofit() {
        synchronized (object) {
            if (retrofit == null) {
                retrofit = new InteressantRetrofit();
            }
            return retrofit;
        }
    }
}
