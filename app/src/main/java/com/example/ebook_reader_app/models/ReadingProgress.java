package com.example.ebook_reader_app.models;



import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reading_progress")
public class ReadingProgress {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String bookId;
    private int pageNumber;

    // Getters và setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }
    public int getPageNumber() { return pageNumber; }
    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }
}