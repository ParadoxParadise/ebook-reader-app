package com.example.ebook_reader.ui.fragment;

//package com.example.ebookreader.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.bumptech.glide.Glide;
import com.example.ebook_reader.R;
import com.example.ebook_reader.data.database.AppDatabase;
import com.example.ebook_reader.data.entity.Ebook;

public class EbookDetailFragment extends Fragment {
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView genreTextView;
    private TextView formatTextView;
    private ProgressBar progressBar;
    private ImageView coverImageView;
    private Button readButton;
    private Button favoriteButton;
    private AppDatabase db;
    private Ebook ebook;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ebook_detail, container, false);

        titleTextView = view.findViewById(R.id.title_text);
        authorTextView = view.findViewById(R.id.author_text);
        genreTextView = view.findViewById(R.id.genre_text);
        formatTextView = view.findViewById(R.id.format_text);
        progressBar = view.findViewById(R.id.progress_bar);
        coverImageView = view.findViewById(R.id.cover_image);
        readButton = view.findViewById(R.id.read_button);
        favoriteButton = view.findViewById(R.id.favorite_button);
        db = AppDatabase.getInstance(getContext());

        int ebookId = getArguments().getInt("ebookId");
        ebook = db.ebookDao().getEbookById(ebookId);
        if (ebook != null) {
            titleTextView.setText(ebook.title);
            authorTextView.setText(ebook.author);
            genreTextView.setText(ebook.genre);
            formatTextView.setText(ebook.format);
            progressBar.setProgress((int) ebook.readingProgress);
            Glide.with(this).load(ebook.coverUrl).placeholder(R.drawable.placeholder).into(coverImageView);
            favoriteButton.setText(ebook.isFavorite ? "Bỏ Yêu Thích" : "Yêu Thích");

            readButton.setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putInt("ebookId", ebook.id);
                Navigation.findNavController(v).navigate(R.id.action_detail_to_reading, args);
            });

            favoriteButton.setOnClickListener(v -> {
                ebook.isFavorite = !ebook.isFavorite;
                db.ebookDao().update(ebook);
                favoriteButton.setText(ebook.isFavorite ? "Bỏ Yêu Thích" : "Yêu Thích");
            });
        }

        return view;
    }
}