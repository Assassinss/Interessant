package me.zsj.interessant.api;

import me.zsj.interessant.model.Find;
import me.zsj.interessant.model.Interesting;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zsj on 2016/10/10.
 */

public interface InterestingApi {

    @GET("v3/videos?num=10")
    Observable<Interesting> getInteresting(
            @Query("start") int start, @Query("categoryId") int categoryId,
            @Query("strategy") String strategy);

    @GET("v3/tag/videos")
    Observable<Interesting> related(
            @Query("start") int start, @Query("tagId") int id,
            @Query("strategy") String strategy);

    @GET("v3/pgc/videos")
    Observable<Interesting> relatedHeader(
            @Query("start") int start, @Query("pgcId") int id,
            @Query("strategy") String strategy);

    @GET("v3/categories/detail")
    Observable<Find> findVideo(@Query("id") int id);

    @GET("v3/categories/videoList")
    Observable<Interesting> videoList(@Query("id") int id, @Query("start") int start,
                                      @Query("strategy") String strategy);

}
