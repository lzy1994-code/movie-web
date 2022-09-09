package com.bw.movie.vo;

import javax.ws.rs.FormParam;
import java.io.Serializable;

public class CinemaCommentInfo implements Serializable {
    @FormParam("cinemaId")
    private int cinemaId;
    @FormParam("commentContent")
    private String commentContent;

    public int getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(int cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
