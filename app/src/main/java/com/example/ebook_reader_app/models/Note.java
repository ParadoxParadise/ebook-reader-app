package com.example.ebook_reader_app.models;



import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String bookId;
    private int pageNumber;
    private String noteText;

    // Getters và setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }
    public int getPageNumber() { return pageNumber; }
    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }
    public String getNoteText() { return noteText; }
    public void setNoteText(String noteText) { this.noteText = noteText; }
}