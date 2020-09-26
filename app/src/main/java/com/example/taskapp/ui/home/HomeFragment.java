package com.example.taskapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskapp.R;
import com.example.taskapp.models.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment implements TaskAdapter.ItemClickListener {
    private TaskAdapter adapter;
    private FloatingActionButton fab;
    private ArrayList<Task> list = new ArrayList<>();
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TaskAdapter(list, this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = view.findViewById(R.id.fab);
        recyclerView = view.findViewById(R.id.recyclerView);
        initListeners();
        initList();

    }

    private void initList() {
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
    }

    private void initListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForm();
            }
        });
        getParentFragmentManager().setFragmentResultListener("form", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Log.e("TAG", "onFragmentResult:" + result.getString("task"));
                adapter.setItem((Task) result.getSerializable("task"));
            }
        });
    }

    private void openForm() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_navigation_home_to_formFragment);
    }

    @Override
    public void onItemClick(final int position) {
        Bundle bundle=  new Bundle();
        bundle.putSerializable("task", list.get(position));
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_navigation_home_to_formFragment, bundle);
        getParentFragmentManager().setFragmentResultListener("updateform", this, new FragmentResultListener(){

            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                adapter.update(position, (Task) result.getSerializable("update"));
            }
        });
    }
}
