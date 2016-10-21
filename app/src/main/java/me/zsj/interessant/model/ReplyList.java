package me.zsj.interessant.model;

/**
 * Created by zsj on 2016/10/14.
 */

public class ReplyList {

    public long id;
    public int videoId;
    public String videoTitle;
    public int parentReplyId;
    public int sequence;
    public String message;
    public String replyStatus;
    public long createTime;
    public User user;
    public int likeCount;
  
}
