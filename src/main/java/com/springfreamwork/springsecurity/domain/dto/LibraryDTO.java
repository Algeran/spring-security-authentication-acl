package com.springfreamwork.springsecurity.domain.dto;

public class LibraryDTO {

    private long genreCount;
    private long authorCount;
    private long bookCount;
    private long commentCount;

    public LibraryDTO(long genreCount, long authorCount, long bookCount, long commentCount) {
        this.genreCount = genreCount;
        this.authorCount = authorCount;
        this.bookCount = bookCount;
        this.commentCount = commentCount;
    }

    public long getGenreCount() {
        return genreCount;
    }

    public void setGenreCount(long genreCount) {
        this.genreCount = genreCount;
    }

    public long getAuthorCount() {
        return authorCount;
    }

    public void setAuthorCount(long authorCount) {
        this.authorCount = authorCount;
    }

    public long getBookCount() {
        return bookCount;
    }

    public void setBookCount(long bookCount) {
        this.bookCount = bookCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public String toString() {
        return "LibraryDTO{" +
                "genreCount=" + genreCount +
                ", authorCount=" + authorCount +
                ", bookCount=" + bookCount +
                ", commentCount=" + commentCount +
                '}';
    }
}
