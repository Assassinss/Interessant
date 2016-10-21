package me.zsj.interessant.model;

import android.support.annotation.NonNull;

import me.drakeet.multitype.Item;

/**
 * Created by zsj on 2016/10/2.
 */

public class Category implements Item {

    public String date;

    public Category(@NonNull String date) {
        this.date = date;
    }
}
