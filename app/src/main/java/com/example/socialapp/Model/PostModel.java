package com.example.socialapp.Model;

public class PostModel {
    String postId;
    String postedBy;
    String postDescription;
    String postImgUrl;
    long postedAt;

    public PostModel() {
    }

    public PostModel(String postId, String postedBy, String postDescription, String postImgUrl, long postedAt) {
        this.postId = postId;
        this.postedBy = postedBy;
        this.postDescription = postDescription;
        this.postImgUrl = postImgUrl;
        this.postedAt = postedAt;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostImgUrl() {
        return postImgUrl;
    }

    public void setPostImgUrl(String postImgUrl) {
        this.postImgUrl = postImgUrl;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }
}
