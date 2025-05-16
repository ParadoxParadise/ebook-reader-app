package com.example.ebook_reader_app.view;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import androidx.viewpager2.widget.ViewPager2;
import java.io.File;

public class PdfReaderView {
    private Context context;
    private PdfRenderer pdfRenderer;
    private ViewPager2.PageAdapter adapter;

    public PdfReaderView(Context context) {
        this.context = context;
    }

    public void loadPdf(String filePath) {
        try {
            File file = new File(filePath);
            ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(fileDescriptor);
            adapter = new ViewPager2.PageAdapter() {
                @Override
                public int getItemCount() {
                    return pdfRenderer.getPageCount();
                }

                @Override
                public void onBindViewHolder(ViewHolder holder, int position) {
                    PdfRenderer.Page page = pdfRenderer.openPage(position);
                    Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    holder.imageView.setImageBitmap(bitmap);
                    page.close();
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ViewPager2.PageAdapter getAdapter() {
        return adapter;
    }
}