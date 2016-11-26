package me.zsj.interessant.api;

import me.zsj.interessant.model.Related;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author zsj
 */

public interface RelatedApi {

    @GET("v3/video/{id}/detail/related")
    Observable<Related> related(@Path("id") int id);

}
