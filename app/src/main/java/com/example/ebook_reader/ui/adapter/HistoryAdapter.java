package com.example.ebook_reader.ui.adapter;



import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ebook_reader.R;
import com.example.ebook_reader.data.database.AppDatabase;
import com.example.ebook_reader.data.entity.Ebook;
import com.example.ebook_reader.data.entity.ReadingHistory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.os.Bundle;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private List<ReadingHistory> histories = new ArrayList<>();
    private AppDatabase db;

    public HistoryAdapter(AppDatabase db) {
        this.db = db;
    }

    public void setHistories(List<ReadingHistory> histories) {
        this.histories = histories;
        notifyDataSetChanged();
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        ReadingHistory history = histories.get(position);
        Ebook ebook = db.ebookDao().getEbookById(history.ebookId);
        if (ebook != null) {
            holder.titleTextView.setText(ebook.title);
            holder.authorTextView.setText(ebook.author);
            holder.timeTextView.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date(history.lastReadTime)));
            holder.progressTextView.setText(String.format(Locale.getDefault(), "Page %d", history.lastPage));

            holder.itemView.setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putInt("ebookId", ebook.id);
                Navigation.findNavController(v).navigate(R.id.action_history_to_reading, args);
            });

            holder.deleteButton.setOnClickListener(v -> {
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Xóa Lịch Sử")
                        .setMessage("Bạn có chắc muốn xóa lịch sử đọc của " + ebook.title + "?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            db.readingHistoryDao().delete(history);
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView authorTextView;
        TextView timeTextView;
        TextView progressTextView;
        ImageButton deleteButton;

        HistoryViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text);
            authorTextView = itemView.findViewById(R.id.author_text);
            timeTextView = itemView.findViewById(R.id.time_text);
            progressTextView = itemView.findViewById(R.id.progress_text);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}