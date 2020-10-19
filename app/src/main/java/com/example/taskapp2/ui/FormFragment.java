package com.example.taskapp2.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.taskapp2.App;
import com.example.taskapp2.R;
import com.example.taskapp2.models.Task;

public class FormFragment extends Fragment {
    EditText editText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_form, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = view.findViewById(R.id.editText);
        if (getArguments() != null) {
            Task task = (Task) getArguments().getSerializable("task");
            editText.setText(task.getTitle());
        }
        view.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

    }

    private void save() {
        String title = editText.getText().toString();
        Task task = new Task(title, System.currentTimeMillis());
        App.instance.getAppDatabase().taskDao().insert(task);
        Bundle bundle = new Bundle();
        if (getArguments() != null) {

            bundle.putSerializable("update", task);
            getParentFragmentManager().setFragmentResult("updateform", bundle);
        } else {
            bundle.putSerializable("task", task);
            getParentFragmentManager().setFragmentResult("form", bundle);
        }
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigateUp();
    }
}