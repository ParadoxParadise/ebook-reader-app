package com.example.ebook_reader.ui.adapter;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ebook_reader.R;

public class PdfPageAdapter extends RecyclerView.Adapter<PdfPageAdapter.PageViewHolder> {
    private PdfRenderer pdfRenderer;

    public PdfPageAdapter(PdfRenderer pdfRenderer) {
        this.pdfRenderer = pdfRenderer;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf_page, parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        // Render trang PDF tại vị trí position
        PdfRenderer.Page page = pdfRenderer.openPage(position);
        Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        holder.pageImageView.setImageBitmap(bitmap);
        page.close();
    }

    @Override
    public int getItemCount() {
        return pdfRenderer != null ? pdfRenderer.getPageCount() : 0;
    }

    static class PageViewHolder extends RecyclerView.ViewHolder {
        ImageView pageImageView;

        PageViewHolder(View itemView) {
            super(itemView);
            pageImageView = itemView.findViewById(R.id.pdf_page_image);
        }
    }
}