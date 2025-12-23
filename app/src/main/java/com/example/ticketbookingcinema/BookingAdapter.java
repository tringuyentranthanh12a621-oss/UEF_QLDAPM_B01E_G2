package com.example.ticketbookingcinema;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView; // Nhớ import cái này
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
        holder.tvDate.setText(booking.getBookingDate() + ", " + booking.getTime());
        holder.tvCinema.setText(booking.getCinemaName());

        if (booking.getSeats() != null) {
            String seatsStr = booking.getSeats().toString().replace("[", "").replace("]", "");
            holder.tvSeats.setText("Seats: " + seatsStr);
        }

        // --- ĐOẠN CODE MỚI: XỬ LÝ HIỂN THỊ ẢNH ---
        // Mẹo: Lấy tên phim, chuyển thành chữ thường, thay dấu cách bằng dấu gạch dưới
        // Ví dụ: "The Batman" -> "the_batman"
        String imgName = booking.getMovieTitle().toLowerCase().replace(" ", "_");

        int resId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());

        if (resId > 0) {
            holder.imgPoster.setImageResource(resId);
        } else {
            // Nếu không tìm thấy ảnh thì hiện ảnh mặc định
            holder.imgPoster.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvCinema, tvSeats;
        ImageView imgPoster; // Khai báo thêm ImageView

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvHistoryTitle);
            tvDate = itemView.findViewById(R.id.tvHistoryDate);
            tvCinema = itemView.findViewById(R.id.tvHistoryCinema);
            tvSeats = itemView.findViewById(R.id.tvHistorySeats);

            // Ánh xạ ImageView (ID này nằm trong file item_booking.xml)
            imgPoster = itemView.findViewById(R.id.imgHistoryPoster);
        }
    }
}