package com.example.taskapp2.room;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.taskapp2.models.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM task")
    void someList();

    @Query("SELECT * FROM task ORDER BY CASE WHEN :isAsc = 1 THEN task.title END ASC, CASE WHEN :isAsc = 0 THEN task.title END DESC")
    List<Task> getTasksAlphabetically(boolean isAsc);

    @Query("SELECT * FROM task ORDER BY CASE WHEN :isAsc = 1 THEN task.createdAt END ASC, " +
            "CASE WHEN :isAsc = 0 THEN task.createdAt END DESC")
    List<Task> getTaskDateAlphabetically(boolean isAsc);
}
