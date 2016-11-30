package me.zsj.interessant.provider.video;

import me.drakeet.multitype.Item;

/**
 * @author zsj
 */

public class FooterForwardItem implements Item {

    public int id;
    public String text;

    public FooterForwardItem(int id, String text) {
        this.id = id;
        this.text = text;
    }

}
