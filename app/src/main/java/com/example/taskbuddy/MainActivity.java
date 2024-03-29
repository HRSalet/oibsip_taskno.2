package com.example.taskbuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskbuddy.Adapter.TaskAdapter;
import com.example.taskbuddy.Model.TodoModel;
import com.example.taskbuddy.Utils.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener{

    private RecyclerView recyclerView;
    private FloatingActionButton factionButton, fLogoutButton;
    private DatabaseHelper dbHelper;
    private List<TodoModel> list;
    private TaskAdapter adapter;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recyclerViews);
        factionButton = findViewById(R.id.actionButton);
        fLogoutButton = findViewById(R.id.logoutButton);
        user = auth.getCurrentUser();

        if (user == null){
            Intent intent = new Intent(getApplicationContext(), activity_login.class);
            startActivity(intent);
            finish();
        }

        dbHelper = new DatabaseHelper(MainActivity.this);
        list = new ArrayList<>();
        adapter = new TaskAdapter(dbHelper, MainActivity.this);

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        list = dbHelper.getAllTasks();
        Collections.reverse(list);
        adapter.setTasks(list);

        factionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.tag);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        fLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), activity_login.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        list = dbHelper.getAllTasks();
        Collections.reverse(list);
        adapter.setTasks(list);
        adapter.notifyDataSetChanged();
    }
}