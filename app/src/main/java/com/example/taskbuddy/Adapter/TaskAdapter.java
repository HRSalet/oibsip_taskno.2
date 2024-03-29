package com.example.taskbuddy.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskbuddy.AddNewTask;
import com.example.taskbuddy.MainActivity;
import com.example.taskbuddy.Model.TodoModel;
import com.example.taskbuddy.R;
import com.example.taskbuddy.Utils.DatabaseHelper;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<TodoModel> mList;
    private MainActivity activity;
    private DatabaseHelper myDB;

    public TaskAdapter(DatabaseHelper myDB, MainActivity activity){
        this.activity = activity;
        this.myDB = myDB;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final TodoModel item = mList.get(position);
        holder.checkBox.setText(item.getTask());
        holder.checkBox.setChecked(toBoolean(item.getStatus()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    myDB.updateStatus(item.getId(),1);
                }else {
                    myDB.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    public boolean toBoolean(int number){
        return number!=0;
    }

    public Context getContext(){
        return activity;
    }

    public void setTasks(List<TodoModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position){
        TodoModel item = mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editTask(int position){
        TodoModel item = mList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(), task.getTag());
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
