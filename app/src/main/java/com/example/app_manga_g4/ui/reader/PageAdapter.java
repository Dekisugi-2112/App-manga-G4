package com.example.app_manga_g4.ui.reader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_manga_g4.R;
import com.example.app_manga_g4.data.model.Page;

import java.util.ArrayList;
import java.util.List;

// Adapter hiển thị danh sách ảnh từng trang tranh bằng Glide
public class PageAdapter extends RecyclerView.Adapter<PageAdapter.PageViewHolder> {

    private List<Page> pageList = new ArrayList<>();

    public void setPageList(List<Page> list) {
        this.pageList = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page, parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        Page page = pageList.get(position);

        // Glide nạp ảnh từ Supabase Storage URL vào ImageView
        Glide.with(holder.itemView.getContext())
                .load(page.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_dialog_alert)
                .into(holder.imgPage);
    }

    @Override
    public int getItemCount() {
        return pageList.size();
    }

    static class PageViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPage;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPage = itemView.findViewById(R.id.imgPage);
        }
    }
}
