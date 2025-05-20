package com.example.ebook_reader.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    public String uid;
    public String name;
    public String email;
    public String passwordHash;
}