package com.example.ticketbookingcinema;
import java.io.Serializable;

public class Movie implements Serializable {
    String title, category, duration, rating;
    int imageResId;
    public Movie(String t, String c, String d, String r, int i) { title=t; category=c; duration=d; rating=r; imageResId=i; }
    public String getTitle() { return title; }
    public int getImageResId() { return imageResId; }
}