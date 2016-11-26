package me.zsj.interessant;

/**
 * @author zsj
 */

public class API {

    public static final String DAILY = "v2/feed?num=2";

    public static final String MORE = "v2/categories";

    public static final String TRENDING = "v3/ranklist?num=10&strategy=%s";

    public static final String DETAIL = "v3/videos?categoryName=%s&strategy=%s";

    public static final String INTERESTING = "/v3/videos?num=10";

    public static final String REPLIES = "http://baobab.wandoujia.com/api/v1/replies/video?id=9962";

    public static final String QUERIES = "http://baobab.wandoujia.com/api/v3/queries/hot";

    public static final String QUERY = "http://baobab.wandoujia.com/api/v1/search?query=Android";

    public static final String TABS = "http://baobab.kaiyanapp.com/api/v3/tabs/pgcs?udid=dc47bc64bc04470cb3537addda22998825c217d5";

    public static final String TABS_ITEM = "http://baobab.kaiyanapp.com/api/v3/pgc/videos?pgcId=56&strategy=date";

    /**
     * strategy : monthly, weekly, historical
     */
    public static final String HOTS = "http://baobab.kaiyanapp.com/api/v3/ranklist?num=10&strategy=monthly";

    public static final String MOVIE_RELATED = "http://baobab.kaiyanapp.com/api/v3/video/10056/detail/related";

    //http://baobab.kaiyanapp.com/api/v3/tag/videos?tagId=24&strategy=shareCount

    //http://baobab.kaiyanapp.com/api/v3/pgc/videos?pgcId=2&strategy=date
}
