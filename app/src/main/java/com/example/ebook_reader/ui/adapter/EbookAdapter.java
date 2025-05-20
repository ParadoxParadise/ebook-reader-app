package com.example.ebook_reader.ui.adapter;



import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.ebook_reader.R;
import com.example.ebook_reader.data.database.AppDatabase;
import com.example.ebook_reader.data.entity.Ebook;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EbookAdapter extends RecyclerView.Adapter<EbookAdapter.EbookViewHolder> {
    private List<Ebook> ebooks = new ArrayList<>();
    private AppDatabase db;
    private OnEbookClickListener listener;

    public interface OnEbookClickListener {
        void onEbookClick(Ebook ebook);
    }

    public EbookAdapter(AppDatabase db, OnEbookClickListener listener) {
        this.db = db;
        this.listener = listener;
    }

    public void setEbooks(List<Ebook> ebooks) {
        this.ebooks = ebooks;
        notifyDataSetChanged();
    }

    @Override
    public EbookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ebook, parent, false);
        return new EbookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EbookViewHolder holder, int position) {
        Ebook ebook = ebooks.get(position);
        holder.titleTextView.setText(ebook.title);
        holder.authorTextView.setText(ebook.author);
        Glide.with(holder.itemView.getContext())
                .load(ebook.coverUrl)
                .placeholder(R.drawable.placeholder)
                .into(holder.coverImageView);

        holder.itemView.setOnClickListener(v -> listener.onEbookClick(ebook));

        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Xóa Ebook")
                    .setMessage("Bạn có chắc muốn xóa " + ebook.title + "?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        db.ebookDao().delete(ebook);
                        new File(ebook.filePath).delete();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return ebooks.size();
    }

    static class EbookViewHolder extends RecyclerView.ViewHolder {
        ImageView coverImageView;
        TextView titleTextView;
        TextView authorTextView;
        ImageButton deleteButton;

        EbookViewHolder(View itemView) {
            super(itemView);
            coverImageView = itemView.findViewById(R.id.cover_image);
            titleTextView = itemView.findViewById(R.id.title_text);
            authorTextView = itemView.findViewById(R.id.author_text);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}