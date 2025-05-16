package com.example.ebook_reader_app.database;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.booksreading.models.Bookmark;
import java.util.List;

@Dao
public interface BookmarkDao {
    @Insert
    void insert(Bookmark bookmark);

    @Query("SELECT * FROM bookmarks WHERE bookId = :bookId")
    List<Bookmark> getBookmarksForBook(String bookId);
}