package me.zsj.interessant.api;

import io.reactivex.Flowable;
import me.zsj.interessant.model.Find;
import me.zsj.interessant.model.Interesting;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author zsj
 */

public interface InterestingApi {

    @GET("v3/videos?num=10")
    Flowable<Interesting> getInteresting(
            @Query("start") int start, @Query("categoryId") int categoryId,
            @Query("strategy") String strategy);

    @GET("v3/tag/videos")
    Flowable<Interesting> related(
            @Query("start") int start, @Query("tagId") int id,
            @Query("strategy") String strategy);

    @GET("v3/pgc/videos")
    Flowable<Interesting> relatedHeader(
            @Query("start") int start, @Query("pgcId") int id,
            @Query("strategy") String strategy);

    @GET("v3/categories/detail")
    Flowable<Find> findVideo(@Query("id") int id);

    @GET("v3/categories/videoList")
    Flowable<Interesting> videoList(@Query("id") int id, @Query("start") int start,
                                      @Query("strategy") String strategy);

}
