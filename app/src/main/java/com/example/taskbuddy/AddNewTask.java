package com.example.taskbuddy;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.taskbuddy.Model.TodoModel;
import com.example.taskbuddy.Utils.DatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.nio.Buffer;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String tag = "AddNewTask";
    private EditText editText;
    private Button saveButton;
    private DatabaseHelper mydb;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_task, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText = view.findViewById(R.id.edittext);
        saveButton = view.findViewById(R.id.btn_save);

        mydb = new DatabaseHelper(getActivity());
        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            editText.setText(task);

            if(task.length() > 0){
                saveButton.setEnabled(false);
            }
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    saveButton.setEnabled(false);
                    saveButton.setBackgroundColor(Color.GRAY);
                }else{
                    saveButton.setEnabled(true);
                    saveButton.setBackgroundColor(Color.BLUE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        boolean finalIsUpdate = isUpdate;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                if(finalIsUpdate){
                    mydb.updateTask(bundle.getInt("id"), text);
                }else {
                    TodoModel item = new TodoModel();
                    item.setTask(text);
                    item.setStatus(0);
                    mydb.insertTask(item);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}
