package com.example.ebook_reader_app.database;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.booksreading.models.ReadingProgress;

@Dao
public interface ReadingProgressDao {
    @Insert
    void insert(ReadingProgress progress);

    @Query("SELECT * FROM reading_progress WHERE bookId = :bookId")
    LiveData<ReadingProgress> getProgressForBook(String bookId);
}