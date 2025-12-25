package com.example.ticketbookingcinema;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookingList;

    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        Context context = holder.itemView.getContext();

        holder.tvTitle.setText(booking.getMovieTitle());
        holder.tvCinema.setText(booking.getCinemaName());
        holder.tvDate.setText(booking.getBookingDate() + " - " + booking.getTime());

        if (booking.getSeats() != null) {
            String seatsStr = booking.getSeats().toString().replace("[", "").replace("]", "");
            holder.tvSeats.setText(context.getString(R.string.seats) + ": " + seatsStr);
        }

        // --- BẮT ĐẦU ĐOẠN SỬA LỖI ẢNH ---
        // 1. Ưu tiên lấy từ picUrl lưu trong Booking
        String picName = booking.getPicUrl();

        // 2. Nếu trong Booking chưa lưu picUrl (do vé cũ), thử lấy từ Tên Phim
        if (picName == null || picName.isEmpty()) {
            // Chuyển "Spider Man" thành "spider_man"
            if (booking.getMovieTitle() != null) {
                picName = booking.getMovieTitle().toLowerCase().trim().replace(" ", "_");
            }
        }

        int drawableResourceId = 0;
        if (picName != null && !picName.isEmpty()) {
            try {
                drawableResourceId = context.getResources()
                        .getIdentifier(picName, "drawable", context.getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (drawableResourceId > 0) {
            holder.imgPoster.setImageResource(drawableResourceId);
        } else {
            holder.imgPoster.setImageResource(R.drawable.ic_launcher_background); // Ảnh mặc định
        }
        // --- KẾT THÚC ĐOẠN SỬA LỖI ---
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvCinema, tvSeats;
        ImageView imgPoster;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvHistoryTitle);
            tvDate = itemView.findViewById(R.id.tvHistoryDate);
            tvCinema = itemView.findViewById(R.id.tvHistoryCinema);
            tvSeats = itemView.findViewById(R.id.tvHistorySeats);
            imgPoster = itemView.findViewById(R.id.imgHistoryPoster);
        }
    }
}