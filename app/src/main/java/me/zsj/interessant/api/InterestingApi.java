package me.zsj.interessant.api;

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

    @GET("v3/pcg/videos")
    Observable<Interesting> relatedHeader(
            @Query("start") int start, @Query("pcgId") int id,
            @Query("strategy") String strategy);

}
