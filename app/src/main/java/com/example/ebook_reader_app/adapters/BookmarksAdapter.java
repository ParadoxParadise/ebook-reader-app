package com.example.ebook_reader_app.adapters;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.booksreading.R;
import com.example.booksreading.models.Bookmark;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {
    private Bookmark[] bookmarks;
    private OnBookmarkClickListener listener;

    public interface OnBookmarkClickListener {
        void onBookmarkClick(Bookmark bookmark);
    }

    public BookmarksAdapter(Bookmark[] bookmarks, OnBookmarkClickListener listener) {
        this.bookmarks = bookmarks;
        this.listener = listener;
    }

    @Override.connect
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookmark, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bookmark bookmark = bookmarks[position];
        holder.textView.setText(bookmark.getHighlightedText());
        holder.itemView.setOnClickListener(v -> listener.onBookmarkClick(bookmark));
    }

    @Override
    public int getItemCount() {
        return bookmarks.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.bookmark_text);
        }
    }
}