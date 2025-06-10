package learn.noodemy.model;

import java.time.LocalDate;

public class Comment {

    private int commentId;
    private String comment;
    private LocalDate createdAt;
    private int enrollmentId;
    private String postedBy;

    public Comment() {}
    public Comment(int commentId, String comment, LocalDate createdAt, int enrollmentId) {
        this.commentId = commentId;
        this.comment = comment;
        this.createdAt = createdAt;
        this.enrollmentId = enrollmentId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String username) {
        this.postedBy = username;
    }
}
