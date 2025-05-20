package com.example.ebook_reader.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.ebook_reader.data.entity.User;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM users WHERE uid = :uid")
    User getUserById(String uid);

    @Query("SELECT * FROM users WHERE email = :email")
    User getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE email = :email AND passwordHash = :passwordHash")
    User authenticate(String email, String passwordHash);
}