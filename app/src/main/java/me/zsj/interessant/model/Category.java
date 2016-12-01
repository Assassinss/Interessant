package me.zsj.interessant.model;

import android.support.annotation.NonNull;

import me.drakeet.multitype.Item;

/**
 * @author zsj
 */

public class Category implements Item {

    public String categoryTitle;

    public Category(@NonNull String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
}
