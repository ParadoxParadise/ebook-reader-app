package com.example.ebook_reader.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.ebook_reader.data.entity.Ebook;
import java.util.List;

@Dao
public interface EbookDao {
    @Insert
    void insert(Ebook ebook);

    @Update
    void update(Ebook ebook);

    @Delete
    void delete(Ebook ebook);

    @Query("SELECT * FROM ebooks ORDER BY title ASC")
    LiveData<List<Ebook>> getAllEbooks();

    @Query("SELECT * FROM ebooks WHERE title LIKE :query OR author LIKE :query")
    LiveData<List<Ebook>> searchEbooks(String query);

    @Query("SELECT * FROM ebooks WHERE genre = :genre")
    LiveData<List<Ebook>> filterByGenre(String genre);

    @Query("SELECT * FROM ebooks WHERE format = :format")
    LiveData<List<Ebook>> filterByFormat(String format);

    @Query("SELECT * FROM ebooks WHERE isFavorite = 1")
    LiveData<List<Ebook>> filterFavorites();

    @Query("SELECT * FROM ebooks ORDER BY timestamp DESC")
    LiveData<List<Ebook>> getEbooksByDateAdded();

    @Query("SELECT * FROM ebooks ORDER BY readingProgress DESC")
    LiveData<List<Ebook>> getEbooksByProgress();

    @Query("SELECT * FROM ebooks WHERE id = :id")
    Ebook getEbookById(int id);
}