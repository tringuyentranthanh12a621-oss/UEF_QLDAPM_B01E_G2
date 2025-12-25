package com.example.ticketbookingcinema;

import java.util.List;

public class Booking {
    private String movieTitle;
    private String cinemaName;
    private String bookingDate;
    private String time;
    private int totalPrice;
    private List<String> seats;
    private String picUrl; // <-- THÊM MỚI

    public Booking() { }

    public Booking(String movieTitle, String cinemaName, String bookingDate, String time, int totalPrice, List<String> seats, String picUrl) {
        this.movieTitle = movieTitle;
        this.cinemaName = cinemaName;
        this.bookingDate = bookingDate;
        this.time = time;
        this.totalPrice = totalPrice;
        this.seats = seats;
        this.picUrl = picUrl; // <-- THÊM MỚI
    }

    // Getters
    public String getMovieTitle() { return movieTitle; }
    public String getCinemaName() { return cinemaName; }
    public String getBookingDate() { return bookingDate; }
    public String getTime() { return time; }
    public int getTotalPrice() { return totalPrice; }
    public List<String> getSeats() { return seats; }
    public String getPicUrl() { return picUrl; } // <-- THÊM MỚI
}