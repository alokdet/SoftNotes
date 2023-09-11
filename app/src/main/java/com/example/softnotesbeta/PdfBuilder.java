package com.example.softnotesbeta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfBuilder {

    private Context context;
    private String text;
    List<String> texts = new ArrayList<>();

    public PdfBuilder(String text, Context context) {
        this.context = context;
        this.text = text;
    }

    public void makePdf(String fileName) {
        int totalCharCount = 0;

        for (int i = 0; i < text.length(); i++) {
            totalCharCount++;
        }

        int charsPerPage = 4900;
        int pages = totalCharCount / charsPerPage;
        int remainedPages = totalCharCount % charsPerPage;

        if (remainedPages > 0) {
            pages++;
        }

        int k = pages, count = 0;

        while (k != 0) {
            StringBuilder textPerPage = new StringBuilder();

            for (int y = 0; y < charsPerPage; y++) {
                if (count < totalCharCount) {
                    textPerPage.append(text.charAt(count));
                    if (y == (charsPerPage - 1) && text.charAt(count) != ' ') {
                        while (text.charAt(count) != '\n') {
                            count++;
                            textPerPage.append(text.charAt(count));
                        }
                    } else {
                        count++;
                    }
                }
            }
            texts.add(textPerPage.toString());
            k--;
        }
        PdfDocument pdfDocument = new PdfDocument();
        int pageNumber = 0;

        try {
            pageNumber++;
            for (String eachPageText : texts) {
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, pageNumber).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                Canvas canvas = page.getCanvas();
                TextPaint textPaint = new TextPaint();
                textPaint.setTextSize(12);
                textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_regular));
                StaticLayout textLayout = new StaticLayout(eachPageText, textPaint, canvas.getWidth() - 60, Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
                canvas.save();

                int textX = 30;
                int textY = 30;
                canvas.translate(textX, textY);
                textLayout.draw(canvas);
                canvas.restore();

                pdfDocument.finishPage(page);
            }
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName + ".pdf");
            FileOutputStream outputStream = new FileOutputStream(file);
            pdfDocument.writeTo(outputStream);
            Toast.makeText(context, "Saved.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
    }
}
