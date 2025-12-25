package com.example.ticketbookingcinema;

import java.io.Serializable;

public class Showtime implements Serializable {
    private String id;          // ID của suất chiếu (do Firebase tự sinh)
    private String movieId;     // ID của phim (để biết suất này thuộc phim nào)
    private String cinemaName;  // Tên rạp
    private String date;        // Ngày chiếu (dd/MM/yyyy)
    private String time;        // Giờ chiếu (HH:mm)
    private String price;       // Giá vé

    public Showtime() { }

    public Showtime(String movieId, String cinemaName, String date, String time, String price) {
        this.movieId = movieId;
        this.cinemaName = cinemaName;
        this.date = date;
        this.time = time;
        this.price = price;
    }

    // Getter & Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public String getCinemaName() { return cinemaName; }
    public void setCinemaName(String cinemaName) { this.cinemaName = cinemaName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
}