package com.example.ebook_reader_app.view;



import android.content.Context;
import android.webkit.WebView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.booksreading.utils.FileUtils;

public class EpubReaderView {
    private Context context;
    private String[] htmlPages;
    private ViewPager2.PageAdapter adapter;

    public EpubReaderView(Context context) {
        this.context = context;
    }

    public void loadEpub(String filePath) {
        htmlPages = FileUtils.extractEpubPages(filePath); // Giả định phương thức tiện ích để trích xuất trang HTML
        adapter = new ViewPager2.PageAdapter() {
            @Override
            public int getItemCount() {
                return htmlPages.length;
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                WebView webView = holder.webView;
                webView.loadDataWithBaseURL(null, htmlPages[position], "text/html", "UTF-8", null);
            }
        };
    }

    public ViewPager2.PageAdapter getAdapter() {
        return adapter;
    }
}