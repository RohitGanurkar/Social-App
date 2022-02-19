package com.example.socialapp.Model;

public class DashBoardModel {
    int profileImg, postImg, saveImg;
    String name, about, like, comment, share;

    public DashBoardModel(int profileImg, int postImg, int saveImg, String name, String about, String like, String comment, String share) {
        this.profileImg = profileImg;
        this.postImg = postImg;
        this.saveImg = saveImg;
        this.name = name;
        this.about = about;
        this.like = like;
        this.comment = comment;
        this.share = share;
    }

    public int getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(int profileImg) {
        this.profileImg = profileImg;
    }

    public int getPostImg() {
        return postImg;
    }

    public void setPostImg(int postImg) {
        this.postImg = postImg;
    }

    public int getSaveImg() {
        return saveImg;
    }

    public void setSaveImg(int saveImg) {
        this.saveImg = saveImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }
}
