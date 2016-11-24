package me.zsj.interessant.api;

import me.zsj.interessant.model.Daily;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author zsj
 */

public interface DailyApi {

    @GET("v2/feed?num=2")
    Observable<Daily> getDaily(@Query("date") long date);

    @GET("v2/feed?num=2")
    Observable<Daily> getDaily();

}
