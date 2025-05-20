package com.example.ebook_reader.ui.adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ebook_reader.R;
import com.example.ebook_reader.data.database.AppDatabase;
import com.example.ebook_reader.data.entity.Bookmark;
import java.util.ArrayList;
import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {
    private List<Bookmark> bookmarks = new ArrayList<>();
    private AppDatabase db;

    public BookmarkAdapter(AppDatabase db) {
        this.db = db;
    }

    public void setBookmarks(List<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
        notifyDataSetChanged();
    }

    @Override
    public BookmarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bookmark, parent, false);
        return new BookmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookmarkViewHolder holder, int position) {
        Bookmark bookmark = bookmarks.get(position);
        holder.pageTextView.setText("Page " + bookmark.page);
        holder.noteTextView.setText(bookmark.note);
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }

    static class BookmarkViewHolder extends RecyclerView.ViewHolder {
        TextView pageTextView;
        TextView noteTextView;

        BookmarkViewHolder(View itemView) {
            super(itemView);
            pageTextView = itemView.findViewById(R.id.page_text);
            noteTextView = itemView.findViewById(R.id.note_text);
        }
    }
}