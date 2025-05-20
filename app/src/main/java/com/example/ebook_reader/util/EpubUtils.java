//package com.example.ebook_reader.util;
//
//import android.content.Context;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipFile;
//
//public class EpubUtils {
//    public static String extractEpub(Context context, String epubPath) {
//        try {
//            File extractDir = new File(context.getFilesDir(), "epub_" + new File(epubPath).getName());
//            if (!extractDir.exists()) {
//                extractDir.mkdirs();
//            }
//
//            ZipFile zipFile = new ZipFile(epubPath);
//            ZipEntry entry;
//            java.util.Enumeration<? extends ZipEntry> entries = zipFile.entries();
//            while (entries.hasMoreElements()) {
//                entry = entries.nextElement();
//                File outFile = new File(extractDir, entry.getName());
//                if (entry.isDirectory()) {
//                    outFile.mkdirs();
//                    continue;
//                }
//                File parentDir = outFile.getParentFile();
//                if (parentDir != null && !parentDir.exists()) {
//                    parentDir.mkdirs();
//                }
//                InputStream inputStream = zipFile.getInputStream(entry);
//                FileOutputStream outputStream = new FileOutputStream(outFile);
//                byte[] buffer = new byte[1024];
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, bytesRead);
//                }
//                outputStream.close();
//                inputStream.close();
//            }
//            zipFile.close();
//
//            // Tìm tệp HTML chính (thường nằm trong thư mục OEBPS hoặc OPS)
//            File oebpsDir = new File(extractDir, "OEBPS");
//            if (!oebpsDir.exists()) {
//                oebpsDir = new File(extractDir, "OPS");
//            }
//            File htmlFile = new File(oebpsDir, "index.html");
//            if (!htmlFile.exists()) {
//                // Nếu không có index.html, tìm tệp HTML đầu tiên
//                File[] files = oebpsDir.listFiles((dir, name) -> name.endsWith(".html"));
//                if (files != null && files.length > 0) {
//                    htmlFile = files[0];
//                }
//            }
//            return htmlFile.exists() ? htmlFile.getAbsolutePath() : null;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}