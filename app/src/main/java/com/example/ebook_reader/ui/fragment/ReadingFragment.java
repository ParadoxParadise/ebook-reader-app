package com.example.ebook_reader.ui.fragment;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ebook_reader.R;
import com.example.ebook_reader.data.database.AppDatabase;
import com.example.ebook_reader.data.entity.Bookmark;
import com.example.ebook_reader.data.entity.Ebook;
import com.example.ebook_reader.data.entity.ReadingHistory;
import com.example.ebook_reader.ui.adapter.PdfPageAdapter;
import java.io.File;

////public class ReadingFragment extends Fragment {
////    private RecyclerView pdfRecyclerView;
////    private SharedPreferences prefs;
////    private AppDatabase db;
////    private Ebook ebook;
////    private PdfRenderer pdfRenderer;
////    private PdfPageAdapter pdfPageAdapter;
////    private static final String ARG_EBOOK_ID = "ebookId";
////
////    public static ReadingFragment newInstance(int ebookId) {
////        ReadingFragment fragment = new ReadingFragment();
////        Bundle args = new Bundle();
////        args.putInt(ARG_EBOOK_ID, ebookId);
////        fragment.setArguments(args);
////        return fragment;
////    }
////
////    @Override
////    public void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setHasOptionsMenu(true);
////        db = AppDatabase.getInstance(getContext());
////    }
////
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
////        View view = inflater.inflate(R.layout.fragment_reading, container, false);
////        pdfRecyclerView = view.findViewById(R.id.pdf_recycler_view);
////        prefs = getContext().getSharedPreferences("reading_settings", Context.MODE_PRIVATE);
////
////        int ebookId = getArguments().getInt(ARG_EBOOK_ID);
////        ebook = db.ebookDao().getEbookById(ebookId);
////        if (ebook != null) {
////            setupPdfRenderer();
////            applyReadingSettings();
////            setupRecyclerView();
////            restoreReadingProgress();
////        }
////
////        return view;
////    }
////
////    @Override
////    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
////        inflater.inflate(R.menu.reading_menu, menu);
////        super.onCreateOptionsMenu(menu, inflater);
////    }
////
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////        if (item.getItemId() == R.id.action_settings) {
////            new ReadingSettingsFragment().show(getChildFragmentManager(), "ReadingSettings");
////            return true;
////        } else if (item.getItemId() == R.id.action_bookmark) {
////            Bundle args = new Bundle();
////            args.putInt("ebookId", ebook.id);
////            Navigation.findNavController(getView()).navigate(R.id.action_reading_to_bookmark, args);
////            return true;
////        } else if (item.getItemId() == R.id.action_note) {
////            showNoteDialog();
////            return true;
////        }
////        return super.onOptionsItemSelected(item);
////    }
////
////    @Override
////    public void onPause() {
////        super.onPause();
////        saveReadingProgress();
////    }
////
////    @Override
////    public void onDestroyView() {
////        if (pdfRenderer != null) {
////            pdfRenderer.close();
////        }
////        super.onDestroyView();
////    }
////
////    private void setupPdfRenderer() {
////        try {
////            File file = new File(ebook.filePath);
////            ParcelFileDescriptor descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
////            pdfRenderer = new PdfRenderer(descriptor);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
////
////    private void setupRecyclerView() {
////        pdfRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
////        pdfPageAdapter = new PdfPageAdapter(pdfRenderer);
////        pdfRecyclerView.setAdapter(pdfPageAdapter);
////    }
////
////    private void applyReadingSettings() {
////        int bgColorIndex = prefs.getInt("background_color", 0);
////        String bgColor = bgColorIndex == 1 ? "#121212" : "#FFFFFF";
////        pdfRecyclerView.setBackgroundColor(Color.parseColor(bgColor));
////    }
////
////    private void restoreReadingProgress() {
////        ReadingHistory history = db.readingHistoryDao().getHistoryForEbook(ebook.id);
////        if (history != null && pdfRecyclerView.getLayoutManager() != null) {
////            pdfRecyclerView.scrollToPosition(history.lastPage);
////        }
////    }
////
////    private void saveReadingProgress() {
////        LinearLayoutManager layoutManager = (LinearLayoutManager) pdfRecyclerView.getLayoutManager();
////        if (layoutManager != null) {
////            int currentPageIndex = layoutManager.findFirstVisibleItemPosition();
////            ebook.readingProgress = (currentPageIndex * 100f / pdfRenderer.getPageCount());
////            db.ebookDao().update(ebook);
////
////            ReadingHistory history = new ReadingHistory();
////            history.ebookId = ebook.id;
////            history.lastReadTime = System.currentTimeMillis();
////            history.lastPage = currentPageIndex;
////            db.readingHistoryDao().insert(history);
////        }
////    }
////
////    private void showNoteDialog() {
////        LinearLayoutManager layoutManager = (LinearLayoutManager) pdfRecyclerView.getLayoutManager();
////        if (layoutManager == null) return;
////        int currentPageIndex = layoutManager.findFirstVisibleItemPosition();
////
////        EditText noteInput = new EditText(getContext());
////        noteInput.setHint("Ghi chú");
////        new AlertDialog.Builder(getContext())
////                .setTitle("Thêm Ghi Chú")
////                .setView(noteInput)
////                .setPositiveButton("Lưu", (dialog, which) -> {
////                    Bookmark bookmark = new Bookmark();
////                    bookmark.ebookId = ebook.id;
////                    bookmark.page = currentPageIndex;
////                    bookmark.note = noteInput.getText().toString();
////                    bookmark.timestamp = System.currentTimeMillis();
////                    db.bookmarkDao().insert(bookmark);
////                })
////                .setNegativeButton("Hủy", null)
////                .show();
////    }
////}
//
//
public class ReadingFragment extends Fragment {
    private RecyclerView pdfRecyclerView;
    private SharedPreferences prefs;
    private AppDatabase db;
    private Ebook ebook;
    private PdfRenderer pdfRenderer;
    private PdfPageAdapter pdfPageAdapter;
    private static final String ARG_EBOOK_ID = "ebookId";

    public static ReadingFragment newInstance(int ebookId) {
        ReadingFragment fragment = new ReadingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_EBOOK_ID, ebookId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        db = AppDatabase.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reading, container, false);
        pdfRecyclerView = view.findViewById(R.id.pdf_recycler_view);
        prefs = getContext().getSharedPreferences("reading_settings", Context.MODE_PRIVATE);

        int ebookId = getArguments().getInt(ARG_EBOOK_ID);
        ebook = db.ebookDao().getEbookById(ebookId);
        if (ebook != null) {
            setupPdfRenderer();
            applyReadingSettings();
            setupRecyclerView();
            restoreReadingProgress();
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.reading_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            new ReadingSettingsFragment().show(getChildFragmentManager(), "ReadingSettings");
            return true;
        } else if (item.getItemId() == R.id.action_bookmark) {
            Bundle args = new Bundle();
            args.putInt("ebookId", ebook.id);
            Navigation.findNavController(getView()).navigate(R.id.action_reading_to_bookmark, args);
            return true;
        } else if (item.getItemId() == R.id.action_note) {
            showNoteDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveReadingProgress();
    }

    @Override
    public void onDestroyView() {
        if (pdfRenderer != null) {
            pdfRenderer.close();
        }
        super.onDestroyView();
    }

    private void setupPdfRenderer() {
        try {
            File file = new File(ebook.filePath);
            ParcelFileDescriptor descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(descriptor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupRecyclerView() {
        pdfRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pdfPageAdapter = new PdfPageAdapter(pdfRenderer);
        pdfRecyclerView.setAdapter(pdfPageAdapter);
    }

    private void applyReadingSettings() {
        int bgColorIndex = prefs.getInt("background_color", 0);
        String bgColor = bgColorIndex == 1 ? "#121212" : "#FFFFFF";
        pdfRecyclerView.setBackgroundColor(Color.parseColor(bgColor));
    }

    private void restoreReadingProgress() {
        ReadingHistory history = db.readingHistoryDao().getHistoryForEbook(ebook.id);
        if (history != null && pdfRecyclerView.getLayoutManager() != null) {
            pdfRecyclerView.scrollToPosition(history.lastPage);
        }
    }

    private void saveReadingProgress() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) pdfRecyclerView.getLayoutManager();
        if (layoutManager != null) {
            int currentPageIndex = layoutManager.findFirstVisibleItemPosition();
            ebook.readingProgress = (currentPageIndex * 100f / pdfRenderer.getPageCount());
            db.ebookDao().update(ebook);

            // Kiểm tra xem đã có bản ghi lịch sử đọc cho ebook này chưa
            ReadingHistory existingHistory = db.readingHistoryDao().getHistoryForEbook(ebook.id);
            if (existingHistory != null) {
                // Nếu đã có, cập nhật bản ghi hiện có
                existingHistory.lastReadTime = System.currentTimeMillis();
                existingHistory.lastPage = currentPageIndex;
                final ReadingHistory historyToUpdate = existingHistory;
                new Thread(() -> {
                    db.readingHistoryDao().update(historyToUpdate);
                }).start();
            } else {
                // Nếu chưa có, tạo bản ghi mới
                final ReadingHistory newHistory = new ReadingHistory();
                newHistory.ebookId = ebook.id;
                newHistory.lastReadTime = System.currentTimeMillis();
                newHistory.lastPage = currentPageIndex;
                new Thread(() -> {
                    db.readingHistoryDao().insert(newHistory);
                }).start();
            }
        }
    }

    private void showNoteDialog() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) pdfRecyclerView.getLayoutManager();
        if (layoutManager == null) return;
        int currentPageIndex = layoutManager.findFirstVisibleItemPosition();

        EditText noteInput = new EditText(getContext());
        noteInput.setHint("Ghi chú");
        new AlertDialog.Builder(getContext())
                .setTitle("Thêm Ghi Chú")
                .setView(noteInput)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    Bookmark bookmark = new Bookmark();
                    bookmark.ebookId = ebook.id;
                    bookmark.page = currentPageIndex;
                    bookmark.note = noteInput.getText().toString();
                    bookmark.timestamp = System.currentTimeMillis();
                    db.bookmarkDao().insert(bookmark);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}

//package com.example.ebook_reader.ui.fragment;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.graphics.pdf.PdfRenderer;
//import android.os.Bundle;
//import android.os.ParcelFileDescriptor;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.WebView;
//import android.widget.EditText;
//import androidx.appcompat.app.AlertDialog;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.Navigation;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.ebook_reader.R;
//import com.example.ebook_reader.data.database.AppDatabase;
//import com.example.ebook_reader.data.entity.Bookmark;
//import com.example.ebook_reader.data.entity.Ebook;
//import com.example.ebook_reader.data.entity.ReadingHistory;
//import com.example.ebook_reader.ui.adapter.PdfPageAdapter;
//import com.example.ebook_reader.util.EpubUtils;
//import java.io.File;
//
//public class ReadingFragment extends Fragment {
//    private RecyclerView pdfRecyclerView;
//    private WebView epubWebView;
//    private SharedPreferences prefs;
//    private AppDatabase db;
//    private Ebook ebook;
//    private PdfRenderer pdfRenderer;
//    private PdfPageAdapter pdfPageAdapter;
//    private static final String ARG_EBOOK_ID = "ebookId";
//
//    public static ReadingFragment newInstance(int ebookId) {
//        ReadingFragment fragment = new ReadingFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_EBOOK_ID, ebookId);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//        db = AppDatabase.getInstance(getContext());
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_reading, container, false);
//        pdfRecyclerView = view.findViewById(R.id.pdf_recycler_view);
//        epubWebView = view.findViewById(R.id.epub_web_view);
//        prefs = getContext().getSharedPreferences("reading_settings", Context.MODE_PRIVATE);
//
//        int ebookId = getArguments().getInt(ARG_EBOOK_ID);
//        ebook = db.ebookDao().getEbookById(ebookId);
//        if (ebook != null) {
//            if ("PDF".equalsIgnoreCase(ebook.format)) {
//                setupPdfRenderer();
//                setupRecyclerView();
//                pdfRecyclerView.setVisibility(View.VISIBLE);
//                epubWebView.setVisibility(View.GONE);
//            } else if ("EPUB".equalsIgnoreCase(ebook.format)) {
//                setupEpubReader();
//                pdfRecyclerView.setVisibility(View.GONE);
//                epubWebView.setVisibility(View.VISIBLE);
//            }
//            applyReadingSettings();
//            restoreReadingProgress();
//        }
//
//        return view;
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.reading_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_settings) {
//            new ReadingSettingsFragment().show(getChildFragmentManager(), "ReadingSettings");
//            return true;
//        } else if (item.getItemId() == R.id.action_bookmark) {
//            Bundle args = new Bundle();
//            args.putInt("ebookId", ebook.id);
//            Navigation.findNavController(getView()).navigate(R.id.action_reading_to_bookmark, args);
//            return true;
//        } else if (item.getItemId() == R.id.action_note) {
//            showNoteDialog();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        saveReadingProgress();
//    }
//
//    @Override
//    public void onDestroyView() {
//        if (pdfRenderer != null) {
//            pdfRenderer.close();
//        }
//        super.onDestroyView();
//    }
//
//    private void setupPdfRenderer() {
//        try {
//            File file = new File(ebook.filePath);
//            ParcelFileDescriptor descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
//            pdfRenderer = new PdfRenderer(descriptor);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void setupRecyclerView() {
//        pdfRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        pdfPageAdapter = new PdfPageAdapter(pdfRenderer);
//        pdfRecyclerView.setAdapter(pdfPageAdapter);
//    }
//
//    private void setupEpubReader() {
//        String htmlPath = EpubUtils.extractEpub(getContext(), ebook.filePath);
//        if (htmlPath != null) {
//            epubWebView.getSettings().setJavaScriptEnabled(true);
//            epubWebView.loadUrl("file://" + htmlPath);
//        }
//    }
//
//    private void applyReadingSettings() {
//        int bgColorIndex = prefs.getInt("background_color", 0);
//        String bgColor = bgColorIndex == 1 ? "#121212" : "#FFFFFF";
//        pdfRecyclerView.setBackgroundColor(Color.parseColor(bgColor));
//        epubWebView.setBackgroundColor(Color.parseColor(bgColor));
//    }
//
//    private void restoreReadingProgress() {
//        ReadingHistory history = db.readingHistoryDao().getHistoryForEbook(ebook.id);
//        if (history != null) {
//            if ("PDF".equalsIgnoreCase(ebook.format) && pdfRecyclerView.getLayoutManager() != null) {
//                pdfRecyclerView.scrollToPosition(history.lastPage);
//            } else if ("EPUB".equalsIgnoreCase(ebook.format)) {
//                epubWebView.scrollTo(0, (int) ebook.readingProgress);
//            }
//        }
//    }
//
//    private void saveReadingProgress() {
//        if ("PDF".equalsIgnoreCase(ebook.format)) {
//            LinearLayoutManager layoutManager = (LinearLayoutManager) pdfRecyclerView.getLayoutManager();
//            if (layoutManager != null) {
//                int currentPageIndex = layoutManager.findFirstVisibleItemPosition();
//                ebook.readingProgress = (currentPageIndex * 100f / pdfRenderer.getPageCount());
//                db.ebookDao().update(ebook);
//
//                ReadingHistory existingHistory = db.readingHistoryDao().getHistoryForEbook(ebook.id);
//                if (existingHistory != null) {
//                    existingHistory.lastReadTime = System.currentTimeMillis();
//                    existingHistory.lastPage = currentPageIndex;
//                    final ReadingHistory historyToUpdate = existingHistory;
//                    new Thread(() -> {
//                        db.readingHistoryDao().update(historyToUpdate);
//                    }).start();
//                } else {
//                    final ReadingHistory newHistory = new ReadingHistory();
//                    newHistory.ebookId = ebook.id;
//                    newHistory.lastReadTime = System.currentTimeMillis();
//                    newHistory.lastPage = currentPageIndex;
//                    new Thread(() -> {
//                        db.readingHistoryDao().insert(newHistory);
//                    }).start();
//                }
//            }
//        } else if ("EPUB".equalsIgnoreCase(ebook.format)) {
//            int scrollY = epubWebView.getScrollY();
//            ebook.readingProgress = scrollY;
//            db.ebookDao().update(ebook);
//
//            ReadingHistory existingHistory = db.readingHistoryDao().getHistoryForEbook(ebook.id);
//            if (existingHistory != null) {
//                existingHistory.lastReadTime = System.currentTimeMillis();
//                existingHistory.lastPage = scrollY;
//                final ReadingHistory historyToUpdate = existingHistory;
//                new Thread(() -> {
//                    db.readingHistoryDao().update(historyToUpdate);
//                }).start();
//            } else {
//                final ReadingHistory newHistory = new ReadingHistory();
//                newHistory.ebookId = ebook.id;
//                newHistory.lastReadTime = System.currentTimeMillis();
//                newHistory.lastPage = scrollY;
//                new Thread(() -> {
//                    db.readingHistoryDao().insert(newHistory);
//                }).start();
//            }
//        }
//    }
//
//    private void showNoteDialog() {
//        int currentPosition;
//        if ("PDF".equalsIgnoreCase(ebook.format)) {
//            LinearLayoutManager layoutManager = (LinearLayoutManager) pdfRecyclerView.getLayoutManager();
//            if (layoutManager == null) return;
//            currentPosition = layoutManager.findFirstVisibleItemPosition();
//        } else {
//            currentPosition = epubWebView.getScrollY();
//        }
//
//        EditText noteInput = new EditText(getContext());
//        noteInput.setHint("Ghi chú");
//        new AlertDialog.Builder(getContext())
//                .setTitle("Thêm Ghi Chú")
//                .setView(noteInput)
//                .setPositiveButton("Lưu", (dialog, which) -> {
//                    Bookmark bookmark = new Bookmark();
//                    bookmark.ebookId = ebook.id;
//                    bookmark.page = currentPosition;
//                    bookmark.note = noteInput.getText().toString();
//                    bookmark.timestamp = System.currentTimeMillis();
//                    db.bookmarkDao().insert(bookmark);
//                })
//                .setNegativeButton("Hủy", null)
//                .show();
//    }
//}