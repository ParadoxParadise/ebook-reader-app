package com.example.ebook_reader.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "reading_history",
        foreignKeys = @ForeignKey(
                entity = Ebook.class,
                parentColumns = "id",
                childColumns = "ebookId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index(value = "ebookId")}  // Thêm dòng này
)

public class ReadingHistory {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int ebookId;
    public long lastReadTime;
    public int lastPage;
}

//package com.example.ebook_reader.data.entity;
//
//import androidx.room.Entity;
//import androidx.room.ForeignKey;
//import androidx.room.Index;
//import androidx.room.PrimaryKey;
//
//@Entity(
//        tableName = "reading_history",
//        foreignKeys = @ForeignKey(
//                entity = Ebook.class,
//                parentColumns = "id",
//                childColumns = "ebookId",
//                onDelete = ForeignKey.CASCADE
//        ),
//        indices = {@Index(value = "ebookId")})
//
//public class ReadingHistory {
//    @PrimaryKey(autoGenerate = true)
//    public int id;
//    public int ebookId;
//    public long lastReadTime;
//    public int lastPage; // Dùng cho cả số trang (PDF) và vị trí cuộn (EPUB)
