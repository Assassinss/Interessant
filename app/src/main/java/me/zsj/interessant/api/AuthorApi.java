package me.zsj.interessant.api;

import me.zsj.interessant.model.VideoAuthor;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author zsj
 */

public interface AuthorApi {

    @GET("v4/pgcs/all?num=10")
    Observable<VideoAuthor> authors(@Query("start") int start);

}
