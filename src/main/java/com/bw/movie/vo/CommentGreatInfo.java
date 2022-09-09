package com.bw.movie.vo;

import javax.ws.rs.FormParam;
import java.io.Serializable;

public class CommentGreatInfo implements Serializable {

    //评论id
    @FormParam("commentId")
    private int commentId;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

}
