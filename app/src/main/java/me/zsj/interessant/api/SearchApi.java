package me.zsj.interessant.api;

import java.util.List;

import me.zsj.interessant.model.SearchResult;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author zsj
 */

public interface SearchApi {

    @GET("v3/queries/hot")
    Observable<List<String>> getTrendingTag();

    @GET("v1/search?num=10")
    Observable<SearchResult> query(@Query("query") String key, @Query("start") int start);

}
