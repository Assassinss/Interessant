package me.zsj.interessant.api;

import me.zsj.interessant.model.Daily;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zsj on 2016/10/1.
 */

public interface DailyApi {

    @GET("v2/feed?num=2")
    Observable<Daily> getDaily(@Query("date") long date);

    @GET("v2/feed?num=2")
    Observable<Daily> getDaily();

}
