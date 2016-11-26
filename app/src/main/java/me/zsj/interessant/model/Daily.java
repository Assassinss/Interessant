package me.zsj.interessant.model;

import java.util.List;

import me.zsj.interessant.provider.daily.ItemList;

/**
 * Created by zsj on 2016/10/1.
 */

public class Daily {

    public String nextPageUrl;
    public List<IssueList> issueList;

    public static class IssueList {
        public long releaseTime;
        public String type;
        public long date;
        public long publishTime;
        public int count;
        public List<ItemList> itemList;
    }
}
