package me.zsj.interessant.provider.related;

import android.support.annotation.NonNull;

import me.drakeet.multitype.Item;
import me.zsj.interessant.provider.daily.ItemList;

/**
 * @author zsj
 */

public class CardItem implements Item {

    public ItemList item;

    public CardItem(@NonNull ItemList item) {
        this.item = item;
    }

}
