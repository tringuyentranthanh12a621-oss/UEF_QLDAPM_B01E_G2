package com.example.ticketbookingcinema;

import com.google.firebase.firestore.Exclude; // Cần import này
import java.io.Serializable;

public class Movie implements Serializable {
    // Thêm trường ID (Dùng @Exclude để khi lưu lên Firebase không bị lưu thừa field này vào data)
    @Exclude
    private String id;

    private String title;
    private String category;
    private String duration;
    private String rating;
    private String description;
    private String picUrl;

    public Movie() { }

    public Movie(String title, String category, String duration, String rating, String description, String picUrl) {
        this.title = title;
        this.category = category;
        this.duration = duration;
        this.rating = rating;
        this.description = description;
        this.picUrl = picUrl;
    }

    // --- Getter & Setter cho ID ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // ... Các Getter/Setter cũ giữ nguyên ...
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPicUrl() { return picUrl; }
    public void setPicUrl(String picUrl) { this.picUrl = picUrl; }
}