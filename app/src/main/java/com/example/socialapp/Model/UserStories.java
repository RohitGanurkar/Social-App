package com.example.socialapp.Model;

public class UserStories {
    String imageUrl;
    long storyAt;

    public UserStories() {
    }

    public UserStories(String imageUrl, long storyAt) {
        this.imageUrl = imageUrl;
        this.storyAt = storyAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getStoryAt() {
        return storyAt;
    }

    public void setStoryAt(long storyAt) {
        this.storyAt = storyAt;
    }
}
