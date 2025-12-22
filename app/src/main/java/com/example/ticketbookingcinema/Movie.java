package com.example.ticketbookingcinema;

import java.io.Serializable;

public class Movie implements Serializable {
    // Các thuộc tính của phim
    private String title;
    private String category;
    private String duration;
    private String rating;      // Đây là biến chứa điểm số (Ví dụ: "8.3")
    private int imageResId;     // ID của ảnh (R.drawable.xxx)

    // Constructor (Hàm khởi tạo)
    public Movie(String title, String category, String duration, String rating, int imageResId) {
        this.title = title;
        this.category = category;
        this.duration = duration;
        this.rating = rating;
        this.imageResId = imageResId;
    }

    // --- CÁC HÀM GETTER (Để lấy dữ liệu ra) ---

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDuration() {
        return duration;
    }

    // ĐÂY LÀ HÀM BẠN ĐANG THIẾU
    public String getRating() {
        return rating;
    }

    public int getImageResId() {
        return imageResId;
    }

    // --- CÁC HÀM SETTER (Nếu cần sửa dữ liệu sau này) ---
    public void setTitle(String title) {
        this.title = title;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}