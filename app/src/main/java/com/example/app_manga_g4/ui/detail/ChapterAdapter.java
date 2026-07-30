package com.example.app_manga_g4.ui.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_manga_g4.R;
import com.example.app_manga_g4.data.model.Chapter;

import java.util.ArrayList;
import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    public interface OnChapterClickListener {
        void onChapterClick(Chapter chapter);
    }

    private List<Chapter> chapterList = new ArrayList<>();
    private final OnChapterClickListener listener;

    public ChapterAdapter(OnChapterClickListener listener) {
        this.listener = listener;
    }

    public void setChapterList(List<Chapter> list) {
        this.chapterList = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);
        holder.tvTitle.setText(chapter.getTitle());
        holder.tvDate.setText(chapter.getCreatedAt());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onChapterClick(chapter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvChapterTitle);
            tvDate = itemView.findViewById(R.id.tvChapterDate);
        }
    }
}
