package com.example.ebook_reader.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.ebook_reader.data.entity.ReadingHistory;
import java.util.List;

//@Dao
//public interface ReadingHistoryDao {
//    @Insert
//    void insert(ReadingHistory history);
//
//    @Delete
//    void delete(ReadingHistory history);
//
//    @Query("SELECT * FROM reading_history ORDER BY lastReadTime DESC")
//    LiveData<List<ReadingHistory>> getReadingHistory();
//
//    @Query("SELECT * FROM reading_history WHERE ebookId = :ebookId ORDER BY lastReadTime DESC LIMIT 1")
//    ReadingHistory getHistoryForEbook(int ebookId);
//}
@Dao
public interface ReadingHistoryDao {
    @Insert
    void insert(ReadingHistory history);

    @Update
    void update(ReadingHistory history);

    @Delete
    void delete(ReadingHistory history);

    @Query("SELECT * FROM reading_history WHERE ebookId = :ebookId ORDER BY lastReadTime DESC LIMIT 1")
    ReadingHistory getHistoryForEbook(int ebookId);

    @Query("SELECT rh.* FROM reading_history rh " +
            "INNER JOIN (SELECT ebookId, MAX(lastReadTime) as maxTime " +
            "FROM reading_history GROUP BY ebookId) latest " +
            "ON rh.ebookId = latest.ebookId AND rh.lastReadTime = latest.maxTime " +
            "ORDER BY rh.lastReadTime DESC")
    LiveData<List<ReadingHistory>> getReadingHistory();
}




//@Dao
//public interface ReadingHistoryDao {
//    @Insert
//    void insert(ReadingHistory history);
//
//    @Update
//    void update(ReadingHistory history);
//
//    @Delete
//    void delete(ReadingHistory history);
//
//    @Query("SELECT * FROM reading_history WHERE ebookId = :ebookId ORDER BY lastReadTime DESC LIMIT 1")
//    ReadingHistory getHistoryForEbook(int ebookId);
//
//    @Query("SELECT rh.* FROM reading_history rh " +
//            "INNER JOIN (SELECT ebookId, MAX(lastReadTime) as maxTime " +
//            "FROM reading_history GROUP BY ebookId) latest " +
//            "ON rh.ebookId = latest.ebookId AND rh.lastReadTime = latest.maxTime " +
//            "ORDER BY rh.lastReadTime DESC")
//    LiveData<List<ReadingHistory>> getReadingHistory();
//}