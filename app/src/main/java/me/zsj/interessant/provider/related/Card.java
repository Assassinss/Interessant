package me.zsj.interessant.provider.related;

import android.support.annotation.NonNull;

import me.zsj.interessant.model.ItemList;

/**
 * @author zsj
 */

public class Card {

    public ItemList item;

    public Card(@NonNull ItemList item) {
        this.item = item;
    }

}
