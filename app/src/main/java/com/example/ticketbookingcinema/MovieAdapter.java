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

        // --- CẬP NHẬT LOGIC HIỂN THỊ ẢNH TỪ FIREBASE ---

        // 1. Lấy tên file ảnh (ví dụ: "the_batman") từ object Movie
        String picUrl = movie.getPicUrl();

        // 2. Tìm ID của ảnh trong thư mục drawable dựa trên tên
        // getIdentifier("tên_ảnh", "kiểu_thư_mục", "tên_package")
        int drawableResourceId = holder.itemView.getContext().getResources()
                .getIdentifier(picUrl, "drawable", holder.itemView.getContext().getPackageName());

        // 3. Hiển thị ảnh
        if (drawableResourceId > 0) {
            // Nếu tìm thấy ảnh
            holder.imgMovie.setImageResource(drawableResourceId);
        } else {
            // Nếu không tìm thấy (hoặc tên ảnh trên Firebase bị sai), hiển thị ảnh mặc định
            holder.imgMovie.setImageResource(R.drawable.ic_launcher_background);
        }
        // --------------------------------------------------

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