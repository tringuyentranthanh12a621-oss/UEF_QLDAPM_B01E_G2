package com.example.ticketbookingcinema;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    List<Movie> movieList;
    Context context;

    public MovieAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    public void filterList(ArrayList<Movie> filteredList) {
        this.movieList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvRating.setText(movie.getRating());

        // --- BẮT ĐẦU ĐOẠN CODE SỬA LỖI (Quan trọng) ---
        String picUrl = movie.getPicUrl();
        int drawableResourceId = 0;

        // 1. Kiểm tra kỹ: Chỉ tìm ảnh nếu picUrl KHÔNG null và KHÔNG rỗng
        if (picUrl != null && !picUrl.isEmpty()) {
            try {
                drawableResourceId = holder.itemView.getContext().getResources()
                        .getIdentifier(picUrl, "drawable", holder.itemView.getContext().getPackageName());
            } catch (Exception e) {
                e.printStackTrace(); // Nếu tên ảnh sai cú pháp, bỏ qua lỗi chứ không crash
            }
        }

        // 2. Hiển thị ảnh
        if (drawableResourceId > 0) {
            holder.imgMovie.setImageResource(drawableResourceId);
        } else {
            // Nếu picUrl bị null hoặc không tìm thấy ảnh -> Hiện ảnh mặc định
            holder.imgMovie.setImageResource(R.drawable.ic_launcher_background);
        }
        // --- KẾT THÚC ĐOẠN CODE SỬA LỖI ---

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", movie);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return movieList.size(); }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvRating;
        ImageView imgMovie;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvRating = itemView.findViewById(R.id.tvRating);
            imgMovie = itemView.findViewById(R.id.imgMovie);
        }
    }
}