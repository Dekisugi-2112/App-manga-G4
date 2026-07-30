package com.example.app_manga_g4.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_manga_g4.R;
import com.example.app_manga_g4.data.model.Comic;

import java.util.ArrayList;
import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder> {

    public interface OnComicClickListener {
        void onComicClick(Comic comic);
    }

    private List<Comic> comicList = new ArrayList<>();
    private final OnComicClickListener listener;

    public ComicAdapter(OnComicClickListener listener) {
        this.listener = listener;
    }

    public void setComicList(List<Comic> list) {
        this.comicList = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comic, parent, false);
        return new ComicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {
        Comic comic = comicList.get(position);
        holder.tvTitle.setText(comic.getTitle());
        holder.tvAuthor.setText(comic.getAuthor());

        Glide.with(holder.itemView.getContext())
                .load(comic.getCoverUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_dialog_alert)
                .into(holder.imgCover);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onComicClick(comic);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    static class ComicViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover;
        TextView tvTitle, tvAuthor;

        public ComicViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.imgCover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
        }
    }
}
