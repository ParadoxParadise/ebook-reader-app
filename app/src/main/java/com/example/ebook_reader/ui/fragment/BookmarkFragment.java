package com.example.ebook_reader.ui.fragment;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ebook_reader.R;
import com.example.ebook_reader.data.database.AppDatabase;
import com.example.ebook_reader.ui.adapter.BookmarkAdapter;

public class BookmarkFragment extends Fragment {
    private RecyclerView recyclerView;
    private BookmarkAdapter adapter;
    private AppDatabase db;
    private int ebookId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        recyclerView = view.findViewById(R.id.bookmark_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db = AppDatabase.getInstance(getContext());
        ebookId = getArguments().getInt("ebookId");
        adapter = new BookmarkAdapter(db);
        recyclerView.setAdapter(adapter);

        db.bookmarkDao().getBookmarksForEbook(ebookId).observe(getViewLifecycleOwner(), bookmarks -> {
            adapter.setBookmarks(bookmarks);
        });

        return view;
    }
}