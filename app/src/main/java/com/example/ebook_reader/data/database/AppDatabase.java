package com.example.ebook_reader.data.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.ebook_reader.data.dao.BookmarkDao;
import com.example.ebook_reader.data.dao.EbookDao;
import com.example.ebook_reader.data.dao.ReadingHistoryDao;
import com.example.ebook_reader.data.dao.UserDao;
import com.example.ebook_reader.data.entity.Bookmark;
import com.example.ebook_reader.data.entity.Ebook;
import com.example.ebook_reader.data.entity.ReadingHistory;
import com.example.ebook_reader.data.entity.User;

@Database(entities = {Ebook.class, ReadingHistory.class, Bookmark.class, User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EbookDao ebookDao();
    public abstract ReadingHistoryDao readingHistoryDao();
    public abstract BookmarkDao bookmarkDao();
    public abstract UserDao userDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "ebook_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries() // Tạm thời cho phép truy cập trên Main Thread để debug
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}