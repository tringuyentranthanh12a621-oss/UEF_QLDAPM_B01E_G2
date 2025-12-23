package com.example.ticketbookingcinema;

import java.util.List;

public class Booking {
    private String movieTitle;
    private String cinemaName;
    private String bookingDate; // Tên trường trên Firestore là "bookingDate"
    private String time;
    private int totalPrice;
    private List<String> seats;

    public Booking() { } // Constructor rỗng bắt buộc cho Firebase

    public Booking(String movieTitle, String cinemaName, String bookingDate, String time, int totalPrice, List<String> seats) {
        this.movieTitle = movieTitle;
        this.cinemaName = cinemaName;
        this.bookingDate = bookingDate;
        this.time = time;
        this.totalPrice = totalPrice;
        this.seats = seats;
    }

    // Getter
    public String getMovieTitle() { return movieTitle; }
    public String getCinemaName() { return cinemaName; }
    public String getBookingDate() { return bookingDate; }
    public String getTime() { return time; }
    public int getTotalPrice() { return totalPrice; }
    public List<String> getSeats() { return seats; }
}