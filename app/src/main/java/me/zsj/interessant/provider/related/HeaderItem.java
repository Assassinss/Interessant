package me.zsj.interessant.provider.related;

import android.support.annotation.NonNull;

import me.zsj.interessant.model.Data;
import me.zsj.interessant.model.Header;

/**
 * @author zsj
 */

public class HeaderItem {

    public Header header;
    public Data data;
    public boolean isShowArrowIcon;

    public HeaderItem(@NonNull Header header, boolean isShowArrowIcon) {
        this.header = header;
        this.isShowArrowIcon = isShowArrowIcon;
    }

    public HeaderItem(@NonNull Data data, Header header, boolean isShowArrowIcon) {
        this.data = data;
        this.header = header;
        this.isShowArrowIcon = isShowArrowIcon;
    }

}
