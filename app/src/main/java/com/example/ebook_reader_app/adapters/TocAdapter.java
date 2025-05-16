package com.example.ebook_reader_app.adapters;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.booksreading.R;

public class TocAdapter extends RecyclerView.Adapter<TocAdapter.ViewHolder> {
    private String[] chapters;
    private OnChapterClickListener listener;

    public interface OnChapterClickListener {
        void onChapterClick(int position);
    }

    public TocAdapter(String[] chapters, OnChapterClickListener listener) {
        this.chapters = chapters;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_toc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.chapterTitle.setText(chapters[position]);
        holder.itemView.setOnClickListener(v -> listener.onChapterClick(position));
    }

    @Override
    public int getItemCount() {
        return chapters.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView chapterTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            chapterTitle = itemView.findViewById(R.id.chapter_title);
        }
    }
}