package me.zsj.interessant.provider.related;

import android.support.annotation.NonNull;

import me.drakeet.multitype.Item;
import me.zsj.interessant.model.Header;

/**
 * @author zsj
 */

public class RelatedHeaderItem implements Item {

    public Header header;

    public RelatedHeaderItem(@NonNull Header header) {
        this.header = header;
    }

}
