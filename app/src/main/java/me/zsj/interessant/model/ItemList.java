package me.zsj.interessant.model;

import android.os.Parcel;
import android.os.Parcelable;

import me.drakeet.multitype.Item;

/**
 * Created by zsj on 2016/10/11.
 */

public class ItemList implements Item, Parcelable {

    public String type;
    public Data data;

    protected ItemList(Parcel in) {
        type = in.readString();
        data = in.readParcelable(Data.class.getClassLoader());
    }

    public static final Creator<ItemList> CREATOR = new Creator<ItemList>() {
        @Override
        public ItemList createFromParcel(Parcel in) {
            return new ItemList(in);
        }

        @Override
        public ItemList[] newArray(int size) {
            return new ItemList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeParcelable(data, flags);
    }

}