package com.example.taskbuddy.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.taskbuddy.Model.TodoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private static final String Database_name = "TASKS_DATABASE";
    private static final String Table_name = "TASKS_TABLE";
    private static final String Column_1 = "ID";
    private static final String Column_2 = "Task";
    private static final String Column_3 = "Status";
    public DatabaseHelper(@Nullable Context context) {
        super(context, Database_name, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + Table_name + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, Task TEXT, Status INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table_name);
        onCreate(sqLiteDatabase);
    }

    public void insertTask(TodoModel model){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Column_2, model.getTask());
        values.put(Column_3, 0);
        db.insert(Table_name, null, values);
    }

    public void updateTask(int id, String task){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Column_2, task);
        db.update(Table_name, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void updateStatus(int id, int status){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Column_3, status);
        db.update(Table_name, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id){
        db = this.getWritableDatabase();
        db.delete(Table_name, "ID=?", new String[]{String.valueOf(id)});
    }


    @SuppressLint("Range")
    public List<TodoModel> getAllTasks(){
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<TodoModel> modelList = new ArrayList<>();
        db.beginTransaction();
        try {
            cursor = db.query(Table_name, null, null, null, null, null, null);
            if(cursor != null){
                if (cursor.moveToFirst()){
                    do {
                        TodoModel task = new TodoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(Column_1)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(Column_2)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(Column_3)));
                        modelList.add(task);
                    } while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }
}
