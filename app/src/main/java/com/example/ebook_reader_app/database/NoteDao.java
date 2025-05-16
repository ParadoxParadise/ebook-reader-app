package com.example.ebook_reader_app.database;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.booksreading.models.Note;
import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insert(Note note);

    @Query("SELECT * FROM notes WHERE bookId = :bookId")
    List<Note> getNotesForBook(String bookId);
}