package com.example.ticketbookingcinema;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdminMovieAdapter extends RecyclerView.Adapter<AdminMovieAdapter.AdminMovieViewHolder> {

    Context context;
    List<Movie> movieList;

    public AdminMovieAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public AdminMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new AdminMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminMovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.tvTitle.setText(movie.getTitle());
        holder.tvRating.setText(movie.getRating());

        // --- XỬ LÝ ẢNH (AN TOÀN) ---
        String picUrl = movie.getPicUrl();
        int drawableResourceId = 0;

        if (picUrl != null && !picUrl.isEmpty()) {
            try {
                drawableResourceId = holder.itemView.getContext().getResources()
                        .getIdentifier(picUrl, "drawable", holder.itemView.getContext().getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (drawableResourceId > 0) {
            holder.imgMovie.setImageResource(drawableResourceId);
        } else {
            holder.imgMovie.setImageResource(R.drawable.ic_launcher_background);
        }
        // -----------------------------

        // 1. NHẤN THƯỜNG -> SỬA PHIM
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditMovieActivity.class);
            intent.putExtra("movieId", movie.getId());
            intent.putExtra("movieData", movie);
            context.startActivity(intent);
        });

        // 2. NHẤN GIỮ (LONG CLICK) -> THÊM SUẤT CHIẾU (SCHEDULE)
        holder.itemView.setOnLongClickListener(v -> {
            // Chuyển sang màn hình ManageShowtimeActivity
            // Lưu ý: Bạn cần chắc chắn đã tạo file ManageShowtimeActivity như hướng dẫn trước
            try {
                Intent intent = new Intent(context, ManageShowtimeActivity.class);
                intent.putExtra("movieId", movie.getId());
                intent.putExtra("movieTitle", movie.getTitle());
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "Chưa tạo ManageShowtimeActivity!", Toast.LENGTH_SHORT).show();
            }
            return true; // Trả về true để hệ thống biết sự kiện đã được xử lý
        });
    }

    @Override
    public int getItemCount() { return movieList.size(); }

    public static class AdminMovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvRating;
        ImageView imgMovie;

        public AdminMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvRating = itemView.findViewById(R.id.tvRating);
            imgMovie = itemView.findViewById(R.id.imgMovie);
        }
    }
}