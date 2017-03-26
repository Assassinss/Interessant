package me.zsj.interessant.api;

import io.reactivex.Flowable;
import me.zsj.interessant.model.Related;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author zsj
 */

public interface RelatedApi {

    @GET("v3/video/{id}/detail/related")
    Flowable<Related> related(@Path("id") int id);

}
