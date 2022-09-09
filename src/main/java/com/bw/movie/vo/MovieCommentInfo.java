package com.bw.movie.vo;

import javax.ws.rs.FormParam;
import java.io.Serializable;

public class MovieCommentInfo implements Serializable {
    @FormParam("movieId")
    private int movieId;
    @FormParam("commentContent")
    private String commentContent;
    @FormParam("score")
    private double score;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
