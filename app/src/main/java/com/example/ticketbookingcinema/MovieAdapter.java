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

    // Hàm mới: Dùng để cập nhật danh sách khi tìm kiếm
    public void filterList(ArrayList<Movie> filteredList) {
        this.movieList = filteredList;
        notifyDataSetChanged(); // Làm mới giao diện
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
        holder.tvRating.setText(movie.getRating()); // Đảm bảo class Movie có getRating()

        // Hiển thị ảnh
        holder.imgMovie.setImageResource(movie.getImageResId());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            // Kiểm tra Android version để truyền object (code cũ của bạn đã xử lý hoặc bỏ qua warning)
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