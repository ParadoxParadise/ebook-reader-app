package com.example.ebook_reader.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.ebook_reader.data.entity.Bookmark;
import java.util.List;

@Dao
public interface BookmarkDao {
    @Insert
    void insert(Bookmark bookmark);

    @Delete
    void delete(Bookmark bookmark);

    @Query("SELECT * FROM bookmarks WHERE ebookId = :ebookId ORDER BY page ASC")
    LiveData<List<Bookmark>> getBookmarksForEbook(int ebookId);
}