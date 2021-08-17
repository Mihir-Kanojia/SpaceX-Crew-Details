package com.example.crewdetails.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MainDao {

    //Insert query
    @Insert(onConflict = REPLACE)
    void insert(MainData mainData);


    //Delete all query
    @Delete
    void reset(List<MainData> mainData);


    //Get all data  query
    @Query("SELECT * FROM table_name")
    List<MainData> getAll();


}
