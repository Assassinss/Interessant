package me.zsj.interessant.api;

import me.zsj.interessant.model.Replies;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author zsj
 */

public interface ReplayApi {

    @GET("v1/replies/video")
    Observable<Replies> fetchReplies(@Query("id") int id);

    @GET("v1/replies/video?num=10")
    Observable<Replies> fetchReplies(@Query("id") int id, @Query("lastId") int lastId);

}
