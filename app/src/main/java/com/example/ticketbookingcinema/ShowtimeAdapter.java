package com.example.ticketbookingcinema;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ViewHolder> {

    private List<Showtime> list;
    private OnShowtimeSelectListener listener;
    private int selectedPos = -1; // Biến lưu vị trí đang được chọn

    // Interface để gửi sự kiện click ra bên ngoài
    public interface OnShowtimeSelectListener {
        void onSelect(Showtime showtime);
    }

    public ShowtimeAdapter(List<Showtime> list, OnShowtimeSelectListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Layout item_showtime phải tồn tại (đã tạo ở bước trước)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_showtime, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Showtime item = list.get(position);

        // Hiển thị dữ liệu
        holder.tvTime.setText(item.getTime());
        holder.tvCinema.setText(item.getCinemaName());
        holder.tvPrice.setText(item.getPrice() + " ₸"); // Thêm đơn vị tiền tệ nếu muốn

        // Xử lý đổi màu khi được chọn
        if (selectedPos == position) {
            // Background viền cam (hoặc màu bạn muốn khi chọn)
            holder.itemView.setBackgroundResource(R.drawable.bg_session_item_selector);
            holder.tvTime.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.orange_main));
        } else {
            // Background mặc định
            holder.itemView.setBackgroundResource(R.drawable.bg_input_field);
            holder.tvTime.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        }

        // Sự kiện Click
        holder.itemView.setOnClickListener(v -> {
            // Cập nhật vị trí chọn
            int previousPos = selectedPos;
            selectedPos = holder.getAdapterPosition();

            // Load lại giao diện để đổi màu
            notifyItemChanged(previousPos);
            notifyItemChanged(selectedPos);

            // Gửi dữ liệu ra DetailActivity
            listener.onSelect(item);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvCinema, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ View từ layout item_showtime.xml
            tvTime = itemView.findViewById(R.id.tvStTime);
            tvCinema = itemView.findViewById(R.id.tvStCinema);
            tvPrice = itemView.findViewById(R.id.tvStPrice);
        }
    }
}