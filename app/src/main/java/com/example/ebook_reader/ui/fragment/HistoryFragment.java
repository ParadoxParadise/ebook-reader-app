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
import com.example.ebook_reader.ui.adapter.HistoryAdapter;

public class HistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private AppDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = view.findViewById(R.id.history_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db = AppDatabase.getInstance(getContext());
        adapter = new HistoryAdapter(db);
        recyclerView.setAdapter(adapter);

        db.readingHistoryDao().getReadingHistory().observe(getViewLifecycleOwner(), histories -> {
            adapter.setHistories(histories);
        });

        return view;
    }
}
//public class HistoryFragment extends Fragment {
//    private RecyclerView recyclerView;
//    private HistoryAdapter adapter;
//    private AppDatabase db;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_history, container, false);
//
//        recyclerView = view.findViewById(R.id.history_recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        db = AppDatabase.getInstance(getContext());
//        adapter = new HistoryAdapter(db);
//        recyclerView.setAdapter(adapter);
//
//        db.readingHistoryDao().getReadingHistory().observe(getViewLifecycleOwner(), histories -> {
//            adapter.setHistories(histories);
//        });
//
//        return view;
//    }
//}
