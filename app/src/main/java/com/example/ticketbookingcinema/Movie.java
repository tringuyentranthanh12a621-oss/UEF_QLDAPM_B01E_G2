package com.example.ticketbookingcinema;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import java.io.Serializable;
import java.util.Locale; // Import để kiểm tra ngôn ngữ máy

@IgnoreExtraProperties
public class Movie implements Serializable {

    @Exclude
    private String id;

    private String title;
    private String duration;
    private String rating;

    // --- KHAI BÁO CÁC BIẾN MÔ TẢ ĐA NGÔN NGỮ ---
    private String description;    // Tiếng Anh (Mặc định)
    private String description_vi; // Tiếng Việt
    private String description_ru; // Tiếng Nga

    private String category;
    private String picUrl; // Giữ nguyên, không đụng vào

    public Movie() { }

    public Movie(String title, String category, String duration, String rating, String description, String picUrl) {
        this.title = title;
        this.category = category;
        this.duration = duration;
        this.rating = rating;
        this.description = description;
        this.picUrl = picUrl;
    }

    // --- LOGIC THÔNG MINH: Tự động trả về mô tả theo ngôn ngữ máy ---
    public String getDescription() {
        String lang = Locale.getDefault().getLanguage(); // Lấy ngôn ngữ máy (vi, ru, en...)

        // Nếu máy là Tiếng Việt và có dữ liệu mô tả tiếng Việt -> Trả về tiếng Việt
        if (lang.equals("vi") && description_vi != null && !description_vi.isEmpty()) {
            return description_vi;
        }

        // Nếu máy là Tiếng Nga và có dữ liệu mô tả tiếng Nga -> Trả về tiếng Nga
        if (lang.equals("ru") && description_ru != null && !description_ru.isEmpty()) {
            return description_ru;
        }

        // Mặc định trả về Tiếng Anh
        return description;
    }

    // --- CÁC SETTER/GETTER CHO ADMIN (Để lưu và sửa dữ liệu) ---
    public void setDescription(String description) { this.description = description; }

    // Getter/Setter cụ thể cho Tiếng Việt (để Admin nhập)
    public String getDescription_vi() { return description_vi; }
    public void setDescription_vi(String description_vi) { this.description_vi = description_vi; }

    // Getter/Setter cụ thể cho Tiếng Nga (để Admin nhập)
    public String getDescription_ru() { return description_ru; }
    public void setDescription_ru(String description_ru) { this.description_ru = description_ru; }


    // --- CÁC PHẦN KHÁC GIỮ NGUYÊN (BAO GỒM ẢNH) ---
    @Exclude
    public String getId() { return id; }
    @Exclude
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getPicUrl() { return picUrl; }
    public void setPicUrl(String picUrl) { this.picUrl = picUrl; }
}