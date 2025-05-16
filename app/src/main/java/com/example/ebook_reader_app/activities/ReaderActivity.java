package com.example.ebook_reader_app.activities;



import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.booksreading.R;
import com.example.booksreading.utils.FileUtils;
import com.example.booksreading.view.PdfReaderView;
import com.example.booksreading.view.EpubReaderView;

public class ReaderActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private String filePath;
    private boolean isPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        viewPager = findViewById(R.id.view_pager);
        Intent intent = getIntent();
        filePath = intent.getStringExtra("file_path");
        isPdf = filePath.endsWith(".pdf");

        if (isPdf) {
            PdfReaderView pdfReaderView = new PdfReaderView(this);
            pdfReaderView.loadPdf(filePath);
            viewPager.setAdapter(pdfReaderView.getAdapter());
        } else {
            EpubReaderView epubReaderView = new EpubReaderView(this);
            epubReaderView.loadEpub(filePath);
            viewPager.setAdapter(epubReaderView.getAdapter());
        }
    }
}