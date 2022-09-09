package com.bw.movie.vo;

import javax.ws.rs.FormParam;
import java.io.Serializable;

public class MovieCommentReplyInfo implements Serializable {
    @FormParam("commentId")
    private int commentId;
    @FormParam("replyContent")
    private String replyContent;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

}
