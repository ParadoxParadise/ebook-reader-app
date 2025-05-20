package com.example.ebook_reader.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "bookmarks",
        foreignKeys = @ForeignKey(
                entity = Ebook.class,
                parentColumns = "id",
                childColumns = "ebookId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index(value = "ebookId")}  // Thêm index vào khóa ngoại
)
public class Bookmark {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int ebookId;
    public int page;
    public String note;
    public long timestamp;
}
