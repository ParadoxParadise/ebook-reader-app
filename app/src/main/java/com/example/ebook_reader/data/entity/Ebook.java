package com.example.ebook_reader.data.entity;



import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ebooks")
public class Ebook {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String author;
    public String coverUrl;
    public String filePath;
    public String genre;
    public String format;
    public long timestamp;
    public float readingProgress;
    public boolean isFavorite;
}