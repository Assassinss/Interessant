package me.zsj.interessant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zsj
 */

public class Cover implements Parcelable {

    public String feed;
    public String detail;
    public String blurred;

    protected Cover(Parcel in) {
        feed = in.readString();
        detail = in.readString();
        blurred = in.readString();
    }

    public static final Creator<Cover> CREATOR = new Creator<Cover>() {
        @Override
        public Cover createFromParcel(Parcel in) {
            return new Cover(in);
        }

        @Override
        public Cover[] newArray(int size) {
            return new Cover[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(feed);
        dest.writeString(detail);
        dest.writeString(blurred);
    }
}